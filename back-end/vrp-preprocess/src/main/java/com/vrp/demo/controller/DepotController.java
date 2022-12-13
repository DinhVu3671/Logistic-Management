package com.vrp.demo.controller;

import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.CustomerModel;
import com.vrp.demo.models.DepotModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.search.DepotSearch;
import com.vrp.demo.service.DepotService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/depots")
public class DepotController {

    @Autowired
    private DepotService depotService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {"/search"})
    public ResponseEntity<ResponseData> search(@RequestBody DepotSearch search) {
        if (!search.isPaged()) {
            List<DepotModel> depotModels = depotService.find(search);
            return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, depotModels);
        }
        Page<DepotModel> depotModels = depotService.search(search);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, depotModels);
    }

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> create(@RequestBody DepotModel depotModel) {
        depotModel = depotService.create(depotModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, depotModel);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> detail(@PathVariable Long id) throws CustomException {
        DepotModel depotModel = depotService.findOne(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, depotModel);
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> update(@RequestBody DepotModel depotModel) throws CustomException {
        depotModel = depotService.update(depotModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, depotModel);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> delete(@PathVariable Long id) throws CustomException {
        int deletedNumber = depotService.delete(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, deletedNumber);
    }
    
}
