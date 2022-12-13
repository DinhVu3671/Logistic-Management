package com.vrptwga.operator.selector.imp;

import com.vrptwga.operator.selector.Selector;
import com.vrptwga.representation.Individual;
import com.vrptwga.representation.Population;

import java.util.*;

public class FitnessProportionateSelector extends Selector {

    @Override
    public List<Individual> execute() {
        List<Individual> currentPopulation = ((Population) this.parameters.get("population")).getIndividuals();
        int tournamentSize = (int) this.parameters.get("tournamentSize");
        int selectionSize = (int) this.parameters.get("selectionSize");
        List<Individual> tournamentIndividuals = new ArrayList<>();
        for (int i = 0; i < selectionSize; i++) {
            List<Individual> selectedIndividuals = new ArrayList<>();
            List<Float> fitnessList = new ArrayList<>();
            List<Float> probabilities = new ArrayList<>();
            Random randomSelected = new Random();
            float positive_infinity = 999999999;
            float sumFitness = 0;
            for (int j = 0; j < tournamentSize; j++) {
                Individual randomIndividual = currentPopulation.get(randomSelected.nextInt(currentPopulation.size()));
                selectedIndividuals.add(randomIndividual);
                sumFitness += (positive_infinity - randomIndividual.getObjectiveValue());
                fitnessList.add(sumFitness);
            }
            for (int j = 0; j < tournamentSize; j++) {
                probabilities.add(fitnessList.get(j) / sumFitness);
            }
            float randomProbability = randomSelected.nextFloat();

            if (randomProbability < probabilities.get(0))
                tournamentIndividuals.add(selectedIndividuals.get(0));
            else {
                for (int j = 1; j < probabilities.size(); j++) {
                    if (probabilities.get(j - 1) < randomProbability && randomProbability < probabilities.get(j)) {
                        tournamentIndividuals.add(selectedIndividuals.get(j));
                        break;
                    }
                }
            }
        }
        tournamentIndividuals.sort(Comparator.comparingDouble(Individual::getObjectiveValue));
        return tournamentIndividuals;
    }

    public FitnessProportionateSelector(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

    public FitnessProportionateSelector() {
    }
}
