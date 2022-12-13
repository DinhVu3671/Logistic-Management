package com.vrptwga.representation;

import com.vrptwga.evaluate.Evaluate;
import com.vrptwga.concepts.*;
import com.vrptwga.representation.genotype.Genotype;
import com.vrptwga.representation.phenotype.Journey;
import com.vrptwga.representation.phenotype.Route;
import com.vrptwga.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Individual implements Cloneable {

    private List<Journey> journeys = new ArrayList<>();
    private List<Customer> unServedCustomers = new ArrayList<Customer>();
    private List<Vehicle> unusedVehicles = new ArrayList<>();
    private List<Request> notServedRequests = new ArrayList<>();
    private double objectiveValue;
    private double totalTravelTime; //total traveling time
    private double totalTravelDistance;
    private int numberUsedVehicle;
    private double fitness;
    private double totalCost;
    private double totalVOCCost;
    private double totalVTCCost;
    private double totalPenaltyCost;
    private Genotype genotype;

    public void calculate() {
        double travelingTime = 0;
        double travelingDistance = 0;
        double totalCost = 0;
        double totalVOCCost = 0;
        double totalVTCCost = 0;
        double totalPenaltyCost = 0;
        for (Journey journey : journeys) {
            journey.calculateTotal();
            travelingTime += journey.getTotalTravelTime();
            travelingDistance += journey.getTotalDistance();
            totalCost += journey.getTotalCost();
            totalVOCCost += journey.getTotalVOCCost();
            totalPenaltyCost += journey.getTotalPenaltyCost();
            totalVTCCost += journey.getUsedVehicle().getFixedCost();
        }
        this.numberUsedVehicle = journeys.size();
        this.totalTravelTime = totalTravelTime;
        this.totalTravelDistance = totalTravelDistance;
        this.totalCost = totalCost;
        this.totalVOCCost = totalVOCCost;
        this.totalPenaltyCost = totalPenaltyCost;
        this.totalVTCCost = totalVTCCost;
        this.objectiveValue = this.totalCost + this.totalVTCCost;
    }

    public String getFitnessString() {
        this.calculate();
        return "Individual{" +
                "objectiveValue=" + objectiveValue +
                ", totalVOC=" + totalCost +
                ", totalVTC=" + this.totalVTCCost +
                ", totalPenaltyCost=" + this.totalPenaltyCost +
                '}';
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getTotalTravelDistance() {
        return totalTravelDistance;
    }

    public void setTotalTravelDistance(double totalTravelDistance) {
        this.totalTravelDistance = totalTravelDistance;
    }

    public int getNumberUsedVehicle() {
        return numberUsedVehicle;
    }

    public void setNumberUsedVehicle(int numberUsedVehicle) {
        this.numberUsedVehicle = numberUsedVehicle;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Journey> getJourneys() {
        return journeys;
    }

    public void setJourneys(List<Journey> journeys) {
        this.journeys = journeys;
    }

    public double getObjectiveValue() {
        return objectiveValue;
    }

    public void setObjectiveValue(double objectiveValue) {
        this.objectiveValue = objectiveValue;
    }

    public Genotype getGenotype() {
        return genotype;
    }

    public void setGenotype(Genotype genotype) {
        this.genotype = genotype;
    }

    @Override
    public Individual clone() {
        Individual clone = null;
        try {
            clone = (Individual) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clone;
    }

    public static Individual clone(Individual individual) {
        Individual clone = new Individual();
        clone.setUnusedVehicles(individual.getUnusedVehicles());
        clone.setNotServedRequests(individual.getNotServedRequests());
        clone.setObjectiveValue(individual.getObjectiveValue());
        clone.setFitness(individual.getFitness());
        clone.getJourneys().addAll(individual.getJourneys());
        return clone;
    }

    public List<Request> getNotServedRequests() {
        return notServedRequests;
    }

    public void setNotServedRequests(List<Request> notServedRequests) {
        this.notServedRequests = notServedRequests;
    }

    public double getTotalTravelTime() {
        return totalTravelTime;
    }

    public void setTotalTravelTime(double totalTravelTime) {
        this.totalTravelTime = totalTravelTime;
    }

    public List<Customer> getUnServedCustomers() {
        return unServedCustomers;
    }

    public void setUnServedCustomers(List<Customer> unServedCustomers) {
        this.unServedCustomers = unServedCustomers;
    }

    public List<Request> removeNotServedRequest(Request request) {
        for (int i = 0; i < this.notServedRequests.size(); i++) {
            if (this.notServedRequests.get(i).getId() == request.getId())
                this.notServedRequests.remove(i);
        }
        return this.notServedRequests;
    }

    public List<Request> updateNotServedRequests() {
        List<Request> requests = new ArrayList<>();
        for (Journey journey : journeys) {
            for (Route route : journey.getRoutes()) {
                for (Request request : route.getRequests()) {
                    requests.add(request);
                }
            }
        }
        for (Request request : requests) {
            removeNotServedRequest(request);
        }
        return this.notServedRequests;
    }

    public List<Request> getServedRequests() {
        List<Request> requests = new ArrayList<>();
        for (Journey journey : journeys) {
            for (Route route : journey.getRoutes()) {
                for (Request request : route.getRequests()) {
                    requests.add(request);
                }
            }
        }
        return requests;
    }

    public static Individual createIndividual(OptimizationScenario optimizationScenario) {
        Individual individual = new Individual();
        individual.getNotServedRequests().addAll(optimizationScenario.getRequests());
        individual.setUnusedVehicles(optimizationScenario.getVehicles());
        while (individual.getNotServedRequests().size() > 0) {
            Journey newJourney = Journey.createNewJourney(individual, optimizationScenario);
            if (newJourney == null)
                return null;
            individual.getJourneys().add(newJourney);
            individual.updateNotServedRequests();
            individual.updateUnusedVehicles();
            individual.calculateTotal();
        }
        try {
            individual.checkValidation(optimizationScenario);
        } catch (NullPointerException e) {
            return null;
        }
        return individual;
    }

    // Khoi tao ca the cho den khi duoc moi thoi. Tru duy nhat truong hop vuot thoi gian
    public static Individual createInsteadIndividual(OptimizationScenario optimizationScenario) {
        long startTime = System.currentTimeMillis();
        Individual individual = null;
        while (individual == null) {
            individual = createIndividual(optimizationScenario);
            long executedTime = System.currentTimeMillis() - startTime;
            if (executedTime > optimizationScenario.getTime() && individual == null)
                return null;
        }
        return individual;
    }

    // best-cost crossover algorithm step by step
    public static Individual createIndividualByCrossover(List<Request> firstRequest, List<Request> secondRequest, OptimizationScenario optimizationScenario) {
        List<Request> firstRequestsClone = new ArrayList<>();
        firstRequestsClone.addAll(firstRequest);
        List<Request> secondRequestsClone = new ArrayList<>();
        secondRequestsClone.addAll(secondRequest);
        Individual individual = new Individual();
        individual.getNotServedRequests().addAll(optimizationScenario.getRequests());
        individual.setUnusedVehicles(optimizationScenario.getVehicles());
        Journey firstJourney = Journey.createJourneyByCrossover(firstRequestsClone, secondRequestsClone, individual, optimizationScenario);
        individual.getJourneys().add(firstJourney);
        individual.updateNotServedRequests();
        individual.updateUnusedVehicles();
        while (individual.getNotServedRequests().size() > 0) {
            Journey newJourney = Journey.createNewJourney(individual, optimizationScenario);
            if (newJourney == null)
                break;
            individual.getJourneys().add(newJourney);
            individual.updateNotServedRequests();
            individual.updateUnusedVehicles();
        }
        try {
            individual.checkValidation(optimizationScenario);
        } catch (NullPointerException e) {
            individual = Individual.createInsteadIndividual(optimizationScenario);
        }
        return individual;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        int i = 1;
        for (Journey journey : journeys) {
            sb.append("journey " + i + " : " + journey);
            sb.append("\n");
            i++;
        }
        sb.append("travelingTime: " + calculateTravelingTime() + "\n");
        sb.append(getObjectiveValue() == 0 ? "" : ("objectiveValue: " + getObjectiveValue() + "\n"));
        return sb.toString();
    }

    public double calculateTravelingTime() {
        totalTravelTime = 0;
        for (Journey journey : journeys) {
            journey.calculateTotal();
            totalTravelTime += journey.getTotalTravelTime();
        }
        return totalTravelTime;
    }

    public void calculateTotal() {
        totalTravelTime = 0;
        totalTravelDistance = 0;
        totalCost = 0;
        for (Journey journey : journeys) {
            journey.calculateTotal();
            totalTravelTime += journey.getTotalTravelTime();
            totalTravelDistance += journey.getTotalDistance();
            totalCost += journey.getTotalCost();
            totalVOCCost += journey.getTotalVOCCost();
            totalPenaltyCost += journey.getTotalPenaltyCost();
        }
        return;
    }

    public Journey getRandomJourney() {
        Random random = new Random();
        return this.journeys.get(random.nextInt(journeys.size()));
    }


    // reverse mutation algorithm step by step
    public static Individual optimizeRandomJourney(Individual individual, OptimizationScenario optimizationScenario) {
        Random random = new Random();
        Individual individualClone = Individual.clone(individual);
        List<Journey> journeys = new ArrayList<>();
        journeys.addAll(individualClone.getJourneys());
        int randomIndex = random.nextInt(individualClone.getJourneys().size());
        // tim 1 journey ngan nhien de mutate
        Journey journey = (individualClone.getJourneys().get(randomIndex));
        Journey journeyClone = Journey.clone(journey);
        journeyClone = Journey.optimizeRoutes(journeyClone, optimizationScenario);
        individualClone.getJourneys().set(randomIndex, journeyClone);
        List<Request> requestList = new ArrayList<>();
        requestList.addAll(journey.getRequests());
        List<Request> notServedRequests = CommonUtils.removeRequests(requestList, journeyClone.getRequests());
        if (!notServedRequests.isEmpty()) {
            addRequestsToJourney(notServedRequests, individualClone, optimizationScenario);
        }
        // ca the duoc mutate khong vi pham rang buoc
        try {
            individualClone.checkValidation(optimizationScenario);
        } catch (Exception e) {
            // vi pham => giu nguyen ca the ban dau
            return individual;
        }
        // khong vi pham => luu ca the moi
        return individualClone;
    }

    public static Individual optimizeJourneys(Individual individual, OptimizationScenario optimizationScenario) {
        Individual individualClone = Individual.clone(individual);
        List<Journey> journeys = new ArrayList<>();
        journeys.addAll(individualClone.getJourneys());
        for (int i = 0; i < journeys.size(); i++) {
            Journey journeyClone = Journey.clone(journeys.get(i));
            journeyClone = Journey.optimizeRoutes(journeyClone, optimizationScenario);
            individualClone.getJourneys().set(i, journeyClone);
        }
        List<Request> notServedRequests = new ArrayList<>();
        notServedRequests.addAll(optimizationScenario.getRequests());
        notServedRequests = CommonUtils.removeRequests(notServedRequests, individualClone.getServedRequests());
        if (!notServedRequests.isEmpty()) {
            addRequestsToJourney(notServedRequests, individualClone, optimizationScenario);
        }
        try {
            individualClone.calculateTotal();
            individualClone.checkValidation(optimizationScenario);
        }catch (NullPointerException e){
            return individual;
        }
        if (Evaluate.calculateObjectiveValue(individualClone) < individual.getObjectiveValue())
            return individualClone;
        return individual;
    }

    public static Individual addRequestsToJourney(List<Request> notServedRequests, Individual individual, OptimizationScenario optimizationScenario) {
        if (!notServedRequests.isEmpty()) {
            for (int i = 0; i < notServedRequests.size(); i++) {
                Request notServedRequest = notServedRequests.get(i);
                for (int j = 0; j < individual.getJourneys().size(); j++) {
                    Journey cloneJourney = individual.getJourneys().get(j);
                    Integer addRouteIndex = cloneJourney.findAddableRequestRoute(notServedRequest, optimizationScenario);
                    if (addRouteIndex != null) {
                        cloneJourney.getRoutes().get(addRouteIndex).addRequest(notServedRequest, optimizationScenario);
                        notServedRequests.remove(notServedRequest);
                    }
                }
            }
        }
        if (!notServedRequests.isEmpty()) {
            for (Journey individualCloneJourney : individual.getJourneys()) {
                Journey journey1 = Journey.clone(individualCloneJourney);
                Route newRoute = Route.createNewRouteInJourney(journey1, notServedRequests, optimizationScenario);
                if (newRoute != null) {
                    notServedRequests = CommonUtils.removeRequests(notServedRequests, newRoute.getRequests());
                    journey1.getRoutes().add(newRoute);
                }
            }
        }
        return individual;
    }

    public boolean hasSameRoutes(Individual individual) {
        int sameRouteCount = 0;
        for (Journey journey : journeys) {
            for (Journey individualJourney : individual.getJourneys()) {
                for (Route route : journey.getRoutes()) {
                    if (individualJourney.hasRoute(route))
                        sameRouteCount++;
                    if (sameRouteCount == countRoute() - 1)
                        return true;
                }
            }
        }
        if (this.getObjectiveValue() == individual.getObjectiveValue() && this.countRoute() == individual.countRoute())
            return true;
        return false;
    }

    public int countRoute() {
        int count = 0;
        for (Journey journey : journeys) {
            count += journey.getRoutes().size();
        }
        return count;
    }

    public List<Vehicle> getUnusedVehicles() {
        return unusedVehicles;
    }

    public void setUnusedVehicles(List<Vehicle> unusedVehicles) {
        List<Vehicle> vehicleInputs = new ArrayList<>();
        if (unusedVehicles != null)
            vehicleInputs.addAll(unusedVehicles);
        vehicleInputs = Vehicle.sortAscByMaxCapacity(vehicleInputs);
        this.unusedVehicles = vehicleInputs;
    }

    public List<Vehicle> updateUnusedVehicles() {
        for (Journey journey : journeys) {
            Vehicle usedVehicle = Vehicle.getVehicleById(journey.getUsedVehicle().getId(), this.unusedVehicles);
            this.unusedVehicles.remove(usedVehicle);
        }
        return this.unusedVehicles;
    }

//    public static Individual mergeIndividual(List<Individual> individuals) {
//        Individual individual = new Individual();
//        for (Individual individual1 : individuals) {
//            individual.getJourneys().addAll(individual1.getJourneys());
//        }
//        individual.setObjectiveValue(Evaluate.calculateObjectiveValue(individual));
//        return individual;
//    }

    public Individual() {

    }

    public Individual(OptimizationScenario optimizationScenario) {
        this.notServedRequests.addAll(optimizationScenario.getRequests());
        this.unusedVehicles.addAll(optimizationScenario.getVehicles());
    }

    // cac rang buoc phai thoa man: thoi gian, khoang cach, doi xe, timewindow, so luong request ...
    public void checkValidation(OptimizationScenario optimizationScenario) {
        int count = 0;
        for (Journey journey : journeys) {
            if (journey.getUsedVehicle() == null)
                throw new NullPointerException();
            journey.calculateTotal();
            if (optimizationScenario.isLimitedDistance() && journey.getTotalDistance() > optimizationScenario.getMaxDistance())
                throw new NullPointerException();
            if (optimizationScenario.isLimitedTime() && journey.getTotalTravelTime() > optimizationScenario.getMaxTravelTime())
                throw new NullPointerException();
            for (Route route : journey.getRoutes()) {
                count += route.getRequests().size();
                if (route.getTimeline().size() != (route.getRequests().size() + 2))
                    throw new NullPointerException();
                if (route.isViolateTimeWindowConstrain(optimizationScenario))
                    throw new NullPointerException();
                if (route == null)
                    throw new NullPointerException();
                for (Request request : route.getRequests()) {
                    if (request.getExcludeVehicles().contains(journey.getUsedVehicle()))
                        throw new NullPointerException();
                }
            }
        }
        if (count != optimizationScenario.getRequests().size())
            throw new NullPointerException();
    }

//    public Individual changeFitVehicles() {
//        List<Journey> journeys = this.getJourneys();
//        journeys = Journey.sortByVehicle(journeys, true);
//        for (Journey journey : journeys) {
//            Journey testJourney = Journey.clone(journey);
//            VehicleInput vehicle = testJourney.findFitVehicle(this.unusedVehicles);
//            if (vehicle != null) {
//                journey.setUsedVehicle(vehicle);
//                this.updateUnusedVehicles();
//            }
//        }
//        journeys = Journey.sortByVehicle(journeys, false);
//        this.unusedVehicles = VehicleInput.sortAscByMaxCapacity(this.unusedVehicles);
//        return this;
//    }

}