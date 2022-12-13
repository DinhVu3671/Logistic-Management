package com.vrp.demo.service.imp;

import com.vrp.demo.entity.common.Role;
import com.vrp.demo.repository.RoleRepository;
import com.vrp.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "roleService")
public class RoleServiceImp extends BaseServiceImp<RoleRepository, Role, Long> implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleRepository getRepository() {
        return this.roleRepository;
    }

    @Override
    public Role findUserRole() {
        return getRepository().findUserRole();
    }
}
