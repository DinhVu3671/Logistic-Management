package com.vrp.demo.models;

import com.vrp.demo.models.enu.NodeType;
import com.vrptwga.concepts.Correlation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CorrelationModel extends BaseModel{

    private Long fromNodeId;
    private String fromNodeCode;
    private String fromNodeName;
    private NodeType fromNodeType;
    private Long toNodeId;
    private String toNodeCode;
    private String toNodeName;
    private NodeType toNodeType;
    private int distance;
    private int time;
    private double riskProbability;

    public static Correlation getCorrelationInput(CorrelationModel correlationModel){
        Correlation correlation = new Correlation();
        correlation.setFromNodeCode(correlationModel.getFromNodeCode());
        correlation.setToNodeCode(correlationModel.getToNodeCode());
        correlation.setDistance(correlationModel.getDistance());
        correlation.setTime(correlationModel.getTime());
        correlation.setRiskProbability(correlationModel.getRiskProbability());
        return correlation;
    }

}
