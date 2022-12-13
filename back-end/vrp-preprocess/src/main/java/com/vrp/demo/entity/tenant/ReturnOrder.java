package com.vrp.demo.entity.tenant;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.models.OrderItemModel;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.ReturnOrderModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "return_orders")
public class ReturnOrder extends BaseEntity {

    @OneToOne
    private Order order;
    private double weight;
    private double capacity;
    @OneToMany(mappedBy = "returnOrder")
    private List<OrderItem> orderItems;

    public static ReturnOrderModel convertToModel(ReturnOrder returnOrder) {
        ModelMapper modelMapper = new ModelMapper();
        ReturnOrderModel returnOrderModel = modelMapper.map(returnOrder, ReturnOrderModel.class);
        List<OrderItemModel> orderItemModels = new ArrayList<>();
        for (OrderItem orderItem : returnOrder.getOrderItems()) {
            ReturnOrderModel model = new ReturnOrderModel();
            model.setId(returnOrder.getId());
            OrderItemModel orderItemModel = OrderItem.convertToModel(orderItem);
            orderItemModel.getProduct().ignoreRecursiveExcludingProducts();
            orderItemModel.setReturnOrder(model);
            orderItemModels.add(orderItemModel);
        }
        returnOrderModel.setOrderItems(orderItemModels);
        OrderModel orderModel = Order.convertToModel(returnOrder.getOrder());
        returnOrderModel.setOrder(orderModel);
        return returnOrderModel;
    }

    public void calculateTotal() {
        double totalWeight = 0;
        double totalCapacity = 0;
        for (OrderItem orderItem : orderItems) {
            totalWeight += orderItem.getWeight();
            totalCapacity += orderItem.getCapacity();
        }
        this.weight = Math.round(totalWeight * 100.0) / 100.0;
        this.capacity = Math.round(totalCapacity * 100.0) / 100.0;
    }

    public ReturnOrder updateReturnOrder(ReturnOrderModel returnOrderModel) {
        this.setWeight(returnOrderModel.getWeight());
        this.setCapacity(returnOrderModel.getCapacity());
        return this;
    }
}
