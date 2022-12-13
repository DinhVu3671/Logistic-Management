package com.vrp.demo.entity.tenant;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.models.GoodsGroupModel;
import com.vrp.demo.models.OrderItemModel;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.enu.DeliveryMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    private String code;
    @ManyToOne
    private Depot depot;
    @ManyToOne
    private Customer customer;
    private double weight;
    private double capacity;
    @Column(name = "order_value")
    private double orderValue;
    @Column(name = "delivery_mode")
    @Enumerated(EnumType.STRING)
    private DeliveryMode deliveryMode;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderItem> orderItems;
    @Column(name = "delivery_before_time")
    private Long deliveryBeforeTime;
    @Column(name = "delivery_after_time")
    private Long deliveryAfterTime;
    @Column(name = "time_service")
    private Integer timeService;
    @Column(name = "time_loading")
    private Integer timeLoading;
    //    private double antiStackingArea;
    @Column(name = "intend_receive_time")
    protected Timestamp intendReceiveTime;

    public static OrderModel convertToModel(Order order) {
        ModelMapper modelMapper = new ModelMapper();
        OrderModel orderModel = modelMapper.map(order, OrderModel.class);
        orderModel.setProductTypeNumber(order.getOrderItems().size());
        List<GoodsGroupModel> goodsGroups = new ArrayList<>();
        List<OrderItemModel> orderItemModels = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderModel model = new OrderModel();
            model.setId(order.getId());
            OrderItemModel orderItemModel = OrderItem.convertToModel(orderItem);
            orderItemModel.getProduct().ignoreRecursiveExcludingProducts();
            orderItemModel.setOrder(model);
            orderItemModels.add(orderItemModel);
            goodsGroups.add(orderItemModel.getProduct().getGoodsGroup());
        }
        orderModel.setOrderItems(orderItemModels);
        orderModel.setGoodsGroups(goodsGroups);
        if (order.getDepot() != null)
            orderModel.setDepot(Depot.convertToModel(order.getDepot()));
        return orderModel;
    }

    public Order updateOrder(OrderModel orderModel) {
        this.setDeliveryMode(orderModel.getDeliveryMode());
        this.setOrderValue(orderModel.getOrderValue());
        this.setWeight(orderModel.getWeight());
        this.setCapacity(orderModel.getCapacity());
        this.setDeliveryAfterTime(orderModel.getDeliveryAfterTime());
        this.setDeliveryBeforeTime(orderModel.getDeliveryBeforeTime());
        this.setTimeService(orderModel.getTimeService());
        this.setTimeLoading(orderModel.getTimeLoading());
        return this;
    }

    public void calculateTotal() {
        double totalWeight = 0;
        double totalCapacity = 0;
        double totalValue = 0;
//        double antiStackingArea = 0;
        for (OrderItem orderItem : orderItems) {
            totalWeight += orderItem.getWeight();
            totalCapacity += orderItem.getCapacity();
            totalValue += orderItem.getPrice();
//            antiStackingArea += orderItem.getAntiStackingArea();
        }
        this.weight = Math.round(totalWeight * 100.0) / 100.0;
        this.capacity = Math.round(totalCapacity * 100.0) / 100.0;
        this.orderValue = Math.round(totalValue * 100.0) / 100.0;
//        this.antiStackingArea = Math.round(antiStackingArea * 100.0) / 100.0;
    }

    public List<Product> getProducts() {
        return orderItems.stream().map(orderItem -> orderItem.getProduct()).collect(Collectors.toList());
    }

}
