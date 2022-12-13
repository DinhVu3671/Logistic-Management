package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.GoodsGroup;
import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.VehicleModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.search.GoodsGroupSearch;
import com.vrp.demo.models.search.VehicleSearch;
import com.vrp.demo.repository.VehicleRepository;
import com.vrp.demo.service.GoodsGroupService;
import com.vrp.demo.service.VehicleProductService;
import com.vrp.demo.service.VehicleService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("vehicleService")
public class VehicleServiceImp extends BaseServiceImp<VehicleRepository, Vehicle, Long> implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleProductService vehicleProductService;
    @Autowired
    private GoodsGroupService goodsGroupService;

    private QueryTemplate buildQuery(VehicleSearch search) {
        QueryTemplate queryTemplate = getBaseQuery(search);
        String query = queryTemplate.getQuery();
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (search.getName() != null && !search.getName().isEmpty()) {
            query += " and e.name like :name ";
            params.put("name", "%" + search.getName() + "%");
        }
        if (search.getAvailable() != null) {
            query += " and e.available = :available ";
            params.put("available", search.getAvailable());
        }
        queryTemplate.setQuery(query);
        queryTemplate.setParameterMap(params);
        return queryTemplate;
    }

    @Override
    public List<VehicleModel> find(VehicleSearch search) {
        List<VehicleModel> vehicleModels = new ArrayList<>();
        QueryTemplate queryTemplate = buildQuery(search);
        List<Vehicle> vehicles = find(queryTemplate);
        for (Vehicle vehicle : vehicles) {
            vehicleModels.add(Vehicle.convertToModel(vehicle));
        }
        return vehicleModels;
    }

    @Override
    public Page<VehicleModel> search(VehicleSearch search) {
        CommonUtils.setDefaultPageIfNull(search);
        QueryTemplate queryTemplate = buildQuery(search);
        Page<Vehicle> vehicles = search(queryTemplate);
        return vehicles.map(vehicle -> {
            VehicleModel model = Vehicle.convertToModel(vehicle);
            return model;
        });
    }

    @Override
    @Transactional(readOnly = false)
    public VehicleModel create(VehicleModel vehicleModel) throws CustomException {
        GoodsGroupSearch goodsGroupSearch = new GoodsGroupSearch();
        goodsGroupSearch.setGoodsGroups(vehicleModel.getExcludedGoodsGroups());
        Vehicle vehicle = VehicleModel.convertToEntity(vehicleModel);
        List<GoodsGroup> excludedGoodsGroups = vehicleModel.getExcludedGoodsGroups().size() > 0 ? goodsGroupService.getGoodsGroups(goodsGroupSearch) : new ArrayList<>();
        vehicle.setExcludedGoodsGroups(excludedGoodsGroups);
        vehicle = create(vehicle);
        vehicleProductService.updateVehicleProduct(vehicle);
        vehicleModel = Vehicle.convertToModel(vehicle);
        return vehicleModel;
    }

    @Override
    @Transactional(readOnly = false)
    public VehicleModel update(VehicleModel vehicleModel) throws CustomException {
        Vehicle vehicle = vehicleRepository.find(vehicleModel.getId());
        if (vehicle == null)
            throw CommonUtils.createException(Code.VEHICLE_ID_NOT_EXISTED);
        GoodsGroupSearch goodsGroupSearch = new GoodsGroupSearch();
        goodsGroupSearch.setGoodsGroups(vehicleModel.getExcludedGoodsGroups());
        List<GoodsGroup> excludedGoodsGroups = vehicleModel.getExcludedGoodsGroups().size() > 0 ? goodsGroupService.getGoodsGroups(goodsGroupSearch) : new ArrayList<>();
        vehicle = vehicle.updateVehicle(vehicleModel);
        vehicle.setExcludedGoodsGroups(excludedGoodsGroups);
        vehicle = update(vehicle);
        vehicleProductService.updateVehicleProduct(vehicle);
        vehicleModel = Vehicle.convertToModel(vehicle);
        return vehicleModel;
    }

    @Override
    @Transactional(readOnly = false)
    public int delete(Long id) throws CustomException {
        Vehicle Vehicle = vehicleRepository.find(id);
        if (Vehicle == null)
            throw CommonUtils.createException(Code.VEHICLE_ID_NOT_EXISTED);
        return vehicleRepository.delete(id);
    }

    @Override
    public VehicleModel findOne(Long id) {
        Vehicle Vehicle = find(id);
        VehicleModel VehicleModel = Vehicle.convertToModel(Vehicle);
        return VehicleModel;
    }

    @Override
    public VehicleRepository getRepository() {
        return this.vehicleRepository;
    }
}
