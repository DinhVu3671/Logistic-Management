package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.search.OrderSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService extends BaseService<Order, Long> {

    public List<OrderModel> find(OrderSearch search);

    public Page<OrderModel> search(OrderSearch search);

    public OrderModel create(OrderModel orderModel) throws CustomException;

    public OrderModel update(OrderModel orderModel) throws CustomException;

    public int delete(Long id) throws CustomException;

    public OrderModel findOne(Long id);

    public void createOrderData() throws CustomException;

}
