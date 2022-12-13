package com.vrp.demo.controller;

import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.search.OrderSearch;
import com.vrp.demo.service.OrderService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {"/search"})
    public ResponseEntity<ResponseData> search(@RequestBody OrderSearch search) {
        if (!search.isPaged()) {
            List<OrderModel> orderModels = orderService.find(search);
            return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, orderModels);
        }
        Page<OrderModel> orderModels = orderService.search(search);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, orderModels);
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> getDetail(@PathVariable Long id) {
        OrderModel orderModel = orderService.findOne(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, orderModel);
    }

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> create(@RequestBody OrderModel orderModel) throws CustomException {
        orderModel = orderService.create(orderModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, orderModel);
    }


    @PutMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> update(@RequestBody OrderModel orderModel) throws CustomException {
        orderModel = orderService.update(orderModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, orderModel);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> delete(@PathVariable Long id) throws CustomException {
        int deletedNumber = orderService.delete(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, deletedNumber);
    }

    @PostMapping(value = {"/create-order-data"})
    public ResponseEntity<ResponseData> createOrderData() throws CustomException {
        orderService.createOrderData();
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, "OK!");
    }

}
