package com.vrptwga.representation.genotype;

import com.vrptwga.concepts.Vehicle;
import com.vrptwga.representation.Individual;
import com.vrptwga.representation.phenotype.Journey;
import com.vrptwga.representation.phenotype.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HeaderChromosome {

    private List<Integer> priorityUses = new ArrayList<>();
    private List<Integer> numberRoutes = new ArrayList<>();
    private List<Integer> numberNodes = new ArrayList<>();
    private List<Integer> numberOrders = new ArrayList<>();
    private List<String> schemeChromosome = new ArrayList<>();

    public List<Integer> getPriorityUses() {
        return priorityUses;
    }

    public void setPriorityUses(List<Integer> priorityUses) {
        this.priorityUses = priorityUses;
    }

    public List<Integer> getNumberRoutes() {
        return numberRoutes;
    }

    public void setNumberRoutes(List<Integer> numberRoutes) {
        this.numberRoutes = numberRoutes;
    }

    public List<Integer> getNumberNodes() {
        return numberNodes;
    }

    public void setNumberNodes(List<Integer> numberNodes) {
        this.numberNodes = numberNodes;
    }

    public List<String> getSchemeChromosome() {
        return schemeChromosome;
    }

    public void setSchemeChromosome(List<String> schemeChromosome) {
        this.schemeChromosome = schemeChromosome;
    }

    public List<Integer> getNumberOrders() {
        return numberOrders;
    }

    public void setNumberOrders(List<Integer> numberOrders) {
        this.numberOrders = numberOrders;
    }

//    @Override
//    public String toString() {
//        return "SubChromosome{" +
//                "schemeChromosome=" + schemeChromosome +
//                '}';
//    }


    @Override
    public String toString() {
        return "HeaderChromosome{" +
                "priorityUses=" + priorityUses +
                ", numberRoutes=" + numberRoutes +
                ", numberNodes=" + numberNodes +
                ", numberOrders=" + numberOrders +
                ", schemeChromosome=" + schemeChromosome +
                '}';
    }

    public HeaderChromosome() {
    }

    public HeaderChromosome(Individual individual, List<Vehicle> vehicleInputs) {
//        int numberVehicleInput = individual.getNumberUsedVehicle() + individual.getUnusedVehicles().size();
        List<Integer> priorityUses = getPriorityUses(individual.getJourneys(), vehicleInputs);
        List<Integer> numberRoutes = getNumberOfRoutes(individual.getJourneys(), vehicleInputs);
        List<Integer> numberOfNodes = individual.getJourneys().stream().flatMap(journey -> journey.getRoutes().stream()).map(route -> route.getRequests().size() + 2).collect(Collectors.toList());
        List<Integer> numberOfOrders = individual.getJourneys().stream().flatMap(journey -> journey.getRoutes().stream()).map(route -> route.getRequests().size()).collect(Collectors.toList());
        this.priorityUses = priorityUses;
        this.numberRoutes = numberRoutes;
        this.numberNodes = numberOfNodes;
        this.numberOrders = numberOfOrders;
        schemeChromosome.addAll(priorityUses.stream().map(Object::toString).collect(Collectors.toList()));
        schemeChromosome.addAll(numberRoutes.stream().map(Object::toString).collect(Collectors.toList()));
        schemeChromosome.addAll(numberOfNodes.stream().map(Object::toString).collect(Collectors.toList()));
        schemeChromosome.addAll(numberOfOrders.stream().map(Object::toString).collect(Collectors.toList()));
    }

    public static List<Integer> getPriorityUses(List<Journey> journeys, List<Vehicle> vehicleInputs) {
        List<Integer> priorityUses = new ArrayList<>();
        for (Vehicle vehicleInput : vehicleInputs) {
            priorityUses.add(0);
        }
        int priorityUse = 1;
        for (Journey journey : journeys) {
            for (int i = 0; i < vehicleInputs.size(); i++) {
                if (journey.getUsedVehicle().getId() == vehicleInputs.get(i).getId()) {
                    priorityUses.set(i, priorityUse);
                    priorityUse++;
                }
            }
        }
        for (Integer priority : priorityUses) {
            if (priority == 0) {
                priority = priorityUse++;
            }
        }
        return priorityUses;
    }

    public static List<Integer> getNumberOfRoutes(List<Journey> journeys, List<Vehicle> vehicleInputs) {
        List<Integer> numberRoutes = vehicleInputs.stream().map(vehicleInput -> 0).collect(Collectors.toList());
        for (Journey journey : journeys) {
            for (int i = 0; i < vehicleInputs.size(); i++) {
                if (journey.getUsedVehicle().getId() == vehicleInputs.get(i).getId()) {
                    numberRoutes.set(i, journey.getRoutes().size());
                }
            }
        }
        return numberRoutes;
    }

    public static List<Integer> getNumberOfNodes(List<Journey> journeys) {
        List<Integer> numberOfNodes = new ArrayList<>();
        for (Journey journey : journeys) {
            for (Route route : journey.getRoutes()) {
                numberOfNodes.add(route.getServedCustomers().size()+2);
            }
        }
        return numberOfNodes;
    }
}
