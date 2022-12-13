package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.entity.tenant.OrderItem;
import com.vrp.demo.entity.tenant.ReturnOrder;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.OrderItemModel;
import com.vrp.demo.models.ReturnOrderModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.search.OrderSearch;
import com.vrp.demo.repository.OrderItemRepository;
import com.vrp.demo.repository.OrderRepository;
import com.vrp.demo.repository.ReturnOrderRepository;
import com.vrp.demo.service.OrderItemService;
import com.vrp.demo.service.ReturnOrderService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("returnOrderService")
public class ReturnOrderServiceImp extends BaseServiceImp<ReturnOrderRepository, ReturnOrder, Long> implements ReturnOrderService {

    @Autowired
    private ReturnOrderRepository returnOrderRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public ReturnOrderModel find(OrderSearch search) {
        String query = "from ReturnOrder e where e.order.id = :orderId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("orderId", search.getId());
        QueryTemplate queryTemplate = new QueryTemplate(query, params);
        ReturnOrder returnOrder = findOne(queryTemplate);
        return returnOrder == null ? null : ReturnOrder.convertToModel(returnOrder);
    }

    @Override
    @Transactional(readOnly = false)
    public ReturnOrderModel create(ReturnOrderModel returnOrderModel) throws CustomException {
        Order order = orderRepository.find(returnOrderModel.getOrder().getId());
        if (order == null)
            throw CommonUtils.createException(Code.ORDER_ID_NOT_EXISTED);
        ReturnOrder returnOrder = ReturnOrderModel.convertToEntity(returnOrderModel);
        returnOrder.setOrder(order);
        returnOrder = create(returnOrder);
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemModel orderItemModel : returnOrderModel.getOrderItems()) {
            OrderItem orderItem = OrderItemModel.convertToEntity(orderItemModel);
            orderItem.setReturnOrder(returnOrder);
            orderItems.add(orderItem);
            orderItem.calculateTotal();
        }
        orderItems = orderItemRepository.create(orderItems);
        returnOrder.setOrderItems(orderItems);
        returnOrder.calculateTotal();
        returnOrder = update(returnOrder);
        returnOrderModel = ReturnOrder.convertToModel(returnOrder);
        return returnOrderModel;
    }

    @Override
    @Transactional(readOnly = false)
    public ReturnOrderModel update(ReturnOrderModel returnOrderModel) throws CustomException {
        ReturnOrder returnOrder = returnOrderRepository.find(returnOrderModel.getId());
        if (returnOrder == null)
            throw CommonUtils.createException(Code.ORDER_ID_NOT_EXISTED);
        returnOrder = returnOrder.updateReturnOrder(returnOrderModel);
        orderItemService.deleteByReturnOrder(returnOrder);
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemModel orderItemModel : returnOrderModel.getOrderItems()) {
            OrderItem orderItem = OrderItemModel.convertToEntity(orderItemModel);
            orderItem.setReturnOrder(returnOrder);
            orderItems.add(orderItem);
            orderItem.calculateTotal();
        }
        orderItems = orderItemRepository.create(orderItems);
        returnOrder.setOrderItems(orderItems);
        returnOrder.calculateTotal();
        returnOrder = update(returnOrder);
        returnOrderModel = ReturnOrder.convertToModel(returnOrder);
        return returnOrderModel;
    }

    @Override
    @Transactional(readOnly = false)
    public int delete(Long id) throws CustomException {
        ReturnOrder ReturnOrder = returnOrderRepository.find(id);
        if (ReturnOrder == null)
            throw CommonUtils.createException(Code.ORDER_ID_NOT_EXISTED);
        return returnOrderRepository.delete(id);
    }

    @Override
    public ReturnOrderRepository getRepository() {
        return this.returnOrderRepository;
    }
}
