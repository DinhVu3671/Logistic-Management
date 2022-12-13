package com.vrp.demo.repository;

import com.vrp.demo.entity.tenant.mongo.DeliveryPlan;
import com.vrp.demo.models.search.DeliveryPlanSearch;
import org.springframework.data.domain.Page;

public interface DeliveryPlanRepository {

    public DeliveryPlan createDeliveryPlan(DeliveryPlan deliveryPlan);

    public DeliveryPlan updateDeliveryPlan(DeliveryPlan deliveryPlan);

    public Page<DeliveryPlan> searchDeliveryPlan(DeliveryPlanSearch search);

    public DeliveryPlan getDeliveryPlan(Long id);


}
