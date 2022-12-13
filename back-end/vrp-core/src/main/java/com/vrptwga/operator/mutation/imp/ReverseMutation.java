package com.vrptwga.operator.mutation.imp;

import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.operator.mutation.Mutation;
import com.vrptwga.representation.Individual;
import com.vrptwga.representation.Population;

import java.util.ArrayList;
import java.util.List;

public class ReverseMutation extends Mutation {

    @Override
    public List<Individual> execute() {
        List<Individual> currentPopulation = ((Population) this.parameters.get("population")).getIndividuals();
        OptimizationScenario optimizationScenario = (OptimizationScenario) this.parameters.get("optimizationScenario");
        double randomMutationRate = 0;
        List<Individual> resultOfMutation = new ArrayList<>();
        List<Individual> originalIndividuals = new ArrayList<>();
        originalIndividuals.addAll(currentPopulation);
        for (Individual originalIndividual : originalIndividuals) {
            randomMutationRate = Math.random();
            if (randomMutationRate < optimizationScenario.getProbMutation()) {
                Individual mutateIndividual = Individual.optimizeRandomJourney(originalIndividual, optimizationScenario);
                mutateIndividual.calculateTravelingTime();
                resultOfMutation.add(mutateIndividual);
            }
        }
//        for (Individual individual : resultOfMutation) {
//            individual.checkValidation(optimizationScenario);
//        }
        return resultOfMutation;
    }
}
