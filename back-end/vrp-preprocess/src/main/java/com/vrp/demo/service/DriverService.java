//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.models.DriverModel;
import com.vrp.demo.models.UserSessionModel;
import com.vrp.demo.models.user.DriverEditation;
import com.vrp.demo.models.user.UserPassword;

public interface DriverService extends BaseService<Vehicle, Long> {
    DriverModel signupByGmail(DriverModel driverModel);

    UserSessionModel signin(DriverModel driverModel);

    UserSessionModel changePassword(UserPassword userPassword);

    UserSessionModel editInfo(DriverEditation driverEditation);
}
