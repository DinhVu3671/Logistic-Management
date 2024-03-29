package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.dashboard.SalesModels;
import com.vrp.demo.models.orders.OrderModelCreate;
import com.vrp.demo.models.search.OrderSearch;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;

public interface OrderService extends BaseService<Order, Long> {

    public List<OrderModel> find(OrderSearch search);

    public Page<OrderModel> search(OrderSearch search);

    public OrderModel create(OrderModelCreate orderModelCreate) throws CustomException;
    public OrderModel create(OrderModel orderModel) throws CustomException;
    public OrderModel update(OrderModel orderModel) throws CustomException;

    public int delete(Long id) throws CustomException;

    public OrderModel findOne(Long id);

    public void createOrderData() throws CustomException;
    public List<SalesModels> searchByYear(String year) throws ParseException;
    public List<OrderModel> getOrdersByCustomer(Long userId);

}
