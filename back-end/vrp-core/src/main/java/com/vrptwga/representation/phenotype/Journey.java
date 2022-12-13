package com.vrptwga.representation.phenotype;

import com.vrptwga.concepts.*;
import com.vrptwga.representation.Individual;
import com.vrptwga.utils.CommonUtils;

import java.util.*;

public class Journey {

    private List<Route> routes = new ArrayList<>();
    private List<Request> requests = new ArrayList<>();
    private int totalTravelTime;
    private int totalTime;
    private int totalDistance;
    private double maxCapacity;
    private double totalCost;
    private Vehicle usedVehicle;
    private double totalVOCCost;
    private double totalPenaltyCost;

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public Vehicle getUsedVehicle() {
        return usedVehicle;
    }

    public void setUsedVehicle(Vehicle usedVehicle) {
        this.usedVehicle = usedVehicle;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalVOCCost() {
        return totalVOCCost;
    }

    public void setTotalVOCCost(double totalVOCCost) {
        this.totalVOCCost = totalVOCCost;
    }

    public double getTotalPenaltyCost() {
        return totalPenaltyCost;
    }

    public void setTotalPenaltyCost(double totalPenaltyCost) {
        this.totalPenaltyCost = totalPenaltyCost;
    }

    public Route getRandomRoute() {
        Random random = new Random();
        return this.routes.get(random.nextInt(routes.size()));
    }

    public static Journey clone(Journey currentJourney) {
        Journey clone = new Journey();
//        clone.setMaxCapacity(currentJourney.getMaxCapacity());
        clone.setUsedVehicle(currentJourney.getUsedVehicle());
        for (Route route : currentJourney.getRoutes()) {
            clone.getRoutes().add(Route.clone(route));
        }
        return clone;
    }

    public boolean hasRoute(Route route) {
        if (route.isExisted(this.routes))
            return true;
        else
            return false;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Request> getRequests() {
        List<Request> requests = new ArrayList<>();
        for (Route route : routes) {
            requests.addAll(route.getRequests());
        }
        return requests;
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

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Route getLastRoute() {
        return this.routes.get(this.routes.size() - 1);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        int i = 1;
        for (Route route : routes) {
            sb.append("route " + i + " : " + route);
            sb.append("\n");
            i++;
        }
        calculateTotal();
        sb.append("travelingTime: " + getTotalTravelTime() + "\n");
        return sb.toString();
    }

    public Journey updateTimeline(OptimizationScenario optimizationScenario) {
        Route firstRoute = routes.get(0);
        firstRoute.updateTimeLine(optimizationScenario.getLoadingTimePerKg(), optimizationScenario.getUnloadingTimePerKg());
        for (int i = 1; i < routes.size(); i++) {
            Route previousRoute = routes.get(i - 1);
            Route route = routes.get(i);
            route.setArriveStartDepotAt(previousRoute.arriveEndDepotAt());
            route.updateTimeLine(optimizationScenario.getLoadingTimePerKg(), optimizationScenario.getUnloadingTimePerKg());
        }
        return this;
    }

    public void calculateTotal() {
        totalTravelTime = 0;
        totalDistance = 0;
        totalCost = 0;
        totalTime = 0;
        totalPenaltyCost = 0;
        for (Route route : routes) {
            route.calculateTotal();
            totalTravelTime += route.getTotalTravelTime();
            totalDistance += route.getTotalDistance();
            totalCost += route.getTotalCost();
            totalPenaltyCost += route.getPenaltyCost();
        }
        totalVOCCost = totalCost - totalPenaltyCost;
        List<TimeWindow> firstTimeLine = routes.get(0).getTimeline();
        List<TimeWindow> lastTimeline = routes.get(routes.size() - 1).getTimeline();
        totalTime = lastTimeline.get(lastTimeline.size() - 1).getStartTime() - firstTimeLine.get(0).getStartTime();
        return;
    }

    public static Journey createNewJourney(Individual individual, OptimizationScenario optimizationScenario) {
        Journey journey = new Journey();
        List<Request> notServedRequests = new ArrayList<>();
        notServedRequests.addAll(individual.getNotServedRequests());
        Vehicle usedVehicle = Vehicle.getBestFitVehicle(individual.getUnusedVehicles(), notServedRequests);
        if (usedVehicle == null)
            return null;
        journey.setUsedVehicle(usedVehicle);
        journey.setMaxCapacity(usedVehicle.getCapacity());
        notServedRequests = Journey.removeInValidRequest(journey, notServedRequests);
        if (notServedRequests.isEmpty())
            return null;
        Route firstRoute = Route.createNewRoute(optimizationScenario, notServedRequests, journey);
        journey.getRoutes().add(firstRoute);
        notServedRequests = CommonUtils.removeRequests(notServedRequests, firstRoute.getRequests());
        while (notServedRequests.size() > 0) {
            Route nextRoute = Route.createNewRouteInJourney(journey, notServedRequests, optimizationScenario);
            if (nextRoute != null) {
                notServedRequests = CommonUtils.removeRequests(notServedRequests, nextRoute.getRequests());
                journey.getRoutes().add(nextRoute);
            } else {
                break;
            }
        }
        journey.calculateTotal();
        if (optimizationScenario.isLimitedDistance() && journey.getTotalDistance() > optimizationScenario.getMaxDistance())
            return null;
        if (optimizationScenario.isLimitedTime() && journey.getTotalTravelTime() > optimizationScenario.getMaxTravelTime())
            return null;
        return journey;
    }

    public static Journey createJourneyWithFirstRequests(List<Request> firstRequests, Individual individual, OptimizationScenario optimizationScenario) {
        Journey journey = new Journey();
        List<Request> notServedRequests = new ArrayList<>();
        Vehicle usedVehicle = Vehicle.getBestFitVehicle(individual.getUnusedVehicles(), firstRequests);
        journey.setMaxCapacity(usedVehicle.getCapacity());
        journey.setUsedVehicle(usedVehicle);
        notServedRequests.addAll(individual.getNotServedRequests());
        Route firstRoute = Route.createRouteByFixedRequest(firstRequests, optimizationScenario, journey);
        notServedRequests = CommonUtils.removeRequests(notServedRequests, firstRoute.getRequests());
        for (Request notServedRequest : notServedRequests) {
            if (firstRoute.isAddableRequest(notServedRequest, optimizationScenario)) {
                System.err.println("Add RequestID: R" + notServedRequest.getId() + " to this route");
                firstRoute.addRequest(notServedRequest, optimizationScenario);
            } else
                System.err.println("Can not add RequestID: R" + notServedRequest.getId());
        }
        firstRoute.addEndDepot(optimizationScenario);
        journey.getRoutes().add(firstRoute);
        return journey;
    }

    public static Journey createJourneyByCrossover(List<Request> firstRequests, List<Request> secondRequests, Individual individual, OptimizationScenario optimizationScenario) {
        Journey journey = null;
        List<Request> notServedRequests = new ArrayList<>();
        notServedRequests.addAll(optimizationScenario.getRequests());
        journey = createJourneyWithFirstRequests(firstRequests, individual, optimizationScenario);
        notServedRequests = CommonUtils.removeRequests(notServedRequests, journey.getRequests());
        secondRequests = CommonUtils.removeRequests(secondRequests, journey.getRequests());
        if (!secondRequests.isEmpty()) {
            Route secondRoute = Route.createNewRouteInJourney(journey, secondRequests, optimizationScenario);
            if (secondRoute != null) {
                notServedRequests = CommonUtils.removeRequests(notServedRequests, secondRoute.getRequests());
                for (Request notServedRequest : notServedRequests) {
                    if (secondRoute.isAddableRequest(notServedRequest, optimizationScenario)) {
                        System.err.println("Add RequestID: R" + notServedRequest.getId() + " to this route");
                        secondRoute.addRequest(notServedRequest, optimizationScenario);
                    } else
                        System.err.println("Can not add RequestID: R" + notServedRequest.getId());
                }
                journey.getRoutes().add(secondRoute);
            }
        }
        notServedRequests = CommonUtils.removeRequests(notServedRequests, journey.getRequests());
        while (notServedRequests.size() > 0) {
            Route nextRoute = Route.createNewRouteInJourney(journey, notServedRequests, optimizationScenario);
            if (nextRoute != null) {
                notServedRequests = CommonUtils.removeRequests(notServedRequests, nextRoute.getRequests());
                journey.getRoutes().add(nextRoute);
            } else {
                break;
            }
        }
        journey.calculateTotal();
        return journey;

    }

    public static Journey optimizeRoutes(Journey journey, OptimizationScenario optimizationScenario) {
        Journey journeyClone = Journey.clone(journey);
        for (int i = 0; i < journeyClone.getRoutes().size(); i++) {
            Route route = journeyClone.getRoutes().get(i);
            Route testRoute = Route.clone(route);
            testRoute = Route.optimizeRoute(testRoute, optimizationScenario);
            System.out.println("Timeline testRoute");
            testRoute.printTimeLine();
            journeyClone.getRoutes().set(i, testRoute);
            journeyClone.updateTimeline(optimizationScenario);
            for (Route route1 : journeyClone.getRoutes()) {
                if (route1.isViolateTimeWindowConstrain(optimizationScenario)) {
                    System.out.println("Timeline route1");
                    route1.printTimeLine();
                    throw new NullPointerException();
                }
            }
        }
        journeyClone.updateTimeline(optimizationScenario);
        for (Route route : journeyClone.getRoutes()) {
            if (route.isViolateTimeWindowConstrain(optimizationScenario))
                throw new NullPointerException();
        }
//        List<Request> notServedRequests = new ArrayList<>();
//        notServedRequests.addAll(journey.getRequests());
//        notServedRequests = CommonUtils.removeRequests(notServedRequests, journey.getRequests());
//        if (!notServedRequests.isEmpty()) {
//            for (int i = 0; i < journeyClone.getRoutes().size(); i++) {
//                Route route = journeyClone.getRoutes().get(i);
//                Route testRoute = Route.bestInsertRequest(route, notServedRequests, optimizationScenario);
//                if (testRoute != null) {
//                    if (testRoute.isViolateTimeWindowConstrain())
//                        throw new NullPointerException();
//                    journeyClone.getRoutes().set(i, testRoute);
//                    journeyClone.updateTimeline(optimizationScenario);
//                    notServedRequests = CommonUtils.removeRequests(notServedRequests, testRoute.getRequests());
//                }
//            }
//        }
//        for (Route route : journeyClone.getRoutes()) {
//            if (route.isViolateTimeWindowConstrain())
//                throw new NullPointerException();
//        }

        if (journeyClone.countRequest() > journey.countRequest())
            throw new NullPointerException();
        return journeyClone;
    }

    public Integer findAddableRequestRoute(Request request, OptimizationScenario optimizationScenario) {
        Journey journeyClone = Journey.clone(this);
        if (journeyClone.isExcludedByVehicle(request))
            return null;
        for (int i = 0; i < journeyClone.getRoutes().size(); i++) {
            Route route = journeyClone.getRoutes().get(i);
            if (route.isAddableRequest(request, optimizationScenario))
                return i;
        }
        return null;
    }

    public static List<Request> removeInValidRequest(Journey journey, List<Request> requests) {
        List<Request> requestsForJourney = new ArrayList<>();
        for (Request request : requests) {
            if (!journey.isExcludedByVehicle(request))
                requestsForJourney.add(request);
        }
        return requestsForJourney;
    }

    public boolean isExcludedByVehicle(Request request) {
        for (Vehicle excludeVehicle : request.getExcludeVehicles()) {
            try {
                if (excludeVehicle.getId() == usedVehicle.getId())
                    return true;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

//    public VehicleInput findFitVehicle(List<VehicleInput> vehicleInputs) {
//        List<VehicleInput> fitAbleVehicles = new ArrayList<>();
//        for (VehicleInput vehicleInput : vehicleInputs) {
//            if (vehicleInput.getCapacity() <= this.getUsedVehicle().getCapacity())
//                fitAbleVehicles.add(vehicleInput);
//        }
//        fitAbleVehicles.sort(Comparator.comparingDouble(VehicleInput::getCapacity));
//        for (VehicleInput fitAbleVehicle : fitAbleVehicles) {
//            Journey testJourney = clone(this);
//            if (testJourney.isFitWithVehicle(fitAbleVehicle))
//                return fitAbleVehicle;
//        }
//        return null;
//    }

//    public boolean isFitWithVehicle(VehicleInput vehicle) {
//        Journey testJourney = clone(this);
//        testJourney.setUsedVehicle(vehicle);
//        for (Route route : testJourney.getRoutes()) {
//            route.setUsedVehicle(vehicle);
//            if (route.isViolateConstrain())
//                return false;
//            for (Request request : route.getRequests()) {
//                if (request.getExcludeVehicles().contains(vehicle))
//                    return false;
//            }
//        }
//        return true;
//    }

    public static List<Journey> sortByVehicle(List<Journey> journeys, boolean isAsc) {
        if (isAsc)
            journeys.sort(Comparator.comparingDouble((Journey j) -> j.getUsedVehicle().getCapacity()));
        else
            journeys.sort(Comparator.comparingDouble((Journey j) -> j.getUsedVehicle().getCapacity()).reversed());
        return journeys;
    }

    public int countRequest() {
        int count = 0;
        for (Route route : routes) {
            count += route.getRequests().size();
        }
        return count;
    }

    public static Journey bestInsertRequestToJourney(Journey journey, Request request, OptimizationScenario optimizationScenario) {
        Integer addRouteIndex = journey.findAddableRequestRoute(request, optimizationScenario);
        Journey newJourney = null;
        int minTravelTime = 999999999;
        if (addRouteIndex == null)
            return null;
        for (int i = 0; i < journey.getRoutes().size(); i++) {
            Journey journeyClone = Journey.clone(journey);
            Route testRoute = Route.clone(journey.getRoutes().get(i));
            testRoute = testRoute.bestInsertRequest(request, optimizationScenario);
            if (testRoute != null) {
                if (isValidRoute(testRoute, i, journeyClone, optimizationScenario)) {
                    journeyClone.getRoutes().set(i, testRoute);
                    journeyClone.calculateTotal();
                    if (journeyClone.getTotalTravelTime() < minTravelTime) {
                        minTravelTime = journeyClone.getTotalTravelTime();
                        newJourney = journeyClone;
                    }
                }
            }
        }
        if (newJourney == null) {
            List<Request> notServedRequests = Arrays.asList(request);
            Route nextRoute = Route.createNewRouteInJourney(journey, notServedRequests, optimizationScenario);
            if (nextRoute != null) {
                journey.getRoutes().add(nextRoute);
                newJourney = journey;
            }
        }
        return newJourney;
    }

    public static boolean isValidRoute(Route updatedRoute, int index, Journey journey, OptimizationScenario optimizationScenario) {
        journey.getRoutes().set(index, updatedRoute);
        journey.updateTimeline(optimizationScenario);
        for (int i = index; i < journey.getRoutes().size(); i++) {
            if (journey.getRoutes().get(i).isViolateTimeWindowConstrain(optimizationScenario))
                return false;
        }
        return true;
    }

    public static Journey createOptimizeJourney(List<Request> requests, Vehicle usedVehicle, OptimizationScenario optimizationScenario) {
        Journey journey = new Journey();
        List<Request> notServedRequests = new ArrayList<>(requests);
        journey.setUsedVehicle(usedVehicle);
        journey.setMaxCapacity(usedVehicle.getCapacity());
        notServedRequests = Journey.removeInValidRequest(journey, notServedRequests);
        if (notServedRequests.isEmpty())
            return null;
        Route firstRoute = Route.createNewRoute(optimizationScenario, notServedRequests, journey);
        journey.getRoutes().add(firstRoute);
        notServedRequests = CommonUtils.removeRequests(notServedRequests, firstRoute.getRequests());
        while (notServedRequests.size() > 0) {
            Route nextRoute = Route.createNewRouteInJourney(journey, notServedRequests, optimizationScenario);
            if (nextRoute != null) {
                notServedRequests = CommonUtils.removeRequests(notServedRequests, nextRoute.getRequests());
                journey.getRoutes().add(nextRoute);
            } else {
                break;
            }
        }
        optimizeRoutes(journey, optimizationScenario);
        journey.calculateTotal();
        return journey;
    }
}
