package com.vrp.demo.entity.tenant;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.models.CorrelationModel;
import com.vrp.demo.models.enu.NodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "correlations")
public class Correlation extends BaseEntity {

    @Column(name = "from_node_id")
    private Long fromNodeId;
    @Column(name = "from_node_code")
    private String fromNodeCode;
    @Column(name = "from_node_name")
    private String fromNodeName;
    @Column(name = "from_node_type")
    @Enumerated(EnumType.STRING)
    private NodeType fromNodeType;
    @Column(name = "to_node_id")
    private Long toNodeId;
    @Column(name = "to_node_code")
    private String toNodeCode;
    @Column(name = "to_node_name")
    private String toNodeName;
    @Column(name = "to_node_type")
    @Enumerated(EnumType.STRING)
    private NodeType toNodeType;
    private int distance;
    private int time;
    @Column(name = "risk_probability")
    private double riskProbability;

    public static CorrelationModel convertToModel(Correlation correlation) {
        ModelMapper modelMapper = new ModelMapper();
        CorrelationModel correlationModel = modelMapper.map(correlation, CorrelationModel.class);
        return correlationModel;
    }

    public Correlation updateCorrelation(CorrelationModel correlationModel) {
        this.setDistance(correlationModel.getDistance());
        this.setTime(correlationModel.getTime());
        this.setRiskProbability(correlationModel.getRiskProbability());
        return this;
    }

}
