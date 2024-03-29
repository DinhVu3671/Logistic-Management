package com.vrp.demo.service.imp;

import com.vrp.demo.entity.common.User;
import com.vrp.demo.entity.tenant.Customer;
import com.vrp.demo.entity.tenant.Depot;
import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.entity.tenant.OrderItem;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.CustomerModel;
import com.vrp.demo.models.OrderItemModel;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.dashboard.SalesModels;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.enu.DeliveryMode;
import com.vrp.demo.models.orders.OrderModelCreate;
import com.vrp.demo.models.search.CustomerSearch;
import com.vrp.demo.models.search.OrderSearch;
import com.vrp.demo.repository.CustomerRepository;
import com.vrp.demo.repository.DepotRepository;
import com.vrp.demo.repository.OrderRepository;
import com.vrp.demo.service.CustomerService;
import com.vrp.demo.service.OrderItemService;
import com.vrp.demo.service.OrderService;
import com.vrp.demo.service.UserService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("orderService")
public class OrderServiceImp extends BaseServiceImp<OrderRepository, Order, Long> implements OrderService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;

    private QueryTemplate buildQuery(OrderSearch search) {
        QueryTemplate queryTemplate = getBaseQuery(search);
        String query = queryTemplate.getQuery();
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (search.getCode() != null && !search.getCode().isEmpty()) {
            query += " and e.code like :code ";
            params.put("code", "%" + search.getCode() + "%");
        }
        queryTemplate.setQuery(query);
        queryTemplate.setParameterMap(params);
        return queryTemplate;
    }

    @Override
    public List<OrderModel> find(OrderSearch search) {
        List<OrderModel> orderModels = new ArrayList<>();
        QueryTemplate queryTemplate = buildQuery(search);
        List<Order> orders = find(queryTemplate);
        for (Order order : orders) {
            orderModels.add(Order.convertToModel(order));
        }
        return orderModels;
    }

    @Override
    public Page<OrderModel> search(OrderSearch search) {
        CommonUtils.setDefaultPageIfNull(search);
        QueryTemplate queryTemplate = buildQuery(search);
        Page<Order> orders = search(queryTemplate);
        return orders.map(order -> {
            OrderModel model = Order.convertToModel(order);
            return model;
        });
    }
    @Override
    public List<OrderModel> getOrdersByCustomer(Long userId) {
        Customer customer = customerService.getCustomerByUserId(userId);
//        User user = userService.find(userId);
        List<OrderModel> orderModels = new ArrayList<>();
//        QueryTemplate queryTemplate = buildQuery(search);

//        QueryTemplate queryTemplate = getBaseQuery(search);
        QueryTemplate queryTemplate = new QueryTemplate();
        HashMap<String, Object> params = queryTemplate.getParameterMap();

        String querysentence = "Select * from orders where ";
        querysentence += " customer_id = :id ";
        params.put("id", customer.getId());
//        queryTemplate.setPageable(search.getPageable());
        queryTemplate.setQuery(querysentence);
        queryTemplate.setParameterMap(params);

        Query query = entityManager.createNativeQuery(queryTemplate.getQuery(), Order.class).setParameter("id", customer.getId());
        List<Order> orders = query.getResultList();
        for (Order order : orders) {
            orderModels.add(Order.convertToModel(order));
        }
        return orderModels;
    }

    @Override
    @Transactional(readOnly = false)
    public OrderModel create(OrderModelCreate orderModelCreate) throws CustomException {
        Customer customer = customerRepository.find(orderModelCreate.getCustomerId());
        if (customer == null)
            throw CommonUtils.createException(Code.CUSTOMER_ID_NOT_EXISTED);
//        Depot depot = depotRepository.find(orderModel.getDepot().getId());
//        if (depot == null)
//            throw CommonUtils.createException(Code.DEPOT_ID_NOT_EXISTED);
        Order orderTmp = new Order();

//        Order order = OrderModel.convertToEntity(orderModel);
//        order.setDepot(depot);
        orderTmp.setDeliveryMode(orderModelCreate.getDeliveryMode());
//        if (orderModelCreate.getDeliveryMode().equals(DeliveryMode.STANDARD)) {
        orderTmp.setDeliveryBeforeTime(customer.getEndTime());
        orderTmp.setDeliveryAfterTime(customer.getStartTime());
//        }
        orderTmp.setTimeService(orderModelCreate.getTimeService());
        orderTmp.setTimeLoading(orderModelCreate.getTimeLoading());
        orderTmp.setStatus("created");
        orderTmp.setCustomer(customer);
        orderTmp = create(orderTmp);

        for (OrderItemModel orderItemModel : orderModelCreate.getOrderItems()) {
            OrderModel orderModel = new OrderModel();
            orderModel.setId(orderTmp.getId());
            orderItemModel.setOrder(orderModel);
        }
        List<OrderItem> orderItems = orderItemService.create(orderModelCreate.getOrderItems());
        orderTmp.setOrderItems(orderItems);
        orderTmp.calculateTotal();
        orderTmp.setCode("O" + orderTmp.getId());
        orderTmp = update(orderTmp);
        OrderModel orderModel = Order.convertToModel(orderTmp);
        return orderModel;
    }
    @Override
    @Transactional(readOnly = false)
    public OrderModel create(OrderModel orderModel) throws CustomException {
        Customer customer = customerRepository.find(orderModel.getCustomer().getId());
        if (customer == null)
            throw CommonUtils.createException(Code.CUSTOMER_ID_NOT_EXISTED);
    //        Depot depot = depotRepository.find(orderModel.getDepot().getId());
    //        if (depot == null)
    //            throw CommonUtils.createException(Code.DEPOT_ID_NOT_EXISTED);
        Order order = OrderModel.convertToEntity(orderModel);
    //        order.setDepot(depot);
//        if (order.getDeliveryMode().equals(DeliveryMode.STANDARD)) {
            order.setDeliveryBeforeTime(customer.getEndTime());
            order.setDeliveryAfterTime(customer.getStartTime());
//        }
        order.setCustomer(customer);
        order.setStatus("created");
        order = create(order);
        for (OrderItemModel orderItemModel : orderModel.getOrderItems()) {
            orderItemModel.setOrder(Order.convertToModel(order));
        }
        List<OrderItem> orderItems = orderItemService.create(orderModel.getOrderItems());
        order.setOrderItems(orderItems);
        order.calculateTotal();
        order.setCode("O" + order.getId());
        order = update(order);
        orderModel = Order.convertToModel(order);
        return orderModel;
    }

    @Override
    @Transactional(readOnly = false)
    public OrderModel update(OrderModel orderModel) throws CustomException {
        Order order = orderRepository.find(orderModel.getId());
        if (order == null)
            throw CommonUtils.createException(Code.ORDER_ID_NOT_EXISTED);
        Customer customer = customerRepository.find(orderModel.getCustomer().getId());
        if (customer == null)
            throw CommonUtils.createException(Code.CUSTOMER_ID_NOT_EXISTED);
//        Depot depot = depotRepository.find(orderModel.getDepot().getId());
//        if (depot == null)
//            throw CommonUtils.createException(Code.DEPOT_ID_NOT_EXISTED);
        order.setStatus(orderModel.getStatus());
        orderModel = Order.convertToModel(order);
        order = order.updateOrder(orderModel);
        if (order.getDeliveryMode().equals(DeliveryMode.STANDARD)) {
            order.setDeliveryBeforeTime(customer.getEndTime());
            order.setDeliveryAfterTime(customer.getStartTime());
        }
//        order.setDepot(depot);
        order.setCustomer(customer);
        orderItemService.deleteByOrder(order);
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemModel orderItemModel : orderModel.getOrderItems()) {
            orderItemModel.setId(null);
            orderItemModel.setOrder(orderModel);
            orderItems.add(orderItemService.create(orderItemModel));
        }
        order.setOrderItems(orderItems);
        order.calculateTotal();
        order = update(order);
        orderModel = Order.convertToModel(order);
        return orderModel;
    }

    @Override
    @Transactional(readOnly = false)
    public int delete(Long id) throws CustomException {
        Order Order = orderRepository.find(id);
        if (Order == null)
            throw CommonUtils.createException(Code.VEHICLE_ID_NOT_EXISTED);
        return orderRepository.delete(id);
    }

    @Override
    public OrderModel findOne(Long id) {
        Order Order = find(id);
        OrderModel OrderModel = Order.convertToModel(Order);
        return OrderModel;
    }

    @Override
    @Transactional(readOnly = false)
    @Async
    public void createOrderData() throws CustomException {
        CustomerSearch customerSearch = new CustomerSearch();
//        customerSearch.setIds(Arrays.asList(368l, 369l, 370l, 371l, 372l, 373l, 374l, 375l, 376l));
        List<CustomerModel> customers = customerService.find(customerSearch);
        List<OrderModel> orderModels = find(new OrderSearch());
        for (CustomerModel customer : customers) {
            if (customer.getId() > 200 && customer.getId() < 236) {
//            if (customer.getId() == 376) {
            OrderModel orderModel = orderModels.get(customer.getId().intValue() % 8).clone();
            orderModel.setCustomer(customer);
//            create(orderModel);
            }
        }
    }

    @Override
    public List<SalesModels> searchByYear(String year) throws ParseException {
        return orderRepository.searchByYear(year);
    }


    @Override
    public OrderRepository getRepository() {
        return this.orderRepository;
    }
}
