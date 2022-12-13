package com.vrp.demo.service.imp;

import com.vrp.demo.entity.common.Role;
import com.vrp.demo.entity.common.User;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.UserModel;
import com.vrp.demo.models.UserSessionModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.search.UserSearch;
import com.vrp.demo.repository.UserRepository;
import com.vrp.demo.service.RoleService;
import com.vrp.demo.service.UserService;
import com.vrp.demo.service.UserSessionService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service(value = "userService")
public class UserServiceImp extends BaseServiceImp<UserRepository, User, Long> implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GMailSender gmailSender;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MultiTenantService multiTenantService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserSessionService userSessionService;

    private QueryTemplate buildQuery(UserSearch search) {
        QueryTemplate queryTemplate = getBaseQuery(search);
        String query = queryTemplate.getQuery();
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (search.getEmail() != null && !search.getEmail().isEmpty()) {
            query += " and e.email like :email ";
            params.put("email", "%" + search.getId() + "%");
        }
        if (search.getFullName() != null && !search.getFullName().isEmpty()) {
            query += " and e.fullName like :fullName ";
            params.put("fullName", "%" + search.getFullName() + "%");
        }
        if (search.getUserName() != null && !search.getUserName().isEmpty()) {
            query += " and e.userName like :userName ";
            params.put("userName", "%" + search.getUserName() + "%");
        }
        if (search.getRoleId() != null && search.getRoleId() > 0) {
            query += " and e.role.id = :roleId ";
            params.put("roleId", search.getRoleId());
        }
        queryTemplate.setQuery(query);
        queryTemplate.setParameterMap(params);
        return queryTemplate;
    }

    void initDBForUser(String userName) {
        multiTenantService.initDatabase(userName);
    }

    private void checkExistedEmail(UserModel userModel) throws CustomException {
        User user = null;
        try {
            user = userRepository.findByEmail(userModel.getEmail());
        } catch (Exception e) {
            return;
        }
        if (user != null)
            throw CommonUtils.createException(Code.EMAIL_EXISTED);
    }

    @Override
    public UserRepository getRepository() {
        return this.userRepository;
    }

    @Override
    public List<UserModel> find(UserSearch search) {
        List<UserModel> userModels = new ArrayList<>();
        QueryTemplate queryTemplate = buildQuery(search);
        List<User> users = find(queryTemplate);
        for (User user : users) {
            userModels.add(User.convertToModel(user, !search.isAdmin()));
        }
        return userModels;
    }

    @Override
    public Page<UserModel> search(UserSearch search) {
        CommonUtils.setDefaultPageIfNull(search);
        QueryTemplate queryTemplate = buildQuery(search);
        Page<User> users = search(queryTemplate);
        return users.map(user -> {
            UserModel model = User.convertToModel(user, !search.isAdmin());
            return model;
        });
    }

    @Override
    @Transactional(readOnly = false)
    public UserModel create(UserModel userModel) {
        Role role = roleService.find(userModel.getRole().getId());
        User user = UserModel.convertToEntity(userModel);
        user.setRole(role);
        user = create(user);
        userModel = User.convertToModel(user, true);
        return userModel;
    }

    @Override
    public UserModel findOne(Long id) {
        User user = find(id);
        UserModel userModel = User.convertToModel(user, true);
        return userModel;
    }

    @Override
    @Transactional
    public UserModel signup(UserModel userModel) throws CustomException {
        checkExistedId(userModel, Code.USER_ID_EXISTED);
        Role role = roleService.findUserRole();
        User user = UserModel.convertToEntity(userModel);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user = create(user);
        userModel = User.convertToModel(user, true);
        multiTenantService.initDatabase(userModel.getUserName());
        multiTenantService.createCollectionForUser(userModel.getUserName());
        return userModel;
    }

    @Override
    @Transactional
    public UserModel signupByGmail(UserModel userModel) throws CustomException {
        checkExistedId(userModel, Code.USER_ID_EXISTED);
        checkExistedEmail(userModel);
        Role role = roleService.findUserRole();
        User user = UserModel.convertToEntity(userModel);
        user.setRole(role);
        user.setActive(true);
        String randomPassword = CommonUtils.generateCommonLangPassword();
        user.setPassword(passwordEncoder.encode(randomPassword));
        gmailSender.sendSimpleMessage(user.getEmail(), "Tạo tài khoản thành công", "Mật khẩu của bạn là: " + randomPassword);
        user = create(user);
        userModel = User.convertToModel(user, true);
        initDBForUser(userModel.getUserName());
        return userModel;
    }

    @Override
    @Transactional
    public UserSessionModel signin(UserModel userModel) {
            User user = null;
        try {
            user = userRepository.findByEmail(userModel.getEmail());
            if (user == null)
                return null;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
        if (passwordEncoder.matches(userModel.getPassword(), user.getPassword()))
            userModel = User.convertToModel(user, true);
        else
            return null;
        UserSessionModel userSessionModel = userSessionService.createUserSession(userModel);
        return userSessionModel;
    }

    @Override
    @Transactional
    public UserSessionModel signout(UserSessionModel userSessionModel) {
        try {
            userSessionModel = userSessionService.getUserSession(userSessionModel);
            userSessionModel = userSessionService.expireUserSession(userSessionModel);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
        return userSessionModel;
    }

    @Override
    @Transactional
    public UserModel getUserByUserName(UserModel userModel) {
        User user = null;
        try {
            user = userRepository.findByUsername(userModel.getUserName());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
        userModel = User.convertToModel(user, true);
        return userModel;
    }

    @Override
    @Transactional
    public UserSessionModel getUserSessionBySessionId(UserSessionModel userSessionModel) {
        try {
            userSessionModel = userSessionService.getUserSession(userSessionModel);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
        return userSessionModel;
    }


}
