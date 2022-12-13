package com.vrptwga.representation.phenotype;

import com.vrptwga.concepts.*;
import com.vrptwga.utils.CommonUtils;

import java.util.*;
import java.util.stream.Collectors;


public class Route {

    private static final Random randomGenerator = new Random();
    private int currentVolume;
    private List<Customer> servedCustomers = new ArrayList<>();
    private List<TimeWindow> timeline = new ArrayList<>();
    private List<Request> requests = new ArrayList<>();
    private List<Double> riskOfCustomers = new ArrayList<>();
    private Depot startDepot;
    private Depot endDepot;
    private int totalTravelTime;
    private int totalDistance;
    private double totalCost;
    private double maxCapacity;
    private Integer arriveStartDepotAt = null;
    private Vehicle usedVehicle;
    private double currentLoadWeight;
    private double currentCapacity;
    private double antiStackingArea;

    private boolean isLimitedTime = false;
    private double maxTravelTime;
    private boolean isLimitedDistance = false;
    private double maxDistance;
    private double penaltyCost;
    private double totalValue;

    private LinkedHashMap<String, Double> finedRequests = new LinkedHashMap<>();

    public LinkedHashMap<String, Double> getFinedRequests() {
        return finedRequests;
    }

    public void setFinedRequests(LinkedHashMap<String, Double> finedRequests) {
        this.finedRequests = finedRequests;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public boolean isLimitedTime() {
        return isLimitedTime;
    }

    public void setLimitedTime(boolean limitedTime) {
        isLimitedTime = limitedTime;
    }

    public double getMaxTravelTime() {
        return maxTravelTime;
    }

    public void setMaxTravelTime(double maxTravelTime) {
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

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getAntiStackingArea() {
        return antiStackingArea;
    }

    public void setAntiStackingArea(double antiStackingArea) {
        this.antiStackingArea = antiStackingArea;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getCurrentLoadWeight() {
        return currentLoadWeight;
    }

    public void setCurrentLoadWeight(double currentLoadWeight) {
        this.currentLoadWeight = currentLoadWeight;
    }

    public double getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(double currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public Vehicle getUsedVehicle() {
        return usedVehicle;
    }

    public void setUsedVehicle(Vehicle usedVehicle) {
        this.usedVehicle = usedVehicle;
    }

    public double getPenaltyCost() {
        return penaltyCost;
    }

    public void setPenaltyCost(double penaltyCost) {
        this.penaltyCost = penaltyCost;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("startDepot: " + startDepot.getId() + " === ");
        for (Request request : getRequests()) {
            sb.append(request.getCustomerId());
            sb.append(" ");
        }
        sb.append(" === " + "endDepot: " + (endDepot == null ? "null" : endDepot.getId()));
        return sb.toString();
    }

    public int arriveEndDepotAt() {
        TimeWindow timeWindowAtEndDepot = timeline.get(timeline.size() - 1);
        return timeWindowAtEndDepot.getStartTime();
    }

    public Integer getArriveStartDepotAt() {
        return arriveStartDepotAt;
    }

    public void setArriveStartDepotAt(Integer arriveStartDepotAt) {
        this.arriveStartDepotAt = arriveStartDepotAt;
    }

    public int getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(int currentVolume) {
        this.currentVolume = currentVolume;
    }

    public List<Integer> getServedCustomerIds() {
        List<Integer> customerIds = new ArrayList<>();
        for (Request request : requests) {
            customerIds.add(request.getCustomerId());
        }
        return customerIds;
    }

    public List<Customer> getServedCustomers() {
        return servedCustomers;
    }

    public void setServedCustomers(List<Customer> servedCustomers) {
        this.servedCustomers = servedCustomers;
    }

    public List<TimeWindow> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimeWindow> timeline) {
        this.timeline = timeline;
    }

    public Depot getStartDepot() {
        return startDepot;
    }

    public void setStartDepot(Depot startDepot) {
        this.startDepot = startDepot;
    }

    public Depot getEndDepot() {
        return endDepot;
    }

    public void setEndDepot(Depot endDepot) {
        this.endDepot = endDepot;
    }

    public List<String> getNodes() {
        List<String> nodes = new ArrayList<>();
        nodes.add(startDepot.getCode());
        nodes.addAll(requests.stream().map(request -> request.getCustomer().getCode()).collect(Collectors.toList()));
        nodes.add(endDepot.getCode());
        return nodes;
    }

    public List<Double> getRiskOfCustomers() {
        return riskOfCustomers;
    }

    public void setRiskOfCustomers(List<Double> riskOfCustomers) {
        this.riskOfCustomers = riskOfCustomers;
    }

    public List<TimeWindow> updateTimeLine(int loadingTimePerTon, int unloadingTimePerTon) {
        int arriveDepotAt = arriveStartDepotAt == null ? startDepot.getStartTime() : arriveStartDepotAt;
        int timeLoading = requests.stream().map(request -> request.getTimeLoading()).mapToInt(Integer::intValue).sum();
        int leaveStartDepotAt = arriveDepotAt + timeLoading;
//        int leaveStartDepotAt = arriveDepotAt + currentVolume * loadingTimePerTon;
        TimeWindow timeWindowAtStartDepot = TimeWindow.createTimeWindowAtStartDepot(arriveDepotAt, leaveStartDepotAt);
        List<TimeWindow> timeWindowsAtCustomer = new ArrayList<>();
        Request firstRequest = requests.get(0);
        int startDepotToFirstCustomer = startDepot.getTimeToCustomer(firstRequest.getCustomer().getCode());
        int arriveFirstCustomerAt = leaveStartDepotAt + startDepotToFirstCustomer;
//        TimeWindow timeWindowAtFirst = TimeWindow.createTimeWindowAtCustomer(arriveFirstCustomerAt, unloadingTimePerTon * firstRequest.getDemand());
        TimeWindow timeWindowAtFirst = TimeWindow.createTimeWindowAtCustomer(arriveFirstCustomerAt, firstRequest.getTimeService());
        timeWindowsAtCustomer.add(timeWindowAtFirst);
        if (requests.size() > 1) {
            for (int i = 1; i < requests.size(); i++) {
                Request previousRequest = requests.get(i - 1);
                Request request = requests.get(i);
                int travelTimeBetween = previousRequest.getTimeToCustomer(request);
                int arriveCustomerAt = timeWindowsAtCustomer.get(i - 1).getEndTime() + travelTimeBetween;
//                TimeWindow timeWindowAtCustomer = TimeWindow.createTimeWindowAtCustomer(arriveCustomerAt, unloadingTimePerTon * request.getDemand());
                TimeWindow timeWindowAtCustomer = TimeWindow.createTimeWindowAtCustomer(arriveCustomerAt, request.getTimeService());
                timeWindowsAtCustomer.add(timeWindowAtCustomer);
            }
        }
        timeline.clear();
        timeline.add(timeWindowAtStartDepot);
        timeline.addAll(timeWindowsAtCustomer);
        if (endDepot != null) {
            TimeWindow timeWindowAtLastCustomer = timeWindowsAtCustomer.get(timeWindowsAtCustomer.size() - 1);
            Customer lastCustomer = requests.get(requests.size() - 1).getCustomer();
            int travelTimeToEndDepot = lastCustomer.getTimeToNode(endDepot.getCode());
            int arriveEndDepotAt = timeWindowAtLastCustomer.getEndTime() + travelTimeToEndDepot;
            TimeWindow timeWindowAtEndDepot = TimeWindow.createTimeWindowAtEndDepot(arriveEndDepotAt);
            timeline.add(timeWindowAtEndDepot);
        }
        return timeline;
    }

    public void updateCurrentVolume() {
        currentLoadWeight = 0;
        currentCapacity = 0;
        antiStackingArea = 0;
        currentVolume = 0;
        for (Request request : requests) {
            currentLoadWeight += request.getWeight();
            currentCapacity += request.getCapacity();
            antiStackingArea += request.getAntiStackingArea();
        }
        currentVolume = (int) currentLoadWeight;
        return;
    }

    public List<Double> updateRiskOfCustomers() {
        List<Double> riskOfCustomers = new ArrayList<>();
        Request firstRequest = requests.get(0);
        double startDepotToFirstCustomer = startDepot.getRiskProbToCustomer(firstRequest.getCustomerId());
        double riskOfFirstCustomer = 0 + startDepotToFirstCustomer;
        riskOfCustomers.add(riskOfFirstCustomer);
        if (requests.size() > 1) {
            for (int i = 1; i < requests.size(); i++) {
                Customer previousCustomer = requests.get(i - 1).getCustomer();
                Customer currentCustomer = requests.get(i).getCustomer();
                double riskOnRoad = previousCustomer.getRiskProbToCustomer(currentCustomer.getId());
                double riskOfPreviousCustomer = riskOfCustomers.get(i - 1);
                double riskOfCustomer = riskOfPreviousCustomer + riskOnRoad * (1 - riskOfPreviousCustomer);
                riskOfCustomers.add(riskOfCustomer);
            }
        }
        this.riskOfCustomers.clear();
        this.riskOfCustomers.addAll(riskOfCustomers);
        return this.riskOfCustomers;
    }

    public void updateFinedRequests() {
        LinkedHashMap<String, Double> finedRequests = new LinkedHashMap<>();
        for (int i = 0; i < requests.size(); i++) {
            TimeWindow timeWindowAtCustomer = timeline.get(i + 1);
            Request request = requests.get(i);
            if (timeWindowAtCustomer.getStartTime() < request.getStartTime()) {
                int violatedTime = request.getStartTime() - timeWindowAtCustomer.getStartTime();
                finedRequests.put(requests.get(i).getCode(), violatedTime * request.getCustomer().getPenaltyCost());
            } else if (timeWindowAtCustomer.getEndTime() > request.getEndTime()) {
                int violatedTime = timeWindowAtCustomer.getEndTime() - request.getEndTime();
                finedRequests.put(requests.get(i).getCode(), violatedTime * request.getCustomer().getPenaltyCost());
            }
        }
        this.finedRequests = finedRequests;
    }

    public Route updateRoute(OptimizationScenario optimizationScenario) {
        updateCurrentVolume();
        updateTimeLine(optimizationScenario.getLoadingTimePerKg(), optimizationScenario.getUnloadingTimePerKg());
//        updateRiskOfCustomers();
        updateFinedRequests();
        return this;
    }

    public Route addRequest(Request request, OptimizationScenario optimizationScenario) {
        requests.add(request);
        servedCustomers.add(request.getCustomer());
        updateRoute(optimizationScenario);
        return this;
    }

    public boolean isViolateTimeWindowConstrain(OptimizationScenario optimizationScenario) {
        if (isViolateTimeWindowDepot()) {
            System.err.println("ViolateTimeWindowDepot!!!");
            return true;
        }
        if (optimizationScenario.isAllowedViolateTW()) {
            return checkViolateSoftTimeWindowCustomer();
        } else {
            if (isViolateHardTimeWindowCustomer()) {
                System.err.println("ViolateTimeWindowCustomer!!!");
                return true;
            }
        }
        return false;
    }

    public boolean isViolateTimeWindowDepot() {
        TimeWindow timeWindowAtEndDepot = timeline.get(timeline.size() - 1);
        return endDepot.getEndTime() < timeWindowAtEndDepot.getStartTime();
    }

    public boolean isViolateLoadingWeight() {
        return currentLoadWeight > this.usedVehicle.getMaxLoadWeight();
    }

    public boolean isViolateCapacity() {
        return currentCapacity > this.usedVehicle.getMaxCapacity();
    }

    public boolean isViolateAntiStackingArea() {
        return antiStackingArea > this.usedVehicle.getArea();
    }

    public boolean isExcludedByVehicle(Request request) {
        for (Vehicle excludeVehicle : request.getExcludeVehicles()) {
            if (excludeVehicle.getId() == usedVehicle.getId())
                return true;
        }
        return false;
    }

    public boolean isViolateHardTimeWindowCustomer() {
        for (int i = 0; i < requests.size(); i++) {
            TimeWindow timeWindowAtCustomer = timeline.get(i + 1);
//            CustomerInput customer = requests.get(i).getCustomer();
            Request request = requests.get(i);
            request.getEndTime();
            if (timeWindowAtCustomer.getStartTime() < request.getStartTime() || timeWindowAtCustomer.getEndTime() > request.getEndTime()) {
                System.err.println("timeline:" + this.timelineToString());
                System.err.println(" endTimeWindow at " + request.getCustomerId() + " is: " + request.getEndTime());
                System.err.println("ViolateTimeWindowCustomer!!! at: " + (i) + " IDCustomer: " + request.getCustomerId());
                return true;
            }
        }
        return false;
    }

    public boolean checkViolateSoftTimeWindowCustomer() {
        boolean isViolateSoftTimeWindowCustomer = false;
        boolean isViolateHardTimeWindowCustomer = false;
        LinkedHashMap<String, Double> finedRequests = new LinkedHashMap<>();
        for (int i = 0; i < requests.size(); i++) {
            TimeWindow timeWindowAtCustomer = timeline.get(i + 1);
            Request request = requests.get(i);
            Customer customer = request.getCustomer();
            if (timeWindowAtCustomer.getStartTime() < request.getStartTime()) {
                System.err.println("timeline:" + this.timelineToString());
                System.err.println("startTimeWindow at " + customer.getId() + " is: " + request.getStartTime());
                System.err.println("ViolateSoftTimeWindowCustomer!!! at: " + (i) + " IDCustomer: " + customer.getId());
                isViolateSoftTimeWindowCustomer = true;
                int violatedTime = request.getStartTime() - timeWindowAtCustomer.getStartTime();
                finedRequests.put(request.getCode(), violatedTime * customer.getPenaltyCost());
            } else if (timeWindowAtCustomer.getEndTime() > request.getEndTime()) {
                isViolateSoftTimeWindowCustomer = true;
                System.err.println("timeline:" + this.timelineToString());
                System.err.println(" endTimeWindow at " + customer.getId() + " is: " + request.getEndTime());
                System.err.println("ViolateSoftTimeWindowCustomer!!! at: " + (i) + " IDCustomer: " + customer.getId());
                int violatedTime = timeWindowAtCustomer.getEndTime() - request.getEndTime();
                finedRequests.put(request.getCode(), violatedTime * customer.getPenaltyCost());
            }
            if (timeWindowAtCustomer.getStartTime() < customer.getStartTime() || timeWindowAtCustomer.getEndTime() > customer.getEndTime()) {
                System.err.println("timeline:" + this.timelineToString());
                System.err.println(" endTimeWindow at " + request.getCustomerId() + " is: " + customer.getEndTime());
                System.err.println("ViolateTimeWindowCustomer!!! at: " + (i) + " IDCustomer: " + request.getCustomerId());
                isViolateHardTimeWindowCustomer = true;
            }
        }
        this.finedRequests = finedRequests;
        return isViolateHardTimeWindowCustomer;
    }

    public boolean isViolateLimitedTimeConstrain() {
        if (this.isLimitedTime) {
            this.calculateTotal();
            return this.totalTravelTime > this.maxTravelTime;
        }
        return false;
    }

    public boolean isViolateLimitedDistanceConstrain() {
        if (this.isLimitedDistance) {
            this.calculateTotal();
            return this.totalDistance > this.maxDistance;
        }
        return false;
    }

    public boolean isTotalCostExceedTotalValue() {
        this.calculateTotal();
        return this.totalCost > this.totalValue;
    }

    public boolean isViolateConstrain(OptimizationScenario optimizationScenario) {
        if (isViolateTimeWindowConstrain(optimizationScenario))
            return true;
        if (isViolateCapacity()) {
            System.err.println("Violate Capacity");
            return true;
        }
        if (isViolateLoadingWeight()) {
            System.err.println("Violate Loading Weight");
            return true;
        }
        if (isViolateAntiStackingArea()) {
            System.err.println("Violate Anti-Stacking Area");
            return true;
        }
        if (isViolateLimitedTimeConstrain()) {
            System.err.println("Violate Limited Time Constrain");
            return true;
        }
        if (isViolateLimitedDistanceConstrain()) {
            System.err.println("Violate Limited Distance Constrain");
            return true;
        }
        if (isTotalCostExceedTotalValue()) {
            System.err.println("Violate TotalCost Exceed Total Value");
            return true;
        }
        return false;
    }

    public boolean isExcludedByRequests(Request request) {
        for (Request servedRequest : requests) {
            if (servedRequest.getExcludingRequests().contains(request))
                return true;
        }
        return false;
    }

    public boolean isAddableRequest(Request request, OptimizationScenario optimizationScenario) {
        if (isExcludedByVehicle(request)) {
            System.err.println("Excluded By Vehicle");
            return false;
        }
        if (isExcludedByRequests(request)) {
            System.err.println("Excluded By Requests");
            return false;
        }
        TimeWindow timeWindowAtLastCustomer = timeline.get(requests.size());
        int travelTimeToCustomer = requests.get(requests.size() - 1).getTimeToCustomer(request);
        int arriveCustomerAt = timeWindowAtLastCustomer.getEndTime() + travelTimeToCustomer;
        if (arriveCustomerAt < request.getStartTime()) {
            System.out.println("arriveCustomerAt < getStartTime");
            return false;
        }
        int leaveCustomerAt = arriveCustomerAt + request.getTimeService();
        if (request.hasFitEndDepot(leaveCustomerAt) == false) {
            System.out.println("No end depot");
            return false;
        }
        Depot endDepot = request.fitEndDepot(leaveCustomerAt);
        Route testRoute = clone(this);
        testRoute = testRoute.addRequest(request, optimizationScenario);
        testRoute.setEndDepot(endDepot);
        testRoute.updateRoute(optimizationScenario);
        if (testRoute.isViolateConstrain(optimizationScenario))
            return false;
        return true;
    }

    public Route addFirstRequestAndStartDepot(Request firstRequest, OptimizationScenario optimizationScenario) {
        Depot startDepot = firstRequest.getDepot();
        this.getRequests().add(firstRequest);
        this.setCurrentVolume(firstRequest.getDemand());
        this.setStartDepot(startDepot);
        this.updateRoute(optimizationScenario);
        return this;
    }

    public Route addEndDepot(OptimizationScenario optimizationScenario) {
        Request lastRequest = this.requests.get(this.requests.size() - 1);
        int leaveCustomerAt = this.timeline.get(timeline.size() - 1).getEndTime();
        Depot endDepot = this.randomEndDepot(optimizationScenario);
        this.setEndDepot(endDepot);
        this.updateRoute(optimizationScenario);
        return this;
    }

    public static Route initRoute(Journey currentJourney, OptimizationScenario optimizationScenario) {
        Route route = new Route();
        route.setMaxCapacity(currentJourney.getUsedVehicle().getMaxCapacity());
        route.setUsedVehicle(currentJourney.getUsedVehicle());
        route.setLimitedTime(optimizationScenario.isLimitedTime());
        route.setLimitedDistance(optimizationScenario.isLimitedDistance());
        if (optimizationScenario.isLimitedTime()) {
            if (currentJourney.getRoutes().size() > 0) {
                int maxTravelTime = optimizationScenario.getMaxTravelTime() - currentJourney.getTotalTravelTime();
                route.setMaxTravelTime(maxTravelTime);
            } else
                route.setMaxTravelTime(optimizationScenario.getMaxTravelTime());
        }
        if (optimizationScenario.isLimitedDistance()) {
            if (currentJourney.getRoutes().size() > 0) {
                route.setMaxDistance(optimizationScenario.getMaxDistance() - currentJourney.getTotalDistance());
            } else
                route.setMaxDistance(optimizationScenario.getMaxDistance());
        }
        return route;
    }

    public static Route clone(Route currentRoute) {
        Route clone = new Route();
        clone.getTimeline().addAll(currentRoute.getTimeline());
        clone.getServedCustomers().addAll(currentRoute.getServedCustomers());
        clone.getRequests().addAll(currentRoute.getRequests());
        clone.setCurrentVolume(currentRoute.getCurrentVolume());
        clone.setStartDepot(currentRoute.getStartDepot());
        clone.setEndDepot(currentRoute.getEndDepot());
        clone.setMaxCapacity(currentRoute.getMaxCapacity());
        clone.setTotalTravelTime(currentRoute.getTotalTravelTime());
        clone.setTotalDistance(currentRoute.getTotalDistance());
        clone.setUsedVehicle(currentRoute.getUsedVehicle());
        clone.setCurrentLoadWeight(currentRoute.getCurrentLoadWeight());
        clone.setCurrentCapacity(currentRoute.getCurrentCapacity());
        clone.setArriveStartDepotAt(currentRoute.getArriveStartDepotAt());
        clone.setLimitedTime(currentRoute.isLimitedTime());
        clone.setMaxTravelTime(currentRoute.getMaxTravelTime());
        clone.setLimitedDistance(currentRoute.isLimitedDistance());
        clone.setMaxDistance(currentRoute.getMaxDistance());
        clone.setPenaltyCost(currentRoute.getPenaltyCost());
        clone.setTotalValue(currentRoute.getTotalValue());
        clone.getFinedRequests().putAll(currentRoute.getFinedRequests());
        return clone;
    }

    public static Route createNewRoute(OptimizationScenario optimizationScenario, List<Request> serveRequests, Journey journey) {
        Route route = Route.initRoute(journey, optimizationScenario);
//        route.setMaxCapacity(journey.getUsedVehicle().getMaxCapacity());
//        route.setUsedVehicle(journey.getUsedVehicle());

        List<Request> notServedRequests = new ArrayList<>();
        notServedRequests.addAll(serveRequests);
        if (notServedRequests == null)
            return null;
        int randomIndex = randomGenerator.nextInt(notServedRequests.size());
        System.err.println("=====randomIndex====: " + randomIndex);
        Request firstRequest = notServedRequests.get(randomIndex);
        System.err.println("FirstRequestID: " + firstRequest.getId());
        route.addFirstRequestAndStartDepot(firstRequest, optimizationScenario);
        notServedRequests = Request.getRequestsByDepot(route.getStartDepot(), notServedRequests);
        notServedRequests = CommonUtils.removeRequest(notServedRequests, firstRequest);
        for (Request notServedRequest : notServedRequests) {
            if (route.isAddableRequest(notServedRequest, optimizationScenario)) {
                System.err.println("Add RequestID: R" + notServedRequest.getId() + " to this route");
                route.addRequest(notServedRequest, optimizationScenario);
            } else
                System.err.println("Can not add RequestID: R" + notServedRequest.getId());
        }
        route.addEndDepot(optimizationScenario);
        route.calculateTotal();
        for (Request request : route.getRequests()) {
            if (request.getExcludeVehicles().contains(route.getUsedVehicle()))
                throw new NullPointerException();
        }
        return route;
    }

    public static Route createNewRouteInJourney(Journey journey, List<Request> notServedRequests, OptimizationScenario optimizationScenario) {
        Route route = Route.initRoute(journey, optimizationScenario);
//        route.setMaxCapacity(journey.getMaxCapacity());
//        route.setUsedVehicle(journey.getUsedVehicle());

        Route lastRoute = journey.getLastRoute();
        Depot endDepot = lastRoute.getEndDepot();
        List<Request> validRequests = Journey.removeInValidRequest(journey, notServedRequests);
        validRequests = Request.getRequestsByDepot(endDepot, validRequests);
        TimeWindow timeWindowAtEndDepot = lastRoute.getTimeline().get(lastRoute.getRequests().size() + 1);
        if (validRequests == null || validRequests.isEmpty())
            return null;
        Request firstRequest = endDepot.getNearestRequest(validRequests);
        System.err.println("FirstRequestID: " + firstRequest.getId());
        route.addFirstRequestAndStartDepot(firstRequest, optimizationScenario);
        route.setArriveStartDepotAt(timeWindowAtEndDepot.getStartTime());
        try {
            route.updateEndDepot(optimizationScenario);
        } catch (IllegalArgumentException e) {
            return null;
        }
        route.updateRoute(optimizationScenario);
        if (route.isViolateConstrain(optimizationScenario))
            return null;
        validRequests = CommonUtils.removeRequest(validRequests, firstRequest);
        for (Request notServedRequest : validRequests) {
            if (route.isAddableRequest(notServedRequest, optimizationScenario)) {
                System.err.println("Add RequestID: R" + notServedRequest.getId() + " to this route");
                route.addRequest(notServedRequest, optimizationScenario);
            } else
                System.err.println("Can not add RequestID: R" + notServedRequest.getId());
        }
        route.addEndDepot(optimizationScenario);
        route.calculateTotal();
        for (Request request : route.getRequests()) {
            if (request.getExcludeVehicles().contains(route.getUsedVehicle()))
                throw new NullPointerException();
        }
        return route;
    }

    public void calculateTotal() {
        totalTravelTime = startDepot.getTimeToCustomer(this.requests.get(0).getCustomer().getCode());
        totalDistance = startDepot.getDistanceToCustomer(this.requests.get(0).getCustomer().getCode());
        penaltyCost = 0;
        totalValue = 0;
        for (int i = 0; i < this.requests.size() - 1; i++) {
            Request request = this.requests.get(i);
            Request nextRequest = this.requests.get(i + 1);
            totalTravelTime += request.getTimeToCustomer(nextRequest);
            totalDistance += request.getDistanceToCustomer(nextRequest);
        }
        for (Double penaltyCostRequest : finedRequests.values()) {
            penaltyCost += penaltyCostRequest;
        }
        for (Request request : requests) {
            totalValue += request.getOrderValue();
        }
        Customer lastCustomer = this.requests.get(requests.size() - 1).getCustomer();
        totalTravelTime += lastCustomer.getTimeToNode(endDepot.getCode());
        totalDistance += lastCustomer.getDistanceToNode(endDepot.getCode());
        totalCost = totalDistance * usedVehicle.getFuelCost() / 1000 + penaltyCost;
        return;
    }

    public List<Customer> getRandomListCustomer() {
        Random random = new Random();
        int fromIndex = random.nextInt(this.servedCustomers.size());
        int toIndex = fromIndex + random.nextInt(this.servedCustomers.size() - fromIndex) + 1;
        List<Customer> randomCustomer = new ArrayList<>();
        for (int i = fromIndex; i < toIndex; i++) {
            randomCustomer.add(servedCustomers.get(i));
        }
        return randomCustomer;
    }

    public List<Request> getRandomListRequest() {
        Random random = new Random();
        int fromIndex = random.nextInt(this.requests.size());
        int toIndex = fromIndex + random.nextInt(this.requests.size() - fromIndex) + 1;
        List<Request> randomRequests = new ArrayList<>();
        for (int i = fromIndex; i < toIndex; i++) {
            randomRequests.add(requests.get(i));
        }
        return randomRequests;
    }

    public static Route createRouteByFixedRequest(List<Request> requestList, OptimizationScenario optimizationScenario, Journey journey) {
        double maxCapacity = journey.getMaxCapacity();
        List<Request> requests = Request.removeUntilValid(requestList, maxCapacity);
        if (requests == null || requests.isEmpty())
            return null;
        Route routeWithoutEndDepot = new Route();
        routeWithoutEndDepot.setUsedVehicle(journey.getUsedVehicle());
        routeWithoutEndDepot.setMaxCapacity(maxCapacity);
        Request firstRequest = requests.get(0);
        routeWithoutEndDepot.addFirstRequestAndStartDepot(firstRequest, optimizationScenario);
        for (int i = 1; i < requests.size(); i++) {
            routeWithoutEndDepot.addRequest(requests.get(i), optimizationScenario);
        }
        return routeWithoutEndDepot;
    }

    public Integer findBestInsertion(Request insertedRequest, OptimizationScenario optimizationScenario) {
        Route route = clone(this);
        Integer bestInsertIndex = null;
        int minTotalTravelTime = 999999999;
        for (int i = 0; i < requests.size() + 1; i++) {
            Route newRoute = route.insertableRequest(insertedRequest, i, optimizationScenario);
            if (newRoute != null) {
                newRoute.calculateTotal();
                if (newRoute.getTotalTravelTime() < minTotalTravelTime) {
                    minTotalTravelTime = newRoute.getTotalTravelTime();
                    bestInsertIndex = i;
                }
            }
        }
        System.err.println("best insert for " + insertedRequest.getCustomerId() + " is " + bestInsertIndex);
        return bestInsertIndex;
    }

    public Route insertableRequest(Request insertedRequest, int index, OptimizationScenario optimizationScenario) {
        Route testInsertRoute = clone(this);
        System.out.println("check " + insertedRequest.getCustomerId() + " insert to " + index);
        try {
            testInsertRoute.insertRequest(insertedRequest, index, optimizationScenario);
        } catch (IllegalArgumentException e) {
            return null;
        }
        if (testInsertRoute.isViolateTimeWindowConstrain(optimizationScenario))
            return null;
        if (testInsertRoute.isViolateCapacity())
            return null;
        System.out.println("is insertAble");
        return testInsertRoute;
    }

    public Route insertRequest(Request insertedRequest, int index, OptimizationScenario optimizationScenario) {
        if (index == 0) {
            Depot startDepot = insertedRequest.getDepot();
            setStartDepot(startDepot);
        }
        requests.add(index, insertedRequest);
        // add end Depot
        this.updateEndDepot(optimizationScenario);
        updateRoute(optimizationScenario);
        return this;
    }

    public Route bestInsertRequest(Request insertedRequest, OptimizationScenario optimizationScenario) {
        Integer bestInsertIndex = findBestInsertion(insertedRequest, optimizationScenario);
        if (bestInsertIndex != null)
            return insertRequest(insertedRequest, bestInsertIndex, optimizationScenario);
        else {
            System.out.println("Can not found best insert for requestID: " + insertedRequest.getId());
            return null;
        }
    }

    public static Route bestInsertRequest(Route route, List<Request> insertedRequests, OptimizationScenario optimizationScenario) {
        Route routeClone = Route.clone(route);
        for (Request insertedRequest : insertedRequests) {
            Route testRoute = Route.clone(routeClone);
            testRoute = testRoute.bestInsertRequest(insertedRequest, optimizationScenario);
            if (testRoute != null)
                routeClone = testRoute;
        }
        if (route.getRequests().size() == routeClone.getRequests().size())
            return null;
        routeClone.updateRoute(optimizationScenario);
        return routeClone;
    }

    public Route updateEndDepot(OptimizationScenario optimizationScenario) {
        Request lastRequest = this.requests.get(this.requests.size() - 1);
        Depot endDepot = this.randomEndDepot(optimizationScenario);
        setEndDepot(endDepot);
        return updateRoute(optimizationScenario);

    }

    public static Route optimizeRoute(Route route, OptimizationScenario optimizationScenario) {
        Random random = new Random();
        List<Request> notServedRequests = new ArrayList<>();
        notServedRequests.addAll(route.getRequests());
        Route optimizeRoute = new Route();
        Depot startDepot = route.getEndDepot();
        int startTimeOfDepot = route.getTimeline().get(0).getStartTime();
        optimizeRoute.setArriveStartDepotAt(startTimeOfDepot);
        Request firstRequest = startDepot.getNearestRequest(notServedRequests);
        if (firstRequest == null)
            throw new NullPointerException();
        optimizeRoute.addFirstRequestAndStartDepot(firstRequest, optimizationScenario);
        notServedRequests = CommonUtils.removeRequest(notServedRequests, firstRequest);
        optimizeRoute.setMaxCapacity(route.getMaxCapacity());
        optimizeRoute.setUsedVehicle(route.getUsedVehicle());
        for (Request request : notServedRequests) {
            optimizeRoute.bestInsertRequest(request, optimizationScenario);
        }
        optimizeRoute.updateEndDepot(optimizationScenario);
        if (optimizeRoute.getRequests().size() < route.getRequests().size()) {
            System.err.println("Can not optimize this route");
            return route;
        } else {
            System.out.println("optimize success");
        }
        optimizeRoute.updateRoute(optimizationScenario);
        if (optimizeRoute.isViolateTimeWindowConstrain(optimizationScenario))
            throw new NullPointerException();
        if (optimizeRoute.isTotalCostExceedTotalValue())
            return route;
        route.calculateTotal();
        optimizeRoute.calculateTotal();
        if (optimizeRoute.getTotalTravelTime() > route.getTotalTravelTime()) {
            return route;
        }
        return optimizeRoute;
    }


    public boolean isSameTo(Route route) {
        List<Request> requests = new ArrayList<>();
        requests.addAll(route.getRequests());
        if (this.requests.equals(requests))
            return true;
        Collections.reverse(requests);
        if (this.requests.equals(requests))
            return true;
        return false;
    }

    public boolean isExisted(List<Route> routeInfos) {
        for (Route route : routeInfos) {
            if (isSameTo(route))
                return true;
        }
        return false;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getTotalTravelTime() {
        return totalTravelTime;
    }

    public void setTotalTravelTime(int totalTravelTime) {
        this.totalTravelTime = totalTravelTime;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public String timelineToString() {
        String timelineStr = "";
        for (TimeWindow timeWindow : timeline) {
            timelineStr += timeWindow.toString();
        }
        return timelineStr;
    }

    public void printTimeLine() {
        System.out.println("Depot timeline " + this.getTimeline().get(0));
        for (int i = 0; i < this.requests.size(); i++) {
            System.out.println("Request " + requests.get(i).getId() + " timeline:");
            System.out.println(this.getTimeline().get(i + 1));
        }
    }

    public List<Depot> getValidEndDepots(OptimizationScenario optimizationScenario) {
        List<Depot> validEndDepots = new ArrayList<>();
        for (Depot depotInput : optimizationScenario.getDepots()) {
            Route routeClone = clone(this);
            routeClone.setEndDepot(depotInput);
            routeClone.updateRoute(optimizationScenario);
            if (!routeClone.isViolateTimeWindowDepot())
                validEndDepots.add(depotInput);
        }
        return validEndDepots;
    }

    public Depot randomEndDepot(OptimizationScenario optimizationScenario) {
        List<Depot> validEndDepots = getValidEndDepots(optimizationScenario);
        int randomIndex = randomGenerator.nextInt(validEndDepots.size());
        return validEndDepots.get(randomIndex);
    }

}


