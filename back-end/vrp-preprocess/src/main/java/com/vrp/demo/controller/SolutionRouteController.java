package com.vrp.demo.controller;

import com.vrp.demo.entity.tenant.mongo.DeliveryPlan;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.response.ResponseData;
import com.vrp.demo.models.search.DeliveryPlanSearch;
import com.vrp.demo.models.solution.ProblemAssumption;
import com.vrp.demo.models.solution.Solution;
import com.vrp.demo.models.solution.SolutionDTO;
import com.vrp.demo.service.imp.SolutionRouteService;
import com.vrp.demo.service.imp.SolutionService;
import com.vrp.demo.utils.ResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
public class SolutionRouteController {

    @Autowired
    private SolutionRouteService solutionRouteService;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private ResponsePreProcessor responsePreProcessor;

    @GetMapping("/hello")
    public String hello() {
        return "hello!";
    }

//    @PostMapping("/read-data-problem")
//    public ResponseData createProblemAssumptionFromData(HttpServletRequest request, @ModelAttribute DataProblemInput problemInput) throws IOException, InvalidFormatException {
//        ProblemAssumption problemAssumption = vrpProcessService.readDataToCreateProblemAssumption(request,problemInput);
//        return new ResponseData("SUCCESS", problemAssumption);
//    }

    @PostMapping("/get-solutions")
    public ResponseEntity<ResponseData> getRoutes(@RequestBody ProblemAssumption problemAssumption) {
        List<Solution> solutions = solutionRouteService.run(problemAssumption);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, solutions);
    }

    @PostMapping("/change-order-journey")
    public ResponseEntity<ResponseData> changeOrderToJourney(@RequestBody SolutionDTO updateSolution) {
        Solution solution = solutionRouteService.changeOrderToJourney(updateSolution);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, solution);
    }

    @PostMapping("/save-solution")
    public ResponseEntity<ResponseData> createSolution(@RequestHeader("sessionID") String sessionID, @RequestBody DeliveryPlan deliveryPlan) {
        DeliveryPlan solution = solutionService.create(deliveryPlan);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, solution);
    }

    @PostMapping("/search-solution")
    public ResponseEntity<ResponseData> searchSolution(@RequestBody DeliveryPlanSearch deliveryPlanSearch) {
        Page<DeliveryPlan> solutions = solutionService.search(deliveryPlanSearch);
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, solutions);
    }

    @PostMapping("/tracking")
    public ResponseEntity<ResponseData> trackingRoute(@RequestBody DeliveryPlanSearch deliveryPlanSearch) {
        DeliveryPlan trackingSolution = solutionService.tracking(deliveryPlanSearch.getId());
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, trackingSolution);
    }

    @PostMapping("/select-solution")
    public ResponseEntity<ResponseData> selectSolution(@RequestBody DeliveryPlan deliveryPlan) {
        DeliveryPlan solution = solutionService.select(deliveryPlan.getId());
        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, solution);
    }

//    @PostMapping("/init-problem")
//    public ResponseEntity<ResponseData> initProblem(@RequestHeader("X-TenantID") String tenantID, @RequestBody ProblemAssumption problemAssumption) {
//        SolutionDTO solutionDTO = routeService.initProblem(problemAssumption);
//        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, solutionDTO);
//    }

//    @PostMapping("/init")
//    public ResponseEntity<ResponseData> init(@RequestHeader("X-TenantID") String tenantID, @RequestBody SolutionDTO solutionDTO) {
//        List<Solution> solutions = routeService.runAlgorithm(solutionDTO);
//        return responsePreProcessor.buildResponseEntity(HttpStatus.OK, Code.SUCCESS, solutions);
//    }

}
