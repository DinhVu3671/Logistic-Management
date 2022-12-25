package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.common.User;
import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.repository.VehicleRepository;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.stereotype.Repository;

@Repository(value = "vehicleRepository")
public class VehicleRepositoryImp extends BaseRepositoryImp<Vehicle,Long> implements VehicleRepository {

    public VehicleRepositoryImp() {
        super(Vehicle.class);
    }

    @Override
    public Vehicle findByUserId(long userId) {
        String query = " from Vehicle e where e.userId = '+userId+'";
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setQuery(query);
        return findOne(queryTemplate);
    }

}
