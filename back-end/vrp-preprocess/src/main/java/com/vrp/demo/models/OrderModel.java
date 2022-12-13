package com.vrp.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.models.enu.DeliveryMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel extends BaseModel {

    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DepotModel depot;
    private CustomerModel customer;
    private double weight;
    private double capacity;
    private double orderValue;
    private DeliveryMode deliveryMode;
    private List<OrderItemModel> orderItems;
    private Long deliveryBeforeTime;
    private Long deliveryAfterTime;
    private Integer productTypeNumber;
    private Integer timeService;
    private Integer timeLoading;
    @JsonIgnore
    private double antiStackingArea;
    private List<GoodsGroupModel> goodsGroups;

    public static Order convertToEntity(OrderModel orderModel) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderModel, Order.class);
    }

    public boolean isExcludeVehicle(VehicleModel vehicleModel) {
        for (GoodsGroupModel excludedGoodsGroup : vehicleModel.getExcludedGoodsGroups()) {
            for (GoodsGroupModel goodsGroup : this.goodsGroups) {
                if (excludedGoodsGroup.getId().equals(goodsGroup.getId()))
                    return true;
            }
        }
//        for (OrderItemModel orderItem : orderItems) {
//            if (orderItem.getQuantity() > vehicleModel.getMaxNumProduct(orderItem.getProduct().getId()))
//                return true;
//        }
//        if (this.antiStackingArea > (vehicleModel.getLength() * vehicleModel.getWidth()))
//            return true;
        return false;
    }

    public boolean isExcludeOrder(OrderModel orderModel) {
        for (OrderItemModel orderItem : orderItems) {
            for (OrderItemModel item : orderModel.getOrderItems()) {
                if (orderItem.isExcludeOrderItem(item))
                    return true;
            }
        }
        return false;
    }

    public List<Long> getExcludeRequestIds(List<OrderModel> orderModels) {
        List<Long> excludeRequestIds = new ArrayList<>();
        for (OrderModel orderModel : orderModels) {
            if (this.isExcludeOrder(orderModel))
                excludeRequestIds.add(orderModel.getId());
        }
        return excludeRequestIds;
    }

    public DepotModel setFromDepot(List<CustomerModel> clusterCustomers) {
        for (CustomerModel clusterCustomer : clusterCustomers) {
            if (this.customer.getId().equals(clusterCustomer.getId()))
                this.setDepot(clusterCustomer.getClusterDepot());
        }
        return this.depot;
    }

    public OrderModel clone() {
        OrderModel clone = new OrderModel();
        clone.setCode(this.code);
        clone.setDepot(this.depot);
        clone.setWeight(this.weight);
        clone.setCapacity(this.capacity);
        clone.setOrderValue(this.orderValue);
        clone.setDeliveryMode(this.deliveryMode);
        clone.setOrderItems(this.orderItems);
        clone.setDeliveryBeforeTime(this.deliveryBeforeTime);
        clone.setDeliveryAfterTime(this.deliveryAfterTime);
        clone.setProductTypeNumber(this.productTypeNumber);
        clone.setTimeService(this.timeService);
        clone.setTimeLoading(this.timeLoading);
        clone.setGoodsGroups(this.goodsGroups);
        List<OrderItemModel> orderItemModels = new ArrayList<>(this.orderItems);
        for (OrderItemModel orderItemModel : orderItemModels) {
            orderItemModel.setId(null);
        }
        clone.setOrderItems(orderItemModels);
        return clone;
    }

}
