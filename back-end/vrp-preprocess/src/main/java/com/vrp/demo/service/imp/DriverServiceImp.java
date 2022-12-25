//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vrp.demo.service.imp;

import com.vrp.demo.entity.common.Role;
import com.vrp.demo.entity.common.User;
import com.vrp.demo.entity.tenant.Customer;
import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.DriverModel;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.UserModel;
import com.vrp.demo.models.UserSessionModel;
import com.vrp.demo.models.driver.DriverResponsive;
import com.vrp.demo.models.search.VehicleSearch;
import com.vrp.demo.repository.OrderRepository;
import com.vrp.demo.repository.UserRepository;
import com.vrp.demo.repository.VehicleRepository;
import com.vrp.demo.service.DriverService;
import com.vrp.demo.service.RoleService;
import com.vrp.demo.service.UserService;
import com.vrp.demo.service.UserSessionService;
import com.vrp.demo.utils.QueryTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Service("driverService")
public class DriverServiceImp extends BaseServiceImp<VehicleRepository, Vehicle, Long> implements DriverService {
    @PersistenceContext
    private EntityManager entityManager;
    private static Logger logger = LoggerFactory.getLogger(UserServiceImp.class);
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSessionService userSessionService;

    public DriverServiceImp() {
    }

    public VehicleRepository getRepository() {
        return this.vehicleRepository;
    }

    private QueryTemplate buildQuery(VehicleSearch search) {
        QueryTemplate queryTemplate = this.getBaseQuery(search);
        String query = queryTemplate.getQuery();
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (search.getName() != null && !search.getName().isEmpty()) {
            query = query + " and e.name like :name ";
            params.put("name", "%" + search.getName() + "%");
        }

        queryTemplate.setQuery(query);
        queryTemplate.setParameterMap(params);
        return queryTemplate;
    }

    public Page<DriverModel> search(VehicleSearch search) {
        return null;
    }

    public DriverModel create(DriverModel driverModel) {
        return null;
    }

    public DriverModel update(DriverModel driverModel) throws CustomException {
        return null;
    }

    public int delete(Long id) throws CustomException {
        return 0;
    }

    public DriverModel findOne(Long id) {
        return null;
    }

    @Override
    @Transactional
    public DriverModel signupByGmail(DriverModel driverModel) {
        Role role = this.roleService.findDriverRole();
        User user = DriverModel.convertToUserEntity(driverModel);
        user.setRole(role);
        user.setActive(true);
        String randomPassword = "123456";
        user.setPassword(this.passwordEncoder.encode(randomPassword));
        user = this.userService.created(user);
        Vehicle vehicle = DriverModel.convertToVehicleEntity(driverModel);
        vehicle.setUserId(user.getId());
        vehicle.setAvailable(true);
        vehicle.setDriverName(user.getUserName());
        System.out.println(vehicle.toString());
        vehicle = (Vehicle)this.create(vehicle);
        vehicle = (Vehicle)this.update(vehicle);
        return driverModel;
    }

    @Override
    @Transactional
    public UserSessionModel signin(DriverModel driverModel) {
        User user = null;
        UserModel userModel = null;

        try {
            user = this.userRepository.findByEmail(driverModel.getEmail());
            if (user == null) {
                return null;
            }
        } catch (Exception var5) {
            logger.info(var5.getMessage());
            return null;
        }

        if (this.passwordEncoder.matches(driverModel.getPassword(), user.getPassword())) {
            userModel = User.convertToModel(user, true);
            UserSessionModel var4 = this.userSessionService.createUserSession(userModel);
            return var4;
        } else {
            return null;
        }
    }

    @Override
    public DriverResponsive getInfo(Long idUser) {
        DriverResponsive driverResponsive = new DriverResponsive();

        User user = userService.find(idUser);
        driverResponsive.setUser(user);

        Vehicle vehicle = vehicleRepository.getInfoByUserId(idUser);
        driverResponsive.setVehicle(vehicle);

        return driverResponsive;
    }
}
