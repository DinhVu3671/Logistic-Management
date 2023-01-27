package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.entity.tenant.OrderItem;
import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.entity.tenant.ReturnOrder;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.OrderItemModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.repository.OrderItemRepository;
import com.vrp.demo.repository.OrderRepository;
import com.vrp.demo.repository.ProductRepository;
import com.vrp.demo.service.OrderItemService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("orderItemService")
public class OrderItemServiceImp extends BaseServiceImp<OrderItemRepository, OrderItem, Long> implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = false)
    public OrderItem create(OrderItemModel orderItemModel) throws CustomException {
        Product product = productRepository.find(orderItemModel.getProduct().getId());
        if (product == null)
            throw CommonUtils.createException(Code.PRODUCT_ID_NOT_EXISTED);
        Order order = orderRepository.find(orderItemModel.getOrder().getId());
        if (order == null)
            throw CommonUtils.createException(Code.ORDER_ID_NOT_EXISTED);
        OrderItem orderItem = OrderItemModel.convertToEntity(orderItemModel);
        orderItem.setProduct(product);
        orderItem.calculateTotal();
        orderItem.setOrder(order);
        orderItem = create(orderItem);
        return orderItem;
    }

    @Override
    @Transactional(readOnly = false)
    public List<OrderItem> create(List<OrderItemModel> orderItemModels) throws CustomException {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemModel orderItemModel : orderItemModels) {
            orderItems.add(create(orderItemModel));
        }
        return orderItems;
    }

    @Override
    @Transactional(readOnly = false)
    public List<OrderItem> update(List<OrderItem> orderItems) throws CustomException {
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            orderItem = update(OrderItem.convertToModel(orderItem));
        }
        return orderItems;
    }

    @Override
    @Transactional(readOnly = false)
    public OrderItem update(OrderItemModel orderItemModel) throws CustomException {
        OrderItem orderItem = orderItemRepository.find(orderItemModel.getId());
        if (orderItem == null)
            throw CommonUtils.createException(Code.ORDER_ITEM_ID_NOT_EXISTED);
        Product product = productRepository.find(orderItemModel.getProduct().getId());
        if (product == null)
            throw CommonUtils.createException(Code.PRODUCT_ID_NOT_EXISTED);
        orderItem.setProduct(product);
        orderItem = orderItem.updateOrderItem(orderItemModel);
        orderItem = update(orderItem);
        return orderItem;
    }

    @Override
    @Transactional(readOnly = false)
    public int delete(Long id) throws CustomException {
        OrderItem OrderItem = orderItemRepository.find(id);
        if (OrderItem == null)
            throw CommonUtils.createException(Code.VEHICLE_ID_NOT_EXISTED);
        return orderItemRepository.delete(id);
    }

    @Override
    public OrderItemModel findOne(Long id) {
        OrderItem OrderItem = find(id);
        OrderItemModel OrderItemModel = OrderItem.convertToModel(OrderItem);
        return OrderItemModel;
    }

    @Override
    public List<OrderItemModel> getByOrder(Order order) {
        String query = " from OrderItem e where 1=1 and e.order.id = :orderId";
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setQuery(query);
        HashMap<String, Object> params = new HashMap<>();
        params.put("orderId", order.getId());
        queryTemplate.setParameterMap(params);
        List<OrderItem> orderItems = find(queryTemplate);
        List<OrderItemModel> orderItemModels = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemModels.add(OrderItem.convertToModel(orderItem));
        }
        return orderItemModels;
    }

    @Override
    @Transactional
    @Async
    public void updateOrderItems(Product product) throws CustomException {
        String query = " from OrderItem e where 1=1 and e.product.id = :productId";
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setQuery(query);
        HashMap<String, Object> params = new HashMap<>();
        params.put("productId", product.getId());
        queryTemplate.setParameterMap(params);
        List<OrderItem> orderItems = find(queryTemplate);
        for (OrderItem orderItem : orderItems) {
            orderItem.setProduct(product);
            orderItem.calculateTotal();
            orderItem = update(orderItem);
            Order order = orderItem.getOrder();
            order.calculateTotal();
            orderRepository.update(order);
        }
    }

    @Override
    public int deleteByOrder(Order order) throws CustomException {
        List<OrderItemModel> orderItems = getByOrder(order);
        for (OrderItemModel orderItemModel : orderItems) {
            delete(orderItemModel.getId());
        }
        return orderItems.size();
    }

    @Override
    public int deleteByReturnOrder(ReturnOrder returnOrder) throws CustomException {
        String query = " from OrderItem e where 1=1 and e.returnOrder.id = :returnOrderId";
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setQuery(query);
        HashMap<String, Object> params = new HashMap<>();
        params.put("returnOrderId", returnOrder.getId());
        queryTemplate.setParameterMap(params);
        List<OrderItem> orderItems = find(queryTemplate);
        for (OrderItem orderItem : orderItems) {
            delete(orderItem.getId());
        }
        return orderItems.size();
    }

    @Override
    public OrderItemRepository getRepository() {
        return this.orderItemRepository;
    }
}
