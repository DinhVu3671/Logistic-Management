package com.vrp.demo.models.solution;

import com.vrp.demo.configuration.mongodb.CascadeSave;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.utils.CommonUtils;
import com.vrptwga.representation.Individual;
import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.concepts.Request;
import com.vrptwga.concepts.Vehicle;
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
@NoArgsConstructor
@AllArgsConstructor
//@Document("solution")
public class Solution {

//    @Id
    private String id;
//    @DBRef
//    @CascadeSave
    private List<Journey> journeys = new ArrayList<>();
    private Double totalCost = 0.0;
    private Double totalDistance = 0.0;
    private double totalAmount = 0;
    private double revenue = 0;
    private double efficiency = 0;
    private int numberVehicle;
    private Double totalTravelTime;
    private Integer totalNumberOrders;
    private Double totalFixedCost = 0.0;
    
    public Double calculateTotalCostAndDist() {
        this.totalCost = 0d;
        this.totalDistance = 0d;
        this.totalAmount = 0d;
        this.revenue = 0d;
        this.totalNumberOrders = 0;
        this.totalTravelTime = 0d;
        double variableCost = 0d;
        double fixedCost = 0d;
        for (Journey journey : this.journeys) {
            this.totalDistance += journey.getTotalDistance();
            this.totalAmount += journey.getTotalAmount();
            this.revenue += journey.getRevenue();
            this.totalNumberOrders += journey.getOrders().size();
            this.totalTravelTime += journey.getTotalTravelTime();
            fixedCost += journey.getFixedCost();
            variableCost += journey.getTotalCost();
        }
        this.totalFixedCost = fixedCost;
        this.efficiency = Math.round(revenue / totalAmount * 100 * 100.0) / 100.0;
        this.totalCost = Math.round((fixedCost + variableCost) * 100.0) / 100.0;
        this.totalFixedCost = Math.round(this.totalFixedCost * 100.0) / 100.0;
        this.totalDistance = Math.round(this.totalDistance * 100.0) / 100.0;
        this.totalAmount = Math.round(this.totalAmount * 100.0) / 100.0;
        this.revenue = Math.round(this.revenue * 100.0) / 100.0;
        this.totalTravelTime = Math.round(this.totalTravelTime * 100.0) / 100.0;
        this.numberVehicle = journeys.size();
        return this.totalCost;
    }

    public static Solution createSolutionWithJourney(Individual individual, ProblemAssumption problemAssumption) {
        Solution solution = new Solution();
        for (com.vrptwga.representation.phenotype.Journey journey : individual.getJourneys()) {
            Journey journeyInfo = Journey.createJourney(journey, problemAssumption);
            solution.getJourneys().add(journeyInfo);
        }
        solution.calculateTotalCostAndDist();
        solution.setNumberVehicle(solution.getJourneys().size());
        return solution;
    }

    public static Solution updateJourneys(SolutionDTO updateSolution, OptimizationScenario optimizationScenario) {
        Request request = CommonUtils.findRequest(optimizationScenario.getRequests(), updateSolution.getChangedOrder());
        com.vrptwga.representation.phenotype.Journey toJourney = Journey.createJourney(updateSolution.getToJourney(), optimizationScenario);
        toJourney = com.vrptwga.representation.phenotype.Journey.bestInsertRequestToJourney(toJourney, request, optimizationScenario);
        Journey newToJourney = Journey.createJourney(toJourney, updateSolution.getProblemAssumption());
        List<OrderModel> fromJourneyOrders = updateSolution.getFromJourney().getOrders();
        fromJourneyOrders.removeIf(orderModel -> orderModel.getId() == updateSolution.getChangedOrder().getId());
        List<Request> fromJourneyRequests = CommonUtils.getRequests(optimizationScenario.getRequests(), fromJourneyOrders);
        Vehicle usedVehicle = CommonUtils.getVehicle(optimizationScenario.getVehicles(), updateSolution.getFromJourney().getVehicle());
        com.vrptwga.representation.phenotype.Journey fromJourney = com.vrptwga.representation.phenotype.Journey.createOptimizeJourney(fromJourneyRequests, usedVehicle, optimizationScenario);
        Journey newFromJourney = Journey.createJourney(fromJourney, updateSolution.getProblemAssumption());
        Solution solution = updateSolution.getSolution();
        for (int i = 0; i < solution.getJourneys().size(); i++) {
            Journey journey = solution.getJourneys().get(i);
            if (journey.getVehicle().getId().compareTo(newToJourney.getVehicle().getId()) == 0)
                solution.getJourneys().set(i, newToJourney);
            if (journey.getVehicle().getId().compareTo(newFromJourney.getVehicle().getId()) == 0)
                solution.getJourneys().set(i, newFromJourney);
        }
        solution.calculateTotalCostAndDist();
        return solution;
    }

    public void deleteCorrelations() {
        for (Journey journey : journeys) {
            for (Route route : journey.getRoutes()) {
                route.getStartDepot().setCorrelations(null);
                route.getEndDepot().setCorrelations(null);
                for (OrderModel order : route.getOrders()) {
                    order.getCustomer().setCorrelations(null);
                }
                for (Arc arc : route.getArcs()) {
                    arc.getFromNode().setCorrelations(null);
                    arc.getToNode().setCorrelations(null);
                }
            }
        }
    }

    public void setJourneysId() {
        for (Journey journey : journeys) {
            journey.setObjectId();
        }
    }

    public void setObjectId() {
        String id = System.currentTimeMillis() + "";
        setId(getId() == null ? id : id + getId());
        setJourneysId();
    }

}
