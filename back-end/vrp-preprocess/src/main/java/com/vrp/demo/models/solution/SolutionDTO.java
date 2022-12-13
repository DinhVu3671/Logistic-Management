package com.vrp.demo.models.solution;

import com.vrp.demo.models.OrderModel;
import com.vrptwga.concepts.OptimizationScenario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolutionDTO {

    private Solution solution;
    private OrderModel changedOrder;
    private Journey toJourney;
    private Journey fromJourney;
    private ProblemAssumption problemAssumption;
    private OptimizationScenario optimizationScenario;

}
