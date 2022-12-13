package com.vrptwga.operator.crossover.imp;

import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.concepts.Request;
import com.vrptwga.operator.crossover.Crossover;
import com.vrptwga.representation.Individual;
import com.vrptwga.representation.Population;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BestCostCrossover extends Crossover {

    @Override
    public List<Individual> execute() {
        List<Individual> currentPopulation = ((Population) this.parameters.get("population")).getIndividuals();
        OptimizationScenario optimizationScenario = (OptimizationScenario) this.parameters.get("optimizationScenario");
        double randomCrossoverRate = 0;
        Random randomIndex = new Random();
        List<Individual> notCrossoverIndividuals = new ArrayList<>();
        List<Individual> childOfCrossoverIndividuals = new ArrayList<>();
        notCrossoverIndividuals.addAll(currentPopulation);
        // population / 2 coz we have parent1, 2.
        for (int i = 0; i < currentPopulation.size() / 2; i++) {
            randomCrossoverRate = Math.random();
            // crossover occur
            if (randomCrossoverRate < optimizationScenario.getProbCrossover()) {
                Individual parent1 = notCrossoverIndividuals.get(randomIndex.nextInt(notCrossoverIndividuals.size()));
                notCrossoverIndividuals.remove(parent1);
                Individual parent2 = notCrossoverIndividuals.get(randomIndex.nextInt(notCrossoverIndividuals.size()));
                notCrossoverIndividuals.remove(parent2);
                List<Request> selectedRequestROP1 = parent1.getJourneys().get(0).getRandomRoute().getRandomListRequest();
                List<Request> selectedRequestROP2 = parent2.getJourneys().get(0).getRandomRoute().getRandomListRequest();
                Individual child1 = Individual.createIndividualByCrossover(selectedRequestROP1, selectedRequestROP2, optimizationScenario);
                Individual child2 = Individual.createIndividualByCrossover(selectedRequestROP2, selectedRequestROP1, optimizationScenario);
                child1.calculateTravelingTime();
                child2.calculateTravelingTime();
                childOfCrossoverIndividuals.add(child1);
                childOfCrossoverIndividuals.add(child2);
            }
        }
//        for (Individual individual : childOfCrossoverIndividuals) {
//            individual.checkValidation(optimizationScenario);
//        }
        return childOfCrossoverIndividuals;
    }
}
