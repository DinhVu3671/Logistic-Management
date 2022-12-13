package com.vrp.demo.controller;

import com.vrp.demo.entity.tenant.mongo.JourneyDriver;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.search.JourneyDriverSearch;
import com.vrp.demo.service.imp.JourneyDriverService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/journey-driver")
public class JourneyDriverController {

    @Autowired
    private JourneyDriverService journeyDriverService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @PostMapping("/tracking")
    public ResponseEntity<ResponseData> tracking(@RequestBody JourneyDriverSearch search) {
        List<JourneyDriver> journeyDrivers = journeyDriverService.find(search);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, journeyDrivers);
    }

}
