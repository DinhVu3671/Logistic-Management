//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vrp.demo.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.DriverModel;
import com.vrp.demo.models.UserSessionModel;
import com.vrp.demo.models.driver.DriverResponsive;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.user.DriverEditation;
import com.vrp.demo.models.user.UserPassword;
import com.vrp.demo.service.DriverService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/drivers"})
public class DriverController {
    @Autowired
    private DriverService driverService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    public DriverController() {
    }

    @PostMapping({"/sign-up-by-gmail"})
    public ResponseEntity<ResponseData> signupByGmail(@RequestBody DriverModel driverModel) throws CustomException {
        driverModel = this.driverService.signupByGmail(driverModel);
        return this.responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, driverModel);
    }

    @PostMapping({"/sign-in"})
    public ResponseEntity<ResponseData> signIn(@RequestBody DriverModel driverModel) {
        UserSessionModel userSessionModel = this.driverService.signin(driverModel);
        return this.responsePreProcessor.buildResponseEntity(HttpStatus.OK, userSessionModel == null ? Code.LOGIN_FAIL : Code.LOGIN_SUCCESS, userSessionModel);
    }
    @GetMapping({"/getInfo"})
    public ResponseEntity<ResponseData> getInfo(@RequestBody DriverModel driverModel) {
        DriverResponsive driverResponsive = driverService.getInfo(driverModel.getId());
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, driverResponsive);

    }

    @PostMapping({"/change-password"})
    public ResponseEntity<ResponseData> changePassword(@RequestBody UserPassword userPassword) {
        UserSessionModel userSessionModel = this.driverService.changePassword(userPassword);
        return this.responsePreProcessor.buildResponseEntity(HttpStatus.OK, userSessionModel == null ? Code.FAIL : Code.SUCCESS, userSessionModel);
    }

    @PostMapping({"/edit-info"})
    public ResponseEntity<ResponseData> editDriverInfo(@RequestBody DriverEditation driverEditation) {
        UserSessionModel userSessionModel = this.driverService.editInfo(driverEditation);
        return this.responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, userSessionModel);
    }
}
