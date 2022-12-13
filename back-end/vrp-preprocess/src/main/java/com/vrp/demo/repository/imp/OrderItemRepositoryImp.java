package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.OrderItem;
import com.vrp.demo.repository.OrderItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "orderItemRepository")
public class OrderItemRepositoryImp extends BaseRepositoryImp<OrderItem,Long> implements OrderItemRepository {

    @Override
    public List<OrderItem> create(List<OrderItem> orderItems){
        for (OrderItem orderItem : orderItems) {
            orderItem = insert(orderItem);
        }
        return orderItems;
    }

    public OrderItemRepositoryImp() {
        super(OrderItem.class);
    }

}
