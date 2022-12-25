package com.vrp.demo.repository;

import com.vrp.demo.entity.common.User;
import com.vrp.demo.entity.tenant.Vehicle;

public interface VehicleRepository extends BaseRepository<Vehicle, Long> {

    public Vehicle findByUserId(long userId);

}
