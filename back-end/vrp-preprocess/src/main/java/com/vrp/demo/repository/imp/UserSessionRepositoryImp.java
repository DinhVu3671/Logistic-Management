package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.common.UserSession;
import com.vrp.demo.repository.UserSessionRepository;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.stereotype.Repository;

@Repository(value = "userSessionRepositoryImp")
public class UserSessionRepositoryImp extends BaseRepositoryImp<UserSession, Long> implements UserSessionRepository {

    public UserSessionRepositoryImp() {
        super(UserSession.class);
    }

    @Override
    public UserSession findBySessionId(String sessionId) {
        String query = " from UserSession e where e.sessionId = '" + sessionId + "'";
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setQuery(query);
        return findOne(queryTemplate);
    }

}
