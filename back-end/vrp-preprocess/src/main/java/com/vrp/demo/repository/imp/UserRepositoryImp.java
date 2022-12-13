package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.common.User;
import com.vrp.demo.repository.UserRepository;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "userRepository")
public class UserRepositoryImp extends BaseRepositoryImp<User, Long> implements UserRepository {

    public UserRepositoryImp() {
        super(User.class);
    }

    public List<User> findAll(QueryTemplate queryTemplate){
        return find(queryTemplate);
    }

    @Override
    public User findByUsername(String userName) {
        String query = " from User e where e.userName = '" + userName + "'";
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setQuery(query);
        return findOne(queryTemplate);
    }

    @Override
    public User findByEmail(String email) {
        String query = " from User e where e.email = '"+email+"'";
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setQuery(query);
        return findOne(queryTemplate);
    }
}
