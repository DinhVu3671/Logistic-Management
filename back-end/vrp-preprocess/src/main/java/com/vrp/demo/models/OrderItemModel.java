package com.vrp.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vrp.demo.entity.tenant.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemModel extends BaseModel{

    private String code;
    private OrderModel order;
    private ProductModel product;
    private long quantity;
    private double price;
    private double weight;
    private double capacity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ReturnOrderModel returnOrder;

    public static OrderItem convertToEntity(OrderItemModel orderItemModel) {
        ModelMapper modelMapper = new ModelMapper();
        OrderItem orderItem = modelMapper.map(orderItemModel, OrderItem.class);
        return orderItem;
    }

    public boolean isExcludeOrderItem(OrderItemModel orderItem) {
        for (ProductModel excludingProduct : product.getExcludingProducts()) {
            if (excludingProduct.getId() == orderItem.getProduct().getId())
                return true;
        }
        return false;
    }
    
}
