package com.vrp.demo.entity.tenant.mongo;

import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.VehicleModel;
import com.vrp.demo.models.enu.JourneyStatus;
import com.vrp.demo.models.solution.Journey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.LinkedList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("journey_driver")
public class JourneyDriver {

    @Id
    private Long id;
    private JourneyStatus status;
    @Field(name = "intend_receive_time")
    private Date intendReceiveTime;
    @Field(name = "solution_routing_id")
    private Long solutionRoutingId;
    @Field(name = "must_delivery_orders")
    private Driver driver;
    private LinkedList<String> mustDeliveryOrders = new LinkedList<>();
    @Field(name = "schedule_journey")
    private Journey scheduleJourney;
    @Field(name = "actual_journey")
    private ActualJourney actualJourney;
    private VehicleModel vehicle;
    @Field(name = "created_at")
    private Date createdAt;
    @Field(name = "updated_at")
    private Date updatedAt;

    public JourneyDriver(DeliveryPlan deliveryPlan, Journey journey) {
        setSolutionRoutingId(deliveryPlan.getId());
        setStatus(JourneyStatus.NOT_YET_START);
        setIntendReceiveTime(deliveryPlan.getIntendReceiveTime());
        setScheduleJourney(journey);
        setVehicle(journey.getVehicle());
        LinkedList<String> mustDeliveryOrders = new LinkedList<>();
        for (OrderModel order : journey.getOrders()) {
            mustDeliveryOrders.add(order.getCode());
        }
        setMustDeliveryOrders(mustDeliveryOrders);
        ActualJourney actualJourney = new ActualJourney(this, journey);
        setActualJourney(actualJourney);
    }

}
