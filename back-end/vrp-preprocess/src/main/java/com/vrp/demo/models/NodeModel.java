package com.vrp.demo.models;

import com.vrp.demo.models.enu.NodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NodeModel extends BaseModel implements Cloneable {

    private String code;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Long startTime;
    private Long endTime;
    private List<CorrelationModel> correlations;
    private NodeType nodeType;

    public static NodeModel findNode(List<NodeModel> nodeModels, Long idNode, NodeType nodeType) {
        NodeModel node = null;
        for (NodeModel nodeModel : nodeModels) {
            if (nodeModel.getId() == idNode && nodeModel.getNodeType().equals(nodeType)){
                node = nodeModel;
//                node.setCorrelations(null);
            }
        }
        return node;
    }

    @Override
    public NodeModel clone() {
        NodeModel clone = null;
        try {
            clone = (NodeModel) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clone;
    }

}
