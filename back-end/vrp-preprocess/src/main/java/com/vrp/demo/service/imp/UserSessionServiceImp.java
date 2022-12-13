package com.vrp.demo.service.imp;

import com.vrp.demo.entity.common.User;
import com.vrp.demo.entity.common.UserSession;
import com.vrp.demo.models.UserModel;
import com.vrp.demo.models.UserSessionModel;
import com.vrp.demo.repository.UserRepository;
import com.vrp.demo.repository.UserSessionRepository;
import com.vrp.demo.service.UserSessionService;
import com.vrp.demo.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("userSessionService")
public class UserSessionServiceImp extends BaseServiceImp<UserSessionRepository, UserSession, Long> implements UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserSessionModel getUserSession(UserSessionModel userSessionModel) {
        UserSession userSession = userSessionRepository.findBySessionId(userSessionModel.getSessionId());
        if (userSession == null)
            return null;
        if (userSession.isExpired())
            return null;
        if (userSession.getExpireDate().getTime() < CommonUtils.getCurrentTime().getTime()) {
            expireUserSession(userSessionModel);
            return null;
        }
        return UserSession.convertToModel(userSession);
    }

    @Override
    public UserSessionModel expireUserSession(UserSessionModel userSessionModel) {
        UserSession userSession = userSessionRepository.findBySessionId(userSessionModel.getSessionId());
        userSession.setExpired(true);
        return UserSession.convertToModel(userSession);
    }

    @Override
    public UserSessionModel getBySessionId(String userSessionId) {
        UserSessionModel userSessionModel = new UserSessionModel();
        userSessionModel.setSessionId(userSessionId);
        return getUserSession(userSessionModel);
    }

    @Override
    public UserSessionModel createUserSession(UserModel userModel) {
        User user = userRepository.findByEmail(userModel.getEmail());
        UserSession userSession = new UserSession();
        String sessionId = passwordEncoder.encode(user.getUserName());
        userSession.setSessionId(sessionId);
        userSession.setExpired(false);
        userSession.setUser(user);
        userSession.setExpireDate(new Date(CommonUtils.getCurrentTime().getTime() + 3 * 24 * 3600 * 1000));
        userSession = create(userSession);
        return UserSession.convertToModel(userSession);
    }

    @Override
    public UserSessionRepository getRepository() {
        return userSessionRepository;
    }

}
