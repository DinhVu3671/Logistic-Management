package com.vrp.demo.controller;

import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.VehicleModel;
import com.vrp.demo.models.VehicleProductModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.search.VehicleSearch;
import com.vrp.demo.service.VehicleProductService;
import com.vrp.demo.service.VehicleService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleProductService vehicleProductService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {"/search"})
    public ResponseEntity<ResponseData> search(@RequestBody VehicleSearch search) {
        if (!search.isPaged()) {
            List<VehicleModel> vehicleModels = vehicleService.find(search);
            return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, vehicleModels);
        }
        Page<VehicleModel> vehicleModels = vehicleService.search(search);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, vehicleModels);
    }

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> create(@RequestBody VehicleModel vehicleModel) throws CustomException {
        vehicleModel = vehicleService.create(vehicleModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, vehicleModel);
    }


    @PutMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> update(@RequestBody VehicleModel vehicleModel) throws CustomException {
        vehicleModel = vehicleService.update(vehicleModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, vehicleModel);
    }

    @PutMapping(value = {"/{vehicleId}/vehicle-product/{id}"})
    public ResponseEntity<ResponseData> updateVehicleProduct(@RequestBody VehicleProductModel vehicleProductModel) throws CustomException {
        vehicleProductModel = vehicleProductService.update(vehicleProductModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, vehicleProductModel);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> detail(@PathVariable(name = "id") Long vehicleId) throws CustomException {
        VehicleModel vehicleModel = vehicleService.findOne(vehicleId);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, vehicleModel);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> delete(@PathVariable Long id) throws CustomException {
        int deletedNumber = vehicleService.delete(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, deletedNumber);
    }

}
