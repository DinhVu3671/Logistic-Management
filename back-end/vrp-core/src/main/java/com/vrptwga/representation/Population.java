package com.vrptwga.representation;

import java.util.ArrayList;
import java.util.List;

public class Population {

    private List<Individual> individuals = new ArrayList<>();
    private List<Individual> elites = new ArrayList<>();
    private int currentGeneration;

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public void setCurrentGeneration(int currentGeneration) {
        this.currentGeneration = currentGeneration;
    }

    public List<Individual> getElites() {
        return elites;
    }

    public void setElites(List<Individual> elites) {
        this.elites = elites;
    }
}
