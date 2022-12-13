package com.vrp.demo.models;

import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.models.enu.NodeType;
import com.vrptwga.concepts.Correlation;
import com.vrptwga.concepts.Depot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepotModel extends NodeModel {

    private double unloadingCost;
    private List<ProductModel> products;

    public static com.vrp.demo.entity.tenant.Depot convertToEntity(DepotModel depotModel) {
        ModelMapper modelMapper = new ModelMapper();
        com.vrp.demo.entity.tenant.Depot depot = modelMapper.map(depotModel, com.vrp.demo.entity.tenant.Depot.class);
        return depot;
    }

    public static Depot getDepotInput(DepotModel depot) {
        Depot depotInput = new Depot();
        depotInput.setId(depot.getId().intValue());
        depotInput.setCode(depot.getCode());
        depotInput.setStartTime(depot.getStartTime().intValue());
        depotInput.setEndTime(depot.getEndTime().intValue());
        depotInput.setUnloadingCost(depot.getUnloadingCost());
        HashMap<Integer,Double> distanceCustomers = new HashMap<>();
        HashMap<Integer,Double> riskProbabilityCustomers = new HashMap<>();
        HashMap<String, Correlation> correlations = new HashMap<>();
        for (CorrelationModel correlation : depot.getCorrelations()) {
            if (correlation.getToNodeType().equals(NodeType.CUSTOMER)){
                Correlation correlationInput = CorrelationModel.getCorrelationInput(correlation);
                correlations.put(correlationInput.getToNodeCode(),correlationInput);
                distanceCustomers.put(correlation.getToNodeId().intValue(),new Double(correlation.getTime()));
                riskProbabilityCustomers.put(correlation.getToNodeId().intValue(),correlation.getRiskProbability());
            }
        }
        depotInput.setCorrelations(correlations);
        depotInput.setDistanceCustomers(distanceCustomers);
        depotInput.setRiskProbabilityCustomers(riskProbabilityCustomers);
        return depotInput;
    }

}
