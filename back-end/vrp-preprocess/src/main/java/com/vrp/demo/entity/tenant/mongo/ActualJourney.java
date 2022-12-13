package com.vrp.demo.entity.tenant.mongo;

import com.vrp.demo.models.DepotModel;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.solution.Journey;
import com.vrp.demo.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActualJourney {

    private Float currentLat;
    private Float currentLng;
    private Integer travelTime;
    private Integer travelDistance;
    private Date startTime;
    private Date endTime;
    private OrderModel nextOrder;
    private LinkedMap<String, CheckIn> milestone;

    public ActualJourney(JourneyDriver journeyDriver, Journey scheduleJourney) {
        LinkedMap<String, CheckIn> milestone = new LinkedMap<>();
        for (String mustDeliveryOrder : journeyDriver.getMustDeliveryOrders()) {
            milestone.put(mustDeliveryOrder, null);
        }
        setMilestone(milestone);
        DepotModel startDepot = scheduleJourney.getRoutes().get(0).getStartDepot();
        setCurrentLat(startDepot.getLatitude().floatValue());
        setCurrentLng(startDepot.getLongitude().floatValue());
        setNextOrder(CommonUtils.getOrderByCode(scheduleJourney.getOrders(), journeyDriver.getMustDeliveryOrders().get(0)));
        setStartTime(new Date(journeyDriver.getIntendReceiveTime().getTime() + startDepot.getStartTime()));
    }

}
