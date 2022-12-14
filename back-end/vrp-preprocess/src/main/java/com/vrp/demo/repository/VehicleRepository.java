package com.vrp.demo.repository;

import com.vrp.demo.entity.tenant.Vehicle;

public interface VehicleRepository extends BaseRepository<Vehicle, Long> {
    Vehicle getInfoByUserId(Long userId);

    public Vehicle findByUserId(long userId);

}
