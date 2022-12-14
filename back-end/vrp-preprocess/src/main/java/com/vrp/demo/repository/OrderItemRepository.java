package com.vrp.demo.repository;

import com.vrp.demo.entity.tenant.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderItemRepository extends BaseRepository<OrderItem, Long> {

    public List<OrderItem> create(List<OrderItem> orderItems);

    public Map<String, Object> stat(String startDate, String endDate);
}
