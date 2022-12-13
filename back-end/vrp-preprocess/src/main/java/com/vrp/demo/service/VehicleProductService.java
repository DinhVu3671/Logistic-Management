package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.entity.tenant.VehicleProduct;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.VehicleProductModel;
import com.vrp.demo.models.search.VehicleProductSearch;

import java.util.List;

public interface VehicleProductService extends BaseService<VehicleProduct, Long> {

    public List<VehicleProductModel> find(VehicleProductSearch search);

    public List<VehicleProduct> findVehicleProducts(VehicleProductSearch search);

    public VehicleProductModel findOne(Long id);

    public VehicleProductModel update(VehicleProductModel vehicleProductModel) throws CustomException;

    public List<VehicleProduct> update(List<VehicleProduct> vehicleProducts) throws CustomException;

    public List<VehicleProduct> create(List<VehicleProduct> vehicleProducts) throws CustomException;

    public void updateVehicleProduct(Vehicle vehicle) throws CustomException;

    public void updateVehicleProduct(Product product) throws CustomException;

}
