package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.entity.tenant.OrderItem;
import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.entity.tenant.ReturnOrder;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.OrderItemModel;

import java.util.List;

public interface OrderItemService extends BaseService<OrderItem, Long> {

    public OrderItem create(OrderItemModel orderItemModel) throws CustomException;

    public List<OrderItem> create(List<OrderItemModel> orderItemModels) throws CustomException;

    public OrderItem update(OrderItemModel orderItemModel) throws CustomException;

    public List<OrderItem> update(List<OrderItem> orderItems) throws CustomException;

    public int delete(Long id) throws CustomException;

    public OrderItemModel findOne(Long id);

    public List<OrderItemModel> getByOrder(Order order);

    public int deleteByOrder(Order order) throws CustomException;

    public int deleteByReturnOrder(ReturnOrder returnOrder) throws CustomException;

    public void updateOrderItems(Product product) throws CustomException;

}
