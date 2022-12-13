package com.vrptwga.operator.selector.imp;

import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.operator.selector.Selector;
import com.vrptwga.representation.Individual;
import com.vrptwga.representation.Population;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomSelector extends Selector {

    @Override
    public List<Individual> execute() {
        List<Individual> population = ((Population) this.parameters.get("population")).getIndividuals();
        OptimizationScenario optimizationScenario = (OptimizationScenario) this.parameters.get("optimizationScenario");
        int populationSize = optimizationScenario.getPopSize() - optimizationScenario.getEliteSize() - optimizationScenario.getSelectionSize();
        Random random = new Random();
        List<Individual> randomIndividuals = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            randomIndividuals.add(population.get(random.nextInt(population.size())));
        }
        return randomIndividuals;
    }

    public RandomSelector(HashMap<String, Object> parameter) {
        this.parameters = parameter;
    }

    public RandomSelector() {
    }
}
