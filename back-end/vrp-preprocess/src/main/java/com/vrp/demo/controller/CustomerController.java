package com.vrp.demo.controller;

import com.vrp.demo.entity.tenant.Customer;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.CustomerModel;
import com.vrp.demo.models.CustomerModelSignUp;
import com.vrp.demo.models.UserSessionModel;
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

//    @PostMapping(value = {"/sign-up-by-gmail"})
//    public ResponseEntity<ResponseData> signupByGmail(@RequestBody CustomerModelSignUp customerModelSignUp) throws CustomException {
//        customerModelSignUp = customerService.signupByGmail(customerModelSignUp);
//        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, customerModelSignUp);
//    }

//    @PostMapping(value = {"/sign-in"})
//    public ResponseEntity<ResponseData> signIn(@RequestBody CustomerModelSignUp customerModelSignUp) {
//        UserSessionModel userSessionModel = customerService.signin(customerModelSignUp);
//        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, userSessionModel == null ? Code.LOGIN_FAIL : Code.LOGIN_SUCCESS, userSessionModel );
//    }
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

    @GetMapping(value = {""})
    public ResponseEntity<ResponseData> detail(@RequestParam Long id) throws CustomException {
        CustomerModel customerModel = customerService.findOne(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, customerModel);
    }
    @GetMapping(value = {"/getCustomer"})
    public ResponseEntity<ResponseData> getCustomerByUserId(@RequestParam Long userId) throws CustomException {
        Customer customer = customerService.getCustomerByUserId(userId);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, customer);
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

//    @PostMapping(value = {"/create-customer-data"})
//    public ResponseEntity<ResponseData> createCustomerData() {
//        customerService.createCustomerData();
//        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, "OK!!");
//    }

}
