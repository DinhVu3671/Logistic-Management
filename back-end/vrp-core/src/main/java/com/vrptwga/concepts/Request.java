package com.vrptwga.concepts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Request {

    private int id;
    private int demand;
    private String code;
    private double weight;
    private double capacity;
    private double orderValue;
    private Customer customer = new Customer();
    private int timeService;
    private int timeLoading;
    private Depot depot;
    private TimeWindow specialTimeWindow = new TimeWindow();
    private List<Vehicle> excludeVehicles;
    private double antiStackingArea;
    private List<Request> excludingRequests;
    private List<Long> excludingRequestIds;

    public Request() {
    }

    public List<Long> getExcludingRequestIds() {
        return excludingRequestIds;
    }

    public void setExcludingRequestIds(List<Long> excludingRequestIds) {
        this.excludingRequestIds = excludingRequestIds;
    }

    public List<Request> getExcludingRequests() {
        return excludingRequests;
    }

    public void setExcludingRequests(List<Request> excludingRequests) {
        this.excludingRequests = excludingRequests;
    }

    public double getAntiStackingArea() {
        return antiStackingArea;
    }

    public void setAntiStackingArea(double antiStackingArea) {
        this.antiStackingArea = antiStackingArea;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(double orderValue) {
        this.orderValue = orderValue;
    }

    public int getDemand() {
        return this.demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public void setTimeService(int timeService) {
        this.timeService = timeService;
    }

    public List<Vehicle> getExcludeVehicles() {
        return excludeVehicles;
    }

    public void setExcludeVehicles(List<Vehicle> excludeVehicles) {
        this.excludeVehicles = excludeVehicles;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeService() {
        return timeService;
    }

    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public int getTimeLoading() {
        return timeLoading;
    }

    public void setTimeLoading(int timeLoading) {
        this.timeLoading = timeLoading;
    }

    public double getDistanceToDepot() {
        return this.customer.getDistanceToDepot(this.depot.getId());
    }

    public int getDistanceToCustomer(Request request) {
        if (this.getCustomerId() == request.getCustomerId())
            return 0;
        System.err.println("Get distance from customerID:" + this.getCustomer().getId() + " to customerID: " + request.getCustomer().getId());
        return this.customer.getDistanceToNode(request.getCustomer().getCode());
    }

    public int getTimeToCustomer(Request request) {
        if (request.getCustomerId() == 3 && this.getCustomerId() == 3)
            System.err.println("Get time from customerID:" + this.getCustomer().getId() + " to customerID: " + request.getCustomer().getId());
        if (this.getCustomerId() == request.getCustomerId())
            return 0;
        System.err.println("Get time from customerID:" + this.getCustomer().getId() + " to customerID: " + request.getCustomer().getId());
        return this.customer.getTimeToNode(request.getCustomer().getCode());
    }

    public int getStartTime() {
        if (this.specialTimeWindow != null && this.specialTimeWindow.getStartTime() > 0)
            return this.specialTimeWindow.getStartTime();
        return this.customer.getStartTime();
    }

    public int getEndTime() {
        if (this.specialTimeWindow != null && this.specialTimeWindow.getEndTime() > 0)
            return this.specialTimeWindow.getEndTime();
        return this.customer.getEndTime();
    }

    public int getCustomerId() {
        return this.customer.getId();
    }

    public static int getTotalDemand(List<Request> requests) {
        int totalDemand = 0;
        for (Request request : requests) {
            totalDemand += request.getDemand();
        }
        return totalDemand;
    }

    public static List<Request> removeUntilValid(List<Request> requestList, double maxCapacity) {
        List<Request> requests = new ArrayList<>();
        requests.addAll(requestList);
        while (getTotalDemand(requestList) > maxCapacity) {
            Request removeCustomer = getMinDemand(requests);
            requests.remove(removeCustomer);
        }
        return requests;
    }

    public static Request getMinDemand(List<Request> requestList) {
        List<Request> requests = new ArrayList<>();
        requests.addAll(requestList);
        requests.sort(Comparator.comparingInt(Request::getDemand));
        return requests.get(0);
    }

    public Depot fitEndDepot(int leaveAt) {
        int travelTimeToEndDepot = (int) this.getDistanceToDepot();
        int arriveEndDepotAt = leaveAt + travelTimeToEndDepot;
        if (arriveEndDepotAt <= depot.getEndTime())
            return depot;
        return null;
    }

    public boolean hasFitEndDepot(int leaveAt) {
        return fitEndDepot(leaveAt) != null;
    }


    public void setSpecialTimeWindow(int startTime, int endTime) {
        this.specialTimeWindow = new TimeWindow(startTime, endTime);
    }

    public static Request getHardestRequest(List<Request> requests) {
        Comparator<Request> requestComparator = Comparator.comparing(
                Request::getExcludeVehicles, (s1, s2) -> s2.size() - (s1.size()));
        requests = requests.stream().sorted(requestComparator).collect(Collectors.toList());
        return requests.get(0);
    }

    public static List<Request> getListRequestById(List<Long> ids, List<Request> requests) {
        List<Request> requestList = new ArrayList<>();
        for (Request request : requests) {
            for (Long id : ids) {
                if (request.getId() == id)
                    requestList.add(request);
            }
        }
        return requestList;
    }

    public static List<Request> getRequestsByDepot(Depot depot, List<Request> requests){
        List<Request> requestsOfDepot = new ArrayList<>();
        for (Request request : requests) {
            if (request.getDepot().getId() == depot.getId())
                requestsOfDepot.add(request);
        }
        return requestsOfDepot;
    }

}
