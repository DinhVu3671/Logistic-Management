package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.repository.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "orderRepository")
public class OrderRepositoryImp extends BaseRepositoryImp<Order,Long> implements OrderRepository {

    public OrderRepositoryImp() {
        super(Order.class);
    }

}