package com.vrp.demo.controller;

import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.service.DashboardService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping(value = {"/order-item-stat"})
    public ResponseEntity<ResponseData> orderItemStat(@RequestBody Map<String, String> dateRange) {
        String startDate = dateRange.get("start");
        String endDate = dateRange.get("end");

        Map<String, Object> orderItemStats = dashboardService.orderItemStat(startDate, endDate);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, orderItemStats);
    }
}
