package com.vrptwga.concepts;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Customer {

    private int id;
    private String code;
    private TimeWindow timeWindow = new TimeWindow();
    private HashMap<Integer, Double> distanceDepots = new HashMap<>();
    private HashMap<Integer, Double> distanceCustomers = new HashMap<>();
    private HashMap<Integer, Double> riskProbabilityDepots = new HashMap<>();
    private HashMap<Integer, Double> riskProbabilityCustomers = new HashMap<>();
    private HashMap<String, Correlation> correlations = new HashMap<>();
    private double penaltyCost;

    public double getPenaltyCost() {
        return penaltyCost;
    }

    public void setPenaltyCost(double penaltyCost) {
        this.penaltyCost = penaltyCost;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public HashMap<String, Correlation> getCorrelations() {
        return correlations;
    }

    public void setCorrelations(HashMap<String, Correlation> correlations) {
        this.correlations = correlations;
    }

    public HashMap<Integer, Double> getRiskProbabilityDepots() {
        return riskProbabilityDepots;
    }

    public double getRiskProbToDepot(int depotId) {
        return this.riskProbabilityDepots.get(depotId);
    }

    public double getRiskProbToCustomer(int customerId) {
        return riskProbabilityCustomers.get(customerId);
    }

    public void setRiskProbabilityDepots(HashMap<Integer, Double> riskProbabilityDepots) {
        this.riskProbabilityDepots = riskProbabilityDepots;
    }

    public HashMap<Integer, Double> getRiskProbabilityCustomers() {
        return riskProbabilityCustomers;
    }

    public void setRiskProbabilityCustomers(HashMap<Integer, Double> riskProbabilityCustomers) {
        this.riskProbabilityCustomers = riskProbabilityCustomers;
    }

    public void setDistanceToDepot(int depotId, double distance) {
        distanceDepots.put(depotId, distance);
    }

    public void setDistanceToCustomer(int customerId, double distance) {
        this.distanceCustomers.put(customerId, distance);
    }

    public double getDistanceToDepot(int depotId) {
        return this.distanceDepots.get(depotId);
    }

    public double getDistanceToCustomer(int customerId) {
//        System.err.println("Get from customerID:" + this.getId() + " distance to customerID: " + customerId);
        return distanceCustomers.get(customerId);
    }

    public int getDistanceToNode(String nodeCode) {
        //        System.err.println("Get from customerID:" + this.getId() + " distance to customerID: " + customerId);
        return correlations.get(nodeCode).getDistance();
    }

    public int getTimeToNode(String nodeCode) {
        //        System.err.println("Get from customerID:" + this.getId() + " distance to customerID: " + customerId);
        return correlations.get(nodeCode).getTime();
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

    public Customer() {
    }

    public HashMap<Integer, Double> getDistanceDepots() {
        return distanceDepots;
    }

    public void setDistanceDepots(HashMap<Integer, Double> distanceDepots) {
        HashMap<Integer, Double> distanceDepotsFilter = (HashMap<Integer, Double>) distanceDepots.entrySet().stream()
                .filter(map -> map.getValue() > 0).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        this.distanceDepots = distanceDepots;
    }

    public HashMap<Integer, Double> getDistanceCustomers() {
        return distanceCustomers;
    }

    public void setDistanceCustomers(HashMap<Integer, Double> distanceCustomers) {
        HashMap<Integer, Double> distanceCustomersFilter = (HashMap<Integer, Double>) distanceCustomers.entrySet().stream()
                .filter(map -> map.getValue() > 0).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        this.distanceCustomers = distanceCustomersFilter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
