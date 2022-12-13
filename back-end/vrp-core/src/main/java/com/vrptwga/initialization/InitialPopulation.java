package com.vrptwga.initialization;

import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.representation.Individual;
import com.vrptwga.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class InitialPopulation {

    public static List<Individual> initPop(OptimizationScenario optimizationScenario) {
        List<Individual> initPop = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < optimizationScenario.getPopSize(); i++) {
            // Khoi tao ca the (khoi tao den khi duoc thi thoi). Neu khong thi ca the = null.
            Individual individual = Individual.createIndividual(optimizationScenario);
            if (individual == null) {
                individual = Individual.createInsteadIndividual(optimizationScenario);
                if (individual == null)
                    break;
            }
            initPop.add(individual);
            // Neu quan thoi gian cho phep khoi tao => Terminate
            if (CommonUtils.checkOverTime(startTime, optimizationScenario.getTime()))
                break;
            System.out.println("****\n" + individual + "****");
        }
        if (initPop.size() < 1)
            throw new NullPointerException();
        // Neu ma chua du so luong => Khoi tao ngau nhien Individual bang cach clone lai 1 thang individual trong
            // list quan the da tao
        else if (initPop.size() < optimizationScenario.getPopSize())
            for (int i = 0; i < optimizationScenario.getPopSize() - initPop.size(); i++) {
                Random random = new Random();
                initPop.add(Individual.clone(initPop.get(random.nextInt(initPop.size()))));
            }
        return initPop;
    }

}


