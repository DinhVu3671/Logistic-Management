package com.vrp.demo.models.solution;

import com.vrp.demo.models.*;
import com.vrptwga.concepts.OptimizationScenario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Document("problem_assumption")
public class ProblemAssumption {

//    @Id
    private String id;
    private List<List<Long>> timeMatrix = new ArrayList<>();
    private List<List<Long>> distanceMatrix = new ArrayList<>();
    private List<NodeModel> nodes = new ArrayList<>();
    private List<VehicleModel> vehicles = new ArrayList<>();
    private List<OrderModel> orders = new ArrayList<>();
    private List<CustomerModel> customers = new ArrayList<>();
    private List<DepotModel> depots = new ArrayList<>();
    //constrain information

    private Boolean isExcludeProduct = true;
    private Boolean isLimitedTime = false;
    private Double maxTravelTime;
    private Boolean isLimitedDistance = false;
    private Boolean isAllowedViolateTW = false;
    private Double maxDistance;
    private Double maxTime = 2d;
    private Integer popSize = 250;
    private Integer eliteSize = 10;
    private Double eliteRate = 13d;
    private Integer maxGen = 200;
    private Integer maxGenImprove = 100;
    private Float probCrossover = 0.96f;
    private Float probMutation = 0.16f;
    private Integer tournamentSize = 20;
    private Integer selectionSize = 40;
    private Integer exponentialFactor =3;

    public static OptimizationScenario setParameters(OptimizationScenario optimizationScenario, ProblemAssumption problemAssumption) {
        optimizationScenario.setExcludeProduct(problemAssumption.getIsExcludeProduct());
        optimizationScenario.setLimitedTime(problemAssumption.getIsLimitedTime());
        if (problemAssumption.getIsLimitedTime())
            optimizationScenario.setMaxTravelTime((int) (problemAssumption.getMaxTravelTime() * 3600));
        optimizationScenario.setLimitedDistance(problemAssumption.getIsLimitedDistance());
        if (problemAssumption.getIsLimitedDistance())
            optimizationScenario.setMaxDistance((int) (problemAssumption.getMaxDistance() * 1000));
        optimizationScenario.setTime((int) (problemAssumption.getMaxTime() * 60 * 1000));
        optimizationScenario.setPopSize(problemAssumption.getPopSize());
        optimizationScenario.setEliteSize((int) (problemAssumption.getEliteRate() * problemAssumption.getPopSize() / 100));
        optimizationScenario.setEliteRate(problemAssumption.getEliteRate());
        optimizationScenario.setMaxGen(problemAssumption.getMaxGen());
        optimizationScenario.setImprove(problemAssumption.getMaxGenImprove());
        optimizationScenario.setProbCrossover(problemAssumption.getProbCrossover());
        optimizationScenario.setProbMutation(problemAssumption.getProbMutation());
        optimizationScenario.setTournamentSize(problemAssumption.getTournamentSize());
        optimizationScenario.setSelectionSize(problemAssumption.getSelectionSize());
        optimizationScenario.setAllowedViolateTW(problemAssumption.getIsAllowedViolateTW());
        optimizationScenario.setExponentialFactor(problemAssumption.getExponentialFactor());
        return optimizationScenario;
    }

    public void setObjectId() {
        String id = System.currentTimeMillis() + "";
        setId(getId() == null ? id : id + getId());
    }
}
