package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.VehicleProduct;
import com.vrp.demo.repository.VehicleProductRepository;
import org.springframework.stereotype.Repository;

@Repository("vehicleProductRepository")
public class VehicleProductRepositoryImp extends BaseRepositoryImp<VehicleProduct, Long> implements VehicleProductRepository {
    public VehicleProductRepositoryImp() {
        super(VehicleProduct.class);
    }
}
