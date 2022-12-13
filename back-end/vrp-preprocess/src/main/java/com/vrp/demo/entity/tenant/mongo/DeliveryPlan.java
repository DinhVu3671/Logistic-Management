package com.vrp.demo.entity.tenant.mongo;

import com.vrp.demo.configuration.mongodb.CascadeSave;
import com.vrp.demo.models.solution.ProblemAssumption;
import com.vrp.demo.models.solution.Solution;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("delivery_plan")
public class DeliveryPlan {

    @Id
    private Long id;
    private String name;
//    @DBRef
//    @CascadeSave
    private Solution solution;
//    @DBRef
//    @CascadeSave
    private ProblemAssumption problemAssumption;
    @Field(value = "intend_receive_time")
    private Date intendReceiveTime;
    @Field(name = "created_at")
    private Date createdAt;
    @Field(name = "updated_at")
    private Date updatedAt;
    @Field(name = "is_selected")
    private Boolean isSelected = false;

}
