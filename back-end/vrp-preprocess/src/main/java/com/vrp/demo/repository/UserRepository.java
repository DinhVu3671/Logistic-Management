package com.vrp.demo.repository;


import com.vrp.demo.entity.common.User;

public interface UserRepository extends BaseRepository<User, Long> {

    public User findByUsername(String userName);

    public User findByEmail(String email);

}
