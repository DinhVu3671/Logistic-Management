package com.vrp.demo.models.solution;

import com.vrp.demo.models.CorrelationModel;
import com.vrp.demo.models.NodeModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@Document("arc")
public class Arc {
//    @Id
    private String id;
    private NodeModel fromNode;
    private NodeModel toNode;
    double distance;
    Integer start;
    Integer end;

    public Arc(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public Arc(NodeModel fromNode, NodeModel toNode) {
        this.fromNode = fromNode.clone();
        this.toNode = toNode.clone();
        for (CorrelationModel correlation : fromNode.getCorrelations()) {
            if (correlation.getToNodeCode().equals(toNode.getCode()))
                this.distance = correlation.getDistance();
        }
        this.fromNode.setCorrelations(null);
        this.toNode.setCorrelations(null);
    }

    public void setObjectId() {
        String id = System.currentTimeMillis() + "" + this.hashCode();
        setId(getId() == null ? id : id + getId());
    }
}
