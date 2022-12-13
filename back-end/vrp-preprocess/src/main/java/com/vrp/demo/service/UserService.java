package com.vrp.demo.service;

import com.vrp.demo.entity.common.User;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.UserModel;
import com.vrp.demo.models.UserSessionModel;
import com.vrp.demo.models.search.UserSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService extends BaseService<User, Long> {

    public List<UserModel> find(UserSearch search);

    public Page<UserModel> search(UserSearch search);

    public UserModel create(UserModel userModel);

    public UserModel findOne(Long id);

    public UserModel signup(UserModel userModel) throws CustomException;

    public UserModel signupByGmail(UserModel userModel) throws CustomException;

    public UserSessionModel signin(UserModel userModel);

    public UserSessionModel signout(UserSessionModel userSessionModel);

    public UserModel getUserByUserName(UserModel userModel);

    public UserSessionModel getUserSessionBySessionId(UserSessionModel userSessionModel);

}
