package com.vrptwga.concepts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptimizationScenario {
    List<Request> requests = new ArrayList<Request>();
    List<List<Double>> distMatrix = new ArrayList<List<Double>>();
    List<Customer> customers = new ArrayList<>();
    List<Depot> depots = new ArrayList<>();
    List<Vehicle> vehicles = new ArrayList<>();

    private boolean isExcludeProduct = true;
    private boolean isLimitedTime = false;
    private int maxTravelTime;
    private boolean isLimitedDistance = false;
    private int maxDistance;
    private boolean isAllowedViolateTW = false;

    //ending criteria
    int time = 60000;

    //constrain information
    int maxVehicleNumber;

    //parameters used in GA
    int popSize = 250;
    int eliteSize = 10;
    double eliteRate = 13d;
    
    int maxGen = 200;
    int improve = 100;


    float probCrossover = 0.96f;
    float probMutation = 0.16f;

    int loadingTimePerKg = 2; // 2s cho 1 kg
    int unloadingTimePerKg = 6; // 6s cho 1 kg

    int tourK = 20;

    int tournamentSize = 20;
    int selectionSize = 40;

    int exponentialFactor = 3;

    public int getExponentialFactor() {
        return exponentialFactor;
    }

    public void setExponentialFactor(int exponentialFactor) {
        this.exponentialFactor = exponentialFactor;
    }

    public double getEliteRate() {
        return eliteRate;
    }

    public void setEliteRate(double eliteRate) {
        this.eliteRate = eliteRate;
    }

    public boolean isExcludeProduct() {
        return isExcludeProduct;
    }

    public void setExcludeProduct(boolean excludeProduct) {
        isExcludeProduct = excludeProduct;
    }

    public boolean isLimitedTime() {
        return isLimitedTime;
    }

    public void setLimitedTime(boolean limitedTime) {
        isLimitedTime = limitedTime;
    }

    public int getMaxTravelTime() {
        return maxTravelTime;
    }

    public void setMaxTravelTime(int maxTravelTime) {
        this.maxTravelTime = maxTravelTime;
    }

    public boolean isLimitedDistance() {
        return isLimitedDistance;
    }

    public void setLimitedDistance(boolean limitedDistance) {
        isLimitedDistance = limitedDistance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setCustomer(List<Request> orders) {
        this.requests = orders;
    }

    public List<List<Double>> getDistMatrix() {
        return distMatrix;
    }

    public void setDistMatrix(List<List<Double>> distMatrix) {
        this.distMatrix = distMatrix;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getImprove() {
        return improve;
    }

    public void setImprove(int improve) {
        this.improve = improve;
    }

    public int getPopSize() {
        return popSize;
    }

    public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    public int getMaxGen() {
        return maxGen;
    }

    public void setMaxGen(int maxGen) {
        this.maxGen = maxGen;
    }

    public int getTourK() {
        return tourK;
    }

    public void setTourK(int tourK) {
        this.tourK = tourK;
    }

    public float getProbCrossover() {
        return probCrossover;
    }

    public void setProbCrossover(float probCrossover) {
        this.probCrossover = probCrossover;
    }

    public float getProbMutation() {
        return probMutation;
    }

    public void setProbMutation(float probMutation) {
        this.probMutation = probMutation;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Depot> getDepots() {
        return depots;
    }

    public void setDepots(List<Depot> depots) {
        this.depots = depots;
    }

    public int getLoadingTimePerKg() {
        return loadingTimePerKg;
    }

    public void setLoadingTimePerKg(int loadingTimePerKg) {
        this.loadingTimePerKg = loadingTimePerKg;
    }

    public int getUnloadingTimePerKg() {
        return unloadingTimePerKg;
    }

    public void setUnloadingTimePerKg(int unloadingTimePerKg) {
        this.unloadingTimePerKg = unloadingTimePerKg;
    }

    public int getEliteSize() {
        return eliteSize;
    }

    public void setEliteSize(int eliteSize) {
        this.eliteSize = eliteSize;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    public int getSelectionSize() {
        return selectionSize;
    }

    public void setSelectionSize(int selectionSize) {
        this.selectionSize = selectionSize;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public boolean isAllowedViolateTW() {
        return isAllowedViolateTW;
    }

    public void setAllowedViolateTW(boolean allowedViolateTW) {
        isAllowedViolateTW = allowedViolateTW;
    }

//    public static List<OptimizationScenario> divideByDepot(OptimizationScenario optimizationScenario) {
//        List<OptimizationScenario> optimizationScenarios = new ArrayList<>();
//        List<Depot> depotInputs = optimizationScenario.getDepots();
//        for (Depot depotInput : depotInputs) {
//            OptimizationScenario optimizationScenarioTemp = new OptimizationScenario();
//            List<Vehicle> vehicles = new ArrayList<>();
//            List<Request> requests = new ArrayList<>();
//            List<Customer> customers = new ArrayList<>();
//            for (Vehicle vehicle : optimizationScenario.getVehicles()) {
//                if (vehicle.getDepot().getId() == depotInput.getId())
//                    vehicles.add(vehicle);
//            }
//            optimizationScenarioTemp.setVehicles(vehicles);
//            for (Request request : optimizationScenario.getRequests()) {
//                if (request.getDepot().getId() == depotInput.getId()) {
//                    requests.add(request);
//                    customers.add(request.getCustomer());
//                }
//            }
//            if (!requests.isEmpty()) {
//                optimizationScenarioTemp.setRequests(requests);
//                optimizationScenarioTemp.setCustomers(customers);
//                optimizationScenarioTemp.setDepots(Arrays.asList(depotInput));
//                optimizationScenarios.add(optimizationScenarioTemp);
//            }
//        }
//        return optimizationScenarios;
//    }

}



