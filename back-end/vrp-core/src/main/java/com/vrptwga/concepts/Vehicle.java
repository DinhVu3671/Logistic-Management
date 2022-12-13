package com.vrptwga.concepts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Vehicle {

    private int id;
    private String name;
    private double capacity;
    private double maxLoadWeight;
    private double maxCapacity;
    private double velocity;
    private double fuelCost;
    private double fixedCost;
    private double loadingCost;
    private double area;
    private List<Request> servedAbleRequests;
    private int servedAbleRequestsNum;

    public List<Request> getServedAbleRequests() {
        return servedAbleRequests;
    }

    public int getServedAbleRequestsNum() {
        return this.servedAbleRequestsNum;
    }

    public void setServedAbleRequests(List<Request> servedAbleRequests) {
        this.servedAbleRequests = servedAbleRequests;
    }

    public void setServedAbleRequestsNum(int servedAbleRequestsNum) {
        this.servedAbleRequestsNum = servedAbleRequestsNum;
    }

    public double getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(double fixedCost) {
        this.fixedCost = fixedCost;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    public void setMaxLoadWeight(double maxLoadWeight) {
        this.maxLoadWeight = maxLoadWeight;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public double getLoadingCost() {
        return loadingCost;
    }

    public void setLoadingCost(double loadingCost) {
        this.loadingCost = loadingCost;
    }

    // chon ra xe tot nhat: Thuc hien duoc nhieu request nhat co the -> capacity tot nhat co the
    public static Vehicle getBestFitVehicle(List<Vehicle> vehicles, List<Request> serveRequests) {
        if (vehicles.isEmpty())
            return null;
        List<Vehicle> vehicleInputs = new ArrayList<>();
        for (Vehicle vehicleInput : vehicles) {
            vehicleInputs.add(Vehicle.cloneVehicleTemp(serveRequests, vehicleInput));
        }
        Comparator<Vehicle> compareVehicle = Comparator
                .comparing(Vehicle::getServedAbleRequestsNum, Comparator.reverseOrder())
                .thenComparing(Vehicle::getCapacity, Comparator.reverseOrder());
        vehicleInputs = vehicleInputs.stream()
                .sorted(compareVehicle)
                .collect(Collectors.toList());
        return Vehicle.getById(vehicleInputs.get(0).getId(), vehicles);
    }

    public static List<Vehicle> sortAscByMaxCapacity(List<Vehicle> vehicles) {
        if (vehicles.isEmpty())
            return null;
        Comparator<Vehicle> compareVehicle = Comparator
                .comparing(Vehicle::getServedAbleRequestsNum, Comparator.reverseOrder())
                .thenComparing(Vehicle::getCapacity, Comparator.reverseOrder());
        vehicles = vehicles.stream()
                .sorted(compareVehicle)
                .collect(Collectors.toList());
        return vehicles;
    }

    public static Vehicle getById(int id, List<Vehicle> vehicleInputs) {
        for (Vehicle vehicle : vehicleInputs) {
            if (vehicle.getId() == id)
                return vehicle;
        }
        return null;
    }

    public static Vehicle getVehicleByCapacity(double capacity, List<Vehicle> vehicleInputs) {
        for (Vehicle vehicleInput : vehicleInputs) {
            if (vehicleInput.getCapacity() == capacity)
                return vehicleInput;
        }
        return null;
    }

    public static Vehicle getVehicleById(int id, List<Vehicle> vehicleInputs) {
        for (Vehicle vehicleInput : vehicleInputs) {
            if (vehicleInput.getId() == id)
                return vehicleInput;
        }
        return null;
    }

    // request co the phuc vu? neu nhu cai xe do co the thuc hien duoc request => khong phai excludeVehicles
    public List<Request> getServedAbleRequests(List<Request> requests) {
        List<Request> servedAbleRequests = new ArrayList<>();
        for (Request request : requests) {
            if (!request.getExcludeVehicles().contains(this))
                servedAbleRequests.add(request);
        }
        return servedAbleRequests;
    }

    public static Vehicle cloneVehicleTemp(List<Request> requests, Vehicle vehicle) {
        Vehicle vehicleInput = new Vehicle();
        vehicleInput.setId(vehicle.getId());
        vehicleInput.setMaxCapacity(vehicle.getMaxCapacity());
        vehicleInput.setMaxLoadWeight(vehicle.getMaxLoadWeight());
        vehicleInput.setServedAbleRequestsNum(vehicle.getServedAbleRequests(requests).size());
        return vehicleInput;
    }
}
