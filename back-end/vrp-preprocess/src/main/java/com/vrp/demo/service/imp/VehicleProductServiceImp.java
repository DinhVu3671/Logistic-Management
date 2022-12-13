package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.entity.tenant.VehicleProduct;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.VehicleProductModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.search.VehicleProductSearch;
import com.vrp.demo.repository.ProductRepository;
import com.vrp.demo.repository.VehicleProductRepository;
import com.vrp.demo.repository.VehicleRepository;
import com.vrp.demo.service.VehicleProductService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("vehicleProductService")
public class VehicleProductServiceImp extends BaseServiceImp<VehicleProductRepository, VehicleProduct, Long> implements VehicleProductService {

    @Autowired
    private VehicleProductRepository vehicleProductRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ProductRepository productRepository;

    private QueryTemplate buildQuery(VehicleProductSearch search) {
        QueryTemplate queryTemplate = getBaseQuery(search);
        String query = queryTemplate.getQuery();
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (search.getVehicleId() != null && search.getVehicleId() > 0) {
            query += " and e.vehicle.id = :vehicleId ";
            params.put("vehicleId", search.getVehicleId());
        }
        if (search.getProductId() != null && search.getProductId() > 0) {
            query += " and e.product.id = :productId ";
            params.put("productId", search.getProductId());
        }
        queryTemplate.setQuery(query);
        queryTemplate.setParameterMap(params);
        return queryTemplate;
    }

    @Override
    public List<VehicleProductModel> find(VehicleProductSearch search) {
        List<VehicleProductModel> vehicleProductModels = new ArrayList<>();
        QueryTemplate queryTemplate = buildQuery(search);
        List<VehicleProduct> vehicleProducts = find(queryTemplate);
        for (VehicleProduct vehicleProduct : vehicleProducts) {
            vehicleProductModels.add(VehicleProduct.convertToModel(vehicleProduct));
        }
        return vehicleProductModels;
    }

    @Override
    public List<VehicleProduct> findVehicleProducts(VehicleProductSearch search) {
        QueryTemplate queryTemplate = buildQuery(search);
        return find(queryTemplate);
    }

    @Override
    @Transactional(readOnly = false)
    public VehicleProductModel update(VehicleProductModel vehicleProductModel) throws CustomException {
        VehicleProduct vehicleProduct = vehicleProductRepository.find(vehicleProductModel.getId());
        if (vehicleProduct == null)
            throw CommonUtils.createException(Code.VEHICLE_PRODUCT_ID_NOT_EXISTED);
        vehicleProduct = vehicleProduct.update(vehicleProductModel);
        vehicleProduct = update(vehicleProduct);
        vehicleProductModel = VehicleProduct.convertToModel(vehicleProduct);
        return vehicleProductModel;
    }

    @Override
    @Transactional(readOnly = false)
    public List<VehicleProduct> create(List<VehicleProduct> vehicleProducts) throws CustomException {
        return getRepository().insert(vehicleProducts);
    }

    @Override
    @Transactional(readOnly = false)
    public List<VehicleProduct> update(List<VehicleProduct> vehicleProducts) throws CustomException {
        return getRepository().update(vehicleProducts);
    }

    @Override
    @Transactional
    @Async
    public void updateVehicleProduct(Vehicle vehicle) throws CustomException {
        VehicleProductSearch search = new VehicleProductSearch();
        search.setVehicleId(vehicle.getId());
        List<VehicleProduct> vehicleProducts = findVehicleProducts(search);
        if (vehicleProducts != null && !vehicleProducts.isEmpty()) {
            for (VehicleProduct vehicleProduct : vehicleProducts) {
                vehicleProduct.setMaxNumber(vehicle.calculateMaxProduct(vehicleProduct.getProduct()));
            }
            update(vehicleProducts);
        } else {
            vehicleProducts = new ArrayList<>();
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                VehicleProduct vehicleProduct = new VehicleProduct();
                vehicleProduct.setVehicle(vehicle);
                vehicleProduct.setProduct(product);
                vehicleProduct.setMaxNumber(vehicle.calculateMaxProduct(product));
                vehicleProducts.add(vehicleProduct);
            }
            create(vehicleProducts);
        }
        return;
    }

    @Override
    @Transactional
    @Async
    public void updateVehicleProduct(Product product) throws CustomException {
        VehicleProductSearch search = new VehicleProductSearch();
        search.setProductId(product.getId());
        List<VehicleProduct> vehicleProducts = findVehicleProducts(search);
        if (vehicleProducts != null && !vehicleProducts.isEmpty()) {
            for (VehicleProduct vehicleProduct : vehicleProducts) {
                vehicleProduct.setMaxNumber(vehicleProduct.getVehicle().calculateMaxProduct(product));
            }
            update(vehicleProducts);
        } else {
            List<Vehicle> vehicles = vehicleRepository.findAll();
            for (Vehicle vehicle : vehicles) {
                VehicleProduct vehicleProduct = new VehicleProduct();
                vehicleProduct.setVehicle(vehicle);
                vehicleProduct.setProduct(product);
                vehicleProduct.setMaxNumber(vehicle.calculateMaxProduct(product));
                vehicleProducts.add(vehicleProduct);
            }
            create(vehicleProducts);
        }
        return;
    }

    @Override
    public VehicleProductModel findOne(Long id) {
        VehicleProduct VehicleProduct = find(id);
        VehicleProductModel VehicleProductModel = VehicleProduct.convertToModel(VehicleProduct);
        return VehicleProductModel;
    }

    @Override
    public VehicleProductRepository getRepository() {
        return this.vehicleProductRepository;
    }
}
