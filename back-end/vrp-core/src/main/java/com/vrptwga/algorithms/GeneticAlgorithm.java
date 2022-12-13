package com.vrptwga.algorithms;

import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.evaluate.Evaluate;
import com.vrptwga.initialization.InitialPopulation;
import com.vrptwga.operator.Operator;
import com.vrptwga.operator.crossover.imp.BestCostCrossover;
import com.vrptwga.operator.mutation.imp.ReverseMutation;
import com.vrptwga.operator.selector.imp.FitnessProportionateSelector;
import com.vrptwga.operator.selector.imp.RandomSelector;
import com.vrptwga.representation.Individual;
import com.vrptwga.representation.Population;
import com.vrptwga.representation.genotype.Genotype;
import com.vrptwga.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class GeneticAlgorithm {

    private List<Individual> elites = new ArrayList<>();
    private List<Individual> currentPopulation = new ArrayList<>();
    private int currentGen = 0;
    int notImproveCount = 0;
    private Population population = new Population();
    private Operator randomSelector = new RandomSelector();
    private Operator fitnessProportionateSelector = new FitnessProportionateSelector();
    private Operator reverseMutation = new ReverseMutation();
    private Operator bestCostCrossover = new BestCostCrossover();

    private List<Individual> randomSelection(Population population, OptimizationScenario optimizationScenario) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("population", population);
        params.put("optimizationScenario", optimizationScenario);
        this.randomSelector.setParameters(params);
        return this.randomSelector.execute();
    }

    private List<Individual> fitnessProportionateSelection(Population population, OptimizationScenario optimizationScenario) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("population", population);
        params.put("tournamentSize", optimizationScenario.getTournamentSize());
        params.put("selectionSize", optimizationScenario.getSelectionSize());
        this.fitnessProportionateSelector.setParameters(params);
        return this.fitnessProportionateSelector.execute();
    }

    private List<Individual> bestCostRouteCrossover(Population population, OptimizationScenario optimizationScenario) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("population", population);
        params.put("optimizationScenario", optimizationScenario);
        this.bestCostCrossover.setParameters(params);
        return this.bestCostCrossover.execute();
    }

    private List<Individual> reverseMutate(Population population, OptimizationScenario optimizationScenario) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("population", population);
        params.put("optimizationScenario", optimizationScenario);
        this.reverseMutation.setParameters(params);
        return this.reverseMutation.execute();
    }

    public void runAlgorithm(OptimizationScenario optimizationScenario) {
        // Tao file excel luu ket qua chay
        String experimentLogPath = CommonUtils.createExperimentDirectory();
        // Record lai thoi gian chay thuan toan
        long startTime = System.currentTimeMillis();
        long executedTime = 0;
        // Khoi tao quan the
        List<Individual> initializationPopulations = InitialPopulation.initPop(optimizationScenario);
        List<Individual> currentPopulation = new ArrayList<>();
        setCurrentPopulation(initializationPopulations);
        Population population = new Population();
        List<Individual> firstElites = new ArrayList<>();
        // Danh gia quan the ban dau
        Evaluate.weightedSumMethod(initializationPopulations, optimizationScenario);
        Evaluate.setElites(initializationPopulations, firstElites, optimizationScenario.getEliteSize());
        population.setIndividuals(initializationPopulations);
        population.setElites(firstElites);
        population.setCurrentGeneration(0);
        this.setCurrentPopulation(initializationPopulations);
        this.setElites(firstElites);
        // write to file
        CommonUtils.writeValuePopulation(population,experimentLogPath);
        CommonUtils.writeParameterConfigHeader(optimizationScenario,experimentLogPath);
        // Bat dau chay cac the he
        for (int i = 0; i < optimizationScenario.getMaxGen(); i++) {
            // Termination 1: Over time set limitation.
            if (isOverTime(startTime, optimizationScenario))
                break;
            // Increase generation index
            setCurrentGen(i + 1);
            population.setCurrentGeneration(i + 1);
            List<Individual> elites = new ArrayList<>();
            // Crossover
            List<Individual> childOfCrossover = bestCostRouteCrossover(population, optimizationScenario);
            // Mutation
            List<Individual> mutateIndividuals = reverseMutate(population, optimizationScenario);
            currentPopulation.addAll(getCurrentPopulation());
            currentPopulation.addAll(childOfCrossover);
            currentPopulation.addAll(mutateIndividuals);
            // Continue evaluating fitness
            Evaluate.weightedSumMethod(currentPopulation, optimizationScenario);
            Evaluate.setElites(currentPopulation, elites, optimizationScenario.getEliteSize());
            population.setIndividuals(currentPopulation);
            population.setElites(elites);
            // Log to file excel
            CommonUtils.writeValuePopulation(population, experimentLogPath);
            List<Individual> selectionIndividuals = fitnessProportionateSelection(population, optimizationScenario);
            List<Individual> randomSelectionIndividuals = randomSelection(population, optimizationScenario);
            currentPopulation.clear();
            currentPopulation.addAll(elites);
            currentPopulation.addAll(selectionIndividuals);
            currentPopulation.addAll(randomSelectionIndividuals);
            // Increase notImprove Generation
            if (i != 0)
                if (elites.get(0).getObjectiveValue() == getElites().get(0).getObjectiveValue())
                    this.setNotImproveCount(this.getNotImproveCount() + 1);
                else
                    this.setNotImproveCount(0);

            this.setCurrentPopulation(currentPopulation);
            this.setElites(elites);
            population.setIndividuals(currentPopulation);
            population.setElites(elites);
            this.setPopulation(population);
            // Termination condition 2: Not improve after some times
            if (this.getNotImproveCount() == optimizationScenario.getImprove()) {
                System.err.println("Improve Constraint!!!!! === Gen:" + (i + 1));
                break;
            }
            executedTime = System.currentTimeMillis() - startTime;
        }
        // Termination 3: After maxGen. => Print results.
//        System.out.println("Completed!!!");
        executedTime = System.currentTimeMillis() - startTime;
        String fitnessSolution = this.elites.get(0).getFitnessString();
        System.out.println("executionTime:" + executedTime + " ms");
        System.err.println("########################");
        System.err.println("Run Algorithm Completed!");
        System.err.println("executionTime: " + executedTime + " ms");
        System.err.println("currentGen: " + this.currentGen);
        System.err.println("Solution: " + this.elites.get(0).toString());
        System.err.println("########################");
        System.err.println("########################");
        CommonUtils.writeValue(population,experimentLogPath);
//        this.elites = improveSolutions(this.elites, optimizationScenario);
    }

    public static List<Individual> improveSolutions(GeneticAlgorithm geneticAlgorithm, OptimizationScenario optimizationScenario) {
        List<Individual> elites = new ArrayList<>();
        for (Individual solution : geneticAlgorithm.getElites()) {
            solution = Individual.optimizeJourneys(solution, optimizationScenario);
//            solution.changeFitVehicles();
            solution.setGenotype(Genotype.enCodingScheme(solution, optimizationScenario));
            elites.add(solution);

        }
        elites.sort(Comparator.comparingDouble(Individual::getObjectiveValue));
        geneticAlgorithm.setElites(elites);
        return geneticAlgorithm.getElites();
    }

    public void run(OptimizationScenario optimizationScenario) {
        long startTime = System.currentTimeMillis();
        long executedTime = 0;
        // khoi tao quan the
        List<Individual> initializationPopulations = InitialPopulation.initPop(optimizationScenario);
        List<Individual> currentPopulation = new ArrayList<>();
        setCurrentPopulation(initializationPopulations);
        Population population = new Population();
        population.setIndividuals(currentPopulation);
        List<Individual> firstElites = new ArrayList<>();
        Evaluate.weightedSumMethod(initializationPopulations, optimizationScenario);
        Evaluate.setElites(initializationPopulations, firstElites, optimizationScenario.getEliteSize());
        this.setCurrentPopulation(initializationPopulations);
        this.setElites(firstElites);
        for (int i = 0; i < optimizationScenario.getMaxGen(); i++) {
            if (isOverTime(startTime, optimizationScenario))
                break;
            setCurrentGen(i + 1);
            population.setCurrentGeneration(i + 1);
            List<Individual> elites = new ArrayList<>();
            List<Individual> childOfCrossover = bestCostRouteCrossover(population, optimizationScenario);
            List<Individual> mutateIndividuals = reverseMutate(population, optimizationScenario);
            currentPopulation.addAll(getCurrentPopulation());
            currentPopulation.addAll(childOfCrossover);
            currentPopulation.addAll(mutateIndividuals);
            Evaluate.weightedSumMethod(currentPopulation, optimizationScenario);
            Evaluate.setElites(currentPopulation, elites, optimizationScenario.getEliteSize());
            population.setIndividuals(currentPopulation);
            List<Individual> selectionIndividuals = fitnessProportionateSelection(population, optimizationScenario);
            List<Individual> randomSelectionIndividuals = randomSelection(population, optimizationScenario);
            currentPopulation.clear();
            currentPopulation.addAll(elites);
            currentPopulation.addAll(selectionIndividuals);
            currentPopulation.addAll(randomSelectionIndividuals);
            if (i != 0)
                if (elites.get(0).getObjectiveValue() == getElites().get(0).getObjectiveValue())
                    this.setNotImproveCount(this.getNotImproveCount() + 1);
                else
                    this.setNotImproveCount(0);
            this.setCurrentPopulation(currentPopulation);
            this.setElites(elites);
            population.setIndividuals(currentPopulation);
            population.setElites(elites);
            this.setPopulation(population);
            if (this.getNotImproveCount() == optimizationScenario.getImprove()) {
                System.err.println("Improve Constraint!!!!! === Gen:" + (i + 1));
                break;
            }
            executedTime = System.currentTimeMillis() - startTime;
        }
//        System.out.println("Completed!!!");
        executedTime = System.currentTimeMillis() - startTime;
        String fitnessSolution = this.elites.get(0).getFitnessString();
        System.out.println("executionTime:" + executedTime + " ms");
        System.err.println("########################");
        System.err.println("Run Algorithm Completed!");
        System.err.println("executionTime: " + executedTime + " ms");
        System.err.println("currentGen: " + this.currentGen);
        System.err.println("Solution: " + this.elites.get(0).toString());
        System.err.println("########################");
        System.err.println("########################");
//        this.elites = improveSolutions(this.elites, optimizationScenario);
    }

    public List<Individual> improveSolutions(List<Individual> elites, OptimizationScenario optimizationScenario) {
        List<Individual> improvedElites = new ArrayList<>();
        for (Individual solution : elites) {
            solution = Individual.optimizeJourneys(solution, optimizationScenario);
            solution.setGenotype(Genotype.enCodingScheme(solution, optimizationScenario));
            improvedElites.add(solution);
        }
        elites.sort(Comparator.comparingDouble(Individual::getObjectiveValue));
        return improvedElites;
    }

    public List<Individual> getResults() {
        return this.elites;
    }

    public int getCurrentGen() {
        return currentGen;
    }

    public void setCurrentGen(int currentGen) {
        this.currentGen = currentGen;
    }

    public List<Individual> getElites() {
        return population.getElites();
    }

    public void setElites(List<Individual> elites) {
        this.elites = elites;
    }

    public List<Individual> getCurrentPopulation() {
        return currentPopulation;
    }

    public void setCurrentPopulation(List<Individual> currentPopulation) {
        this.currentPopulation = currentPopulation;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public int getNotImproveCount() {
        return notImproveCount;
    }

    public void setNotImproveCount(int notImproveCount) {
        this.notImproveCount = notImproveCount;
    }

    private static boolean isOverTime(long startTime, OptimizationScenario optimizationScenario) {
        long executedTime = System.currentTimeMillis() - startTime;
        return executedTime > optimizationScenario.getTime();
    }


}
