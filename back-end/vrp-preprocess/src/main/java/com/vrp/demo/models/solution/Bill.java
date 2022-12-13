package com.vrp.demo.models.solution;

import com.vrp.demo.models.OrderModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    private double totalAmount;
    private double unloadingFee;
    private double orderValue;
    private OrderModel order;
    private double capacityAfter;
    private double loadWeightAfter;
    private double fillRateLoadWeight;
    private double fillRateCapacity;
    private double penaltyCost;

}
