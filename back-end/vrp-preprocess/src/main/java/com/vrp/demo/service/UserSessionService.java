package com.vrp.demo.service;

import com.vrp.demo.entity.common.UserSession;
import com.vrp.demo.models.UserModel;
import com.vrp.demo.models.UserSessionModel;

public interface UserSessionService extends BaseService<UserSession, Long> {

    public UserSessionModel getUserSession(UserSessionModel userSessionModel);

    public UserSessionModel createUserSession(UserModel userModel);

    public UserSessionModel expireUserSession(UserSessionModel userSessionModel);

    public UserSessionModel getBySessionId(String userSessionId);

}
