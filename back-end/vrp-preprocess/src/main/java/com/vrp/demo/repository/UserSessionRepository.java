package com.vrp.demo.repository;

import com.vrp.demo.entity.common.UserSession;

public interface UserSessionRepository extends BaseRepository<UserSession, Long> {

    public UserSession findBySessionId(String sessionId);

}
