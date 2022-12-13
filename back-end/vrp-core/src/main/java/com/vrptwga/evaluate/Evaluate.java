package com.vrptwga.evaluate;

import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.representation.Individual;

import java.util.Comparator;
import java.util.List;

public class Evaluate {


    public static List<Individual> weightedSumMethod(List<Individual> population, OptimizationScenario optimizationScenario) {
        for (Individual individual : population) {
            individual.setObjectiveValue(calculateObjectiveValue(individual));
        }
        population.sort(Comparator.comparingDouble(Individual::getObjectiveValue));
        double minObjectiveValue = population.get(0).getObjectiveValue();
        double maxObjectiveValue = population.get(population.size() - 1).getObjectiveValue();
        for (Individual individual : population) {
            individual.setFitness(calculateFitness(individual, optimizationScenario, minObjectiveValue, maxObjectiveValue));
        }
        return population;
    }

    public static double calculateObjectiveValue(Individual individual) {
        individual.calculateTotal();
        double objectiveValue = individual.getTotalCost();
        return objectiveValue;
    }

    public static double calculateFitness(Individual individual, OptimizationScenario optimizationScenario, double minObjectiveValue, double maxObjectiveValue) {
        double difference = maxObjectiveValue - individual.getObjectiveValue();
        double fitness = Math.pow(difference, optimizationScenario.getExponentialFactor()) - minObjectiveValue;
        return fitness;
    }

    public static List<Individual> setElites(List<Individual> currentPopulation, List<Individual> elites, int eliteSize) {
        elites.clear();
        for (Individual individual : currentPopulation) {
            if (elites.isEmpty())
                elites.add(individual);
            else {
                if (!individual.hasSameRoutes(elites.get(elites.size() - 1)))
                    elites.add(individual);
            }
            if (elites.size() == eliteSize)
                break;
        }
        return elites;
    }

}