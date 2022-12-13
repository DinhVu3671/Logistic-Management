package com.vrp.demo.repository;

import com.vrp.demo.entity.tenant.OrderItem;

import java.util.List;

public interface OrderItemRepository extends BaseRepository<OrderItem, Long> {

    public List<OrderItem> create(List<OrderItem> orderItems);
}
