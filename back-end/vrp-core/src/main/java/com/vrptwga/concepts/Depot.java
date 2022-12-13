package com.vrptwga.concepts;

import com.vrptwga.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;

public class Depot {

    private int id;
    private String code;
    private com.vrptwga.concepts.TimeWindow timeWindow = new com.vrptwga.concepts.TimeWindow();
    private HashMap<Integer, Double> distanceCustomers = new HashMap<>();
    private HashMap<Integer, Double> riskProbabilityCustomers = new HashMap<>();
    private double unloadingCost;
    private HashMap<String, com.vrptwga.concepts.Correlation> correlations = new HashMap<>();

    public HashMap<String, com.vrptwga.concepts.Correlation> getCorrelations() {
        return correlations;
    }

    public void setCorrelations(HashMap<String, com.vrptwga.concepts.Correlation> correlations) {
        this.correlations = correlations;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getUnloadingCost() {
        return unloadingCost;
    }

    public void setUnloadingCost(double unloadingCost) {
        this.unloadingCost = unloadingCost;
    }

    public double getRiskProbToCustomer(int customerId) {
        return riskProbabilityCustomers.get(customerId);
    }

    public HashMap<Integer, Double> getRiskProbabilityCustomers() {
        return riskProbabilityCustomers;
    }

    public void setRiskProbabilityCustomers(HashMap<Integer, Double> riskProbabilityCustomers) {
        this.riskProbabilityCustomers = riskProbabilityCustomers;
    }

    public void setDistanceToCustomer(int customerId, double distance) {
        distanceCustomers.put(customerId, distance);
    }

    public double getDistanceToCustomer(int customerId) {
        return distanceCustomers.get(customerId);
    }

    public int getDistanceToCustomer(String customerCode) {
        return correlations.get(customerCode).getDistance();
    }

    public int getTimeToCustomer(String customerCode) {
        return correlations.get(customerCode).getTime();
    }

    public int getStartTime() {
        return this.timeWindow.getStartTime();
    }

    public void setStartTime(int startTime) {
        this.timeWindow.setStartTime(startTime);
    }

    public int getEndTime() {
        return this.timeWindow.getEndTime();
    }

    public void setEndTime(int endTime) {
        this.timeWindow.setEndTime(endTime);
    }

    public HashMap<Integer, Double> getDistanceCustomers() {
        return distanceCustomers;
    }

    public void setDistanceCustomers(HashMap<Integer, Double> distanceCustomers) {
        this.distanceCustomers = distanceCustomers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Depot getById(int id, List<Depot> depotInputs) {
        for (Depot depot : depotInputs) {
            if (depot.getId() == id)
                return depot;
        }
        return null;
    }


    // choose customer whose request is at least distances, return that request.
    public com.vrptwga.concepts.Request getNearestRequest(List<com.vrptwga.concepts.Request> requests) {
        HashMap<Integer, Double> distanceCustomers = new HashMap<>();
        for (com.vrptwga.concepts.Request request : requests) {
            if (this.distanceCustomers.containsKey(request.getCustomerId()))
                distanceCustomers.put(request.getCustomerId(), getDistanceToCustomer(request.getCustomerId()));
        }
        int customerId = CommonUtils.getKeyOfMinValue(distanceCustomers);
        for (com.vrptwga.concepts.Request request : requests) {
            if (request.getCustomerId() == customerId)
                return request;
        }
        return null;
    }

}
