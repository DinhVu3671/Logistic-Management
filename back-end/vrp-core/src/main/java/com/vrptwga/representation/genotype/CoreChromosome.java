package com.vrptwga.representation.genotype;

import com.vrptwga.representation.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoreChromosome {

    private List<String> nodes = new ArrayList<>();
    private List<String> schemeChromosome = new ArrayList<>();

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public List<String> getSchemeChromosome() {
        return schemeChromosome;
    }

    public void setSchemeChromosome(List<String> schemeChromosome) {
        this.schemeChromosome = schemeChromosome;
    }

    @Override
    public String toString() {
        return "CoreChromosome{" +
                "nodes=" + nodes +
                ", schemeChromosome=" + schemeChromosome +
                '}';
    }

    public CoreChromosome(){
    }

    public CoreChromosome(Individual individual){
        List<String> nodes = individual.getJourneys().stream()
                .flatMap(journey -> journey.getRoutes().stream())
                .flatMap(route -> route.getNodes().stream())
                .collect(Collectors.toList());
        this.nodes = nodes;
        schemeChromosome.addAll(nodes.stream().map(Objects::toString).collect(Collectors.toList()));
    }
}
