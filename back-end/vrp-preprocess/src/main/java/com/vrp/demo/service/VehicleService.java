package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.VehicleModel;
import com.vrp.demo.models.search.VehicleSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleService extends BaseService<Vehicle, Long> {

    public List<VehicleModel> find(VehicleSearch search);

    public Page<VehicleModel> search(VehicleSearch search);

    public VehicleModel create(VehicleModel vehicleModel) throws CustomException;

    public VehicleModel update(VehicleModel vehicleModel) throws CustomException;

    public int delete(Long id) throws CustomException;

    public VehicleModel findOne(Long id);

}
