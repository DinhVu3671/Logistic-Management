package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.repository.VehicleRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "vehicleRepository")
public class VehicleRepositoryImp extends BaseRepositoryImp<Vehicle,Long> implements VehicleRepository {

    public VehicleRepositoryImp() {
        super(Vehicle.class);
    }

}
