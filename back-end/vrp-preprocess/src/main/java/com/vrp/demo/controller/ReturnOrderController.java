package com.vrp.demo.controller;

import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.ReturnOrderModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.search.OrderSearch;
import com.vrp.demo.service.ReturnOrderService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/{orderId}/return-orders")
public class ReturnOrderController {

    @Autowired
    private ReturnOrderService returnOrderService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {""})
    public ResponseEntity<ResponseData> create(@RequestBody ReturnOrderModel returnOrderModel) throws CustomException {
        returnOrderModel = returnOrderService.create(returnOrderModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, returnOrderModel);
    }

    @PostMapping(value = {"/get-by-order"})
    public ResponseEntity<ResponseData> getByOrder(@RequestBody OrderSearch search) throws CustomException {
        ReturnOrderModel returnOrderModel = returnOrderService.find(search);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, returnOrderModel);
    }

    @PutMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> update(@RequestBody ReturnOrderModel returnOrderModel) throws CustomException {
        returnOrderModel = returnOrderService.update(returnOrderModel);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, returnOrderModel);
    }

    @DeleteMapping(value = {"/{id}"})
    public ResponseEntity<ResponseData> delete(@PathVariable Long id) throws CustomException {
        int deletedNumber = returnOrderService.delete(id);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, deletedNumber);
    }

}
