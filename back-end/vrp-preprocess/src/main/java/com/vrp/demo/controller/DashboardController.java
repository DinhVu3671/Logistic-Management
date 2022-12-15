package com.vrp.demo.controller;

import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @Autowired
    OrderService orderService;

    @GetMapping(value = {"/sales"})
    public ResponseEntity<ResponseData> getSales(@PathVariable String year) {
        orderService.searchByYear(year);
    }
}
