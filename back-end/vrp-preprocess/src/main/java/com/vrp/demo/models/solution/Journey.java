package com.vrp.demo.models.solution;

import com.vrp.demo.configuration.mongodb.CascadeSave;
import com.vrp.demo.models.*;
import com.vrp.demo.utils.CommonUtils;
import com.vrptwga.concepts.OptimizationScenario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Document("journey")
public class Journey {

//    @Id
    private String id;
//    @DBRef
//    @CascadeSave
    private List<Route> routes = new ArrayList<>();
    private Double totalCost;
    private Integer totalDistance;
    private Integer totalTime;
    private Integer totalTravelTime;
    private double totalAmount = 0;
    private double revenue = 0;
//    @DBRef
//    @CascadeSave
    private VehicleModel vehicle;
    private Double totalPenaltyCost;
    private Double fixedCost;

    public static Journey createJourney(com.vrptwga.representation.phenotype.Journey journey, ProblemAssumption problemAssumption) {
        Journey journeyInfo = new Journey();
        double totalAmount = 0;
        double totalPenaltyCost = 0;
        journeyInfo.setVehicle(CommonUtils.getVehicleById(problemAssumption.getVehicles(), journey.getUsedVehicle().getId()));
        for (com.vrptwga.representation.phenotype.Route route : journey.getRoutes()) {
            Route routeInfo = Route.createRoute(route, problemAssumption, journey.getUsedVehicle());
            journeyInfo.getRoutes().add(routeInfo);
            totalAmount += routeInfo.getTotalAmount();
            totalPenaltyCost += routeInfo.getPenaltyCost();
        }
        journeyInfo.setTotalTravelTime(journey.getTotalTravelTime());
        journeyInfo.setTotalTime(journey.getTotalTime());
        journeyInfo.setTotalDistance(journey.getTotalDistance());
        journeyInfo.setTotalCost((Math.round(journey.getTotalCost() * 10.0) / 10.0));
        journeyInfo.setTotalAmount((Math.round(totalAmount * 10.0) / 10.0));
        journeyInfo.setRevenue(((Math.round(totalAmount * 10.0) / 10.0) - journeyInfo.getTotalCost()));
        journeyInfo.setTotalPenaltyCost((Math.round(totalPenaltyCost * 10.0) / 10.0));
        journeyInfo.setFixedCost((Math.round(journeyInfo.getVehicle().getFixedCost() * 10.0) / 10.0));
        return journeyInfo;
    }

    public static com.vrptwga.representation.phenotype.Journey createJourney(Journey journeyInfo, OptimizationScenario optimizationScenario) {
        com.vrptwga.representation.phenotype.Journey journey = new com.vrptwga.representation.phenotype.Journey();
        journey.setUsedVehicle(CommonUtils.getVehicle(optimizationScenario.getVehicles(), journeyInfo.getVehicle()));
        for (Route routeInfo : journeyInfo.getRoutes()) {
            com.vrptwga.representation.phenotype.Route route = Route.createRoute(routeInfo, optimizationScenario, journey.getUsedVehicle());
            journey.getRoutes().add(route);
        }
        return journey;
    }

    public List<OrderModel> getOrders() {
        List<OrderModel> orderModels = new ArrayList<>();
        for (Route route : routes) {
            orderModels.addAll(route.getOrders());
        }
        return orderModels;
    }

    public void setRoutesId() {
        for (Route route : routes) {
            route.setObjectId();
        }
    }

    public void setObjectId() {
        String id = System.currentTimeMillis() + "";
        setId(getId() == null ? id : id + getId());
        setRoutesId();
    }
}
