//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.models.DriverModel;
import com.vrp.demo.models.UserSessionModel;
import com.vrp.demo.models.driver.DriverResponsive;

public interface DriverService extends BaseService<Vehicle, Long> {
    DriverModel signupByGmail(DriverModel driverModel);

    UserSessionModel signin(DriverModel driverModel);
    DriverResponsive getInfo(Long idUser);
}