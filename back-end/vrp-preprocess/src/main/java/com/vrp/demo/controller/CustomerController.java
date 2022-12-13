package com.vrp.demo.controller;

import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.CustomerModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.search.CustomerSearch;
import com.vrp.demo.service.CustomerService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {"/search"})
    public ResponseEntity<ResponseData> search(@RequestBody CustomerSearch search) {
        if (!search.isPaged()) {
            List<CustomerModel> customerModels = customerService.find(search);
            return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, customerModels);
        }
        Page<CustomerModel> customerModels = customerService.search(search);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, customerModels);
    }

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> create(@RequestBody CustomerModel customerModel) {
        customerModel = customerService.create(customerModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, customerModel);
    }


    @PutMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> update(@RequestBody CustomerModel customerModel) throws CustomException {
        customerModel = customerService.update(customerModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, customerModel);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> detail(@PathVariable Long id) throws CustomException {
        CustomerModel customerModel = customerService.findOne(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, customerModel);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> delete(@PathVariable Long id) throws CustomException {
        int deletedNumber = customerService.delete(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, deletedNumber);
    }

    @PostMapping(value = {"/get-clustering-customers"})
    public ResponseEntity<ResponseData> getClusteringCustomers() {
        List<CustomerModel> customerModels = customerService.getClusteringCustomers();
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, customerModels);
    }

    @PostMapping(value = {"/create-customer-data"})
    public ResponseEntity<ResponseData> createCustomerData() {
        customerService.createCustomerData();
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, "OK!!");
    }

}
