package com.vrp.demo.models;

import com.vrp.demo.entity.tenant.ReturnOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderModel extends BaseModel{

    private OrderModel order;
    private double weight;
    private double capacity;
    private List<OrderItemModel> orderItems;

    public static ReturnOrder convertToEntity(ReturnOrderModel returnOrderModel) {
        ModelMapper modelMapper = new ModelMapper();
        ReturnOrder returnOrder = modelMapper.map(returnOrderModel, ReturnOrder.class);
        return returnOrder;
    }
}
