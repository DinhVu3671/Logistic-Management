package com.vrptwga.representation.genotype;

import com.vrptwga.concepts.Request;
import com.vrptwga.representation.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TailerChromosome {

    private List<String> orders = new ArrayList<>();
    private List<String> schemeChromosome = new ArrayList<>();

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public List<String> getSchemeChromosome() {
        return schemeChromosome;
    }

    public void setSchemeChromosome(List<String> schemeChromosome) {
        this.schemeChromosome = schemeChromosome;
    }

    @Override
    public String toString() {
        return "TailerChromosome{" +
                "orders=" + orders +
                ", schemeChromosome=" + schemeChromosome +
                '}';
    }

    public TailerChromosome() {

    }

    public TailerChromosome(Individual individual) {
        List<String> orders = new ArrayList<>();
        orders = individual.getJourneys().stream()
                .flatMap(journey -> journey.getRoutes().stream())
                .flatMap(route -> route.getRequests().stream())
                .map(Request::getCode)
                .collect(Collectors.toList());
        this.orders = orders;
        schemeChromosome.addAll(orders.stream().map(Objects::toString).collect(Collectors.toList()));
    }
}
