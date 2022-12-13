package com.vrptwga;

import com.vrptwga.algorithms.GeneticAlgorithm;
import com.vrptwga.representation.Individual;
import com.vrptwga.concepts.OptimizationScenario;

import java.util.ArrayList;
import java.util.List;

public class MainAlgorithm {

    public static List<Individual> runAlgorithm(OptimizationScenario optimizationScenario) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
//        geneticAlgorithm.run(optimizationScenario);
        geneticAlgorithm.runAlgorithm(optimizationScenario);
        List<Individual> solutions = geneticAlgorithm.getResults();
        return solutions;
    }
}
