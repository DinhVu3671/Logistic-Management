package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.common.Role;
import com.vrp.demo.repository.RoleRepository;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.stereotype.Repository;


@Repository(value = "roleRepository")
public class RoleRepositoryImp extends BaseRepositoryImp<Role,Long> implements RoleRepository {

    public RoleRepositoryImp() {
        super(Role.class);
    }

    @Override
    public Role findUserRole() {
        String query = "From Role e where e.name = 'user'";
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setQuery(query);
        Role role = findOne(queryTemplate);
        return role;
    }
}
