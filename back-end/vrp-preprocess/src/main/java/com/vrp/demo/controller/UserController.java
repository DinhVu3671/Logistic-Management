package com.vrp.demo.controller;

import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.UserModel;
import com.vrp.demo.models.UserSessionModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.search.UserSearch;
import com.vrp.demo.service.UserService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {"/users/search"})
    public ResponseEntity<ResponseData> search(@RequestBody UserSearch search, HttpServletRequest request) {
        if (!search.isPaged()) {
            List<UserModel> userModels = userService.find(search);
            return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, userModels);
        }
        Page<UserModel> userModels = userService.search(search);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, userModels);
    }

    @PostMapping(value = {"/users", "/users/", "/sign-up"})
    public ResponseEntity<ResponseData> signup(@RequestBody @Validated UserModel userModel) throws CustomException {
        userModel = userService.signup(userModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, userModel);
    }

    @PostMapping(value = {"/sign-up-by-gmail"})
    public ResponseEntity<ResponseData> signupByGmail(@RequestBody UserModel userModel) throws CustomException {
        userModel = userService.signupByGmail(userModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, userModel);
    }

    @PostMapping(value = {"/sign-in"})
    public ResponseEntity<ResponseData> signin(@RequestBody UserModel userModel) throws CustomException {
        UserSessionModel userSessionModel = userService.signin(userModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, userSessionModel == null ? Code.LOGIN_FAIL : Code.LOGIN_SUCCESS, userSessionModel);
    }

    @PostMapping(value = {"/sign-out"})
    public ResponseEntity<ResponseData> signout(@RequestBody UserSessionModel userSessionModel) throws CustomException {
        userSessionModel = userService.signout(userSessionModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, userSessionModel);
    }

    @PostMapping(value = {"/get-current-user"})
    public ResponseEntity<ResponseData> getCurrentUser(@RequestBody UserSessionModel userSessionModel) throws CustomException {
        userSessionModel = userService.getUserSessionBySessionId(userSessionModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, userSessionModel);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello!";
    }

    @PostMapping("/test")
    public String test() {
        return "test!";
    }
}
