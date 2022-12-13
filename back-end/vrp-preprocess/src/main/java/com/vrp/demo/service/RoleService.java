package com.vrp.demo.service;


import com.vrp.demo.entity.common.Role;

public interface RoleService extends BaseService<Role, Long> {

    public Role findUserRole();

}
