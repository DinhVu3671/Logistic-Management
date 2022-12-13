package com.vrp.demo.entity.tenant;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.models.OrderItemModel;
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
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private long quantity;
    private double price;
    private double weight;
    private double capacity;
    @ManyToOne
    @JoinColumn(name = "return_order_id")
    private ReturnOrder returnOrder;

    public static OrderItemModel convertToModel(OrderItem orderItem) {
        ModelMapper modelMapper = new ModelMapper();
        OrderItemModel orderItemModel = modelMapper.map(orderItem, OrderItemModel.class);
        return orderItemModel;
    }

    public OrderItem updateOrderItem(OrderItemModel orderItemModel) {
        this.setQuantity(orderItemModel.getQuantity());
        this.calculateTotal();
        return this;
    }

    public void calculateTotal() {
        this.price = Math.round(this.product.getPrice() * this.quantity * 100.0) / 100.0;
        this.weight = Math.round(this.product.getWeight() * this.quantity * 100.0) / 100.0;
        this.capacity = Math.round(this.product.getCapacity() * this.quantity * 100.0) / 100.0;
    }
}
