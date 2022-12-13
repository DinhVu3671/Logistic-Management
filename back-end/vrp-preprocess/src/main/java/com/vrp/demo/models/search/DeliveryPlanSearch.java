package com.vrp.demo.models.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPlanSearch extends BaseSearch{

    private String name;
    private Date intendReceiveTime;

}
