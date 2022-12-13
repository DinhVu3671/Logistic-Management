package com.vrp.demo.repository;


import com.vrp.demo.entity.common.Role;

public interface RoleRepository extends BaseRepository<Role, Long> {

    public Role findUserRole();

}
