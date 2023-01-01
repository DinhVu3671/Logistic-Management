package com.vrp.demo.models.orders;

import com.vrp.demo.models.BaseModel;
import com.vrp.demo.models.OrderItemModel;
import com.vrp.demo.models.enu.DeliveryMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModelCreate extends BaseModel {
    private Long customerId;
    private DeliveryMode deliveryMode;
    private Integer timeService;
    private Integer timeLoading;
    List<OrderItemModel> orderItems;
}
