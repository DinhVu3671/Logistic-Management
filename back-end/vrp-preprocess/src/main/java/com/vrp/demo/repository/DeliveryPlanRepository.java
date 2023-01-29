package com.vrp.demo.repository;

import com.vrp.demo.entity.tenant.mongo.DeliveryPlan;
import com.vrp.demo.models.search.DeliveryPlanSearch;
import com.vrp.demo.models.solution.Journey;
import com.vrp.demo.models.solution.Solution;
import com.vrp.demo.models.solution.UpdateRouterRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DeliveryPlanRepository {

    public DeliveryPlan createDeliveryPlan(DeliveryPlan deliveryPlan);

    public DeliveryPlan updateDeliveryPlan(DeliveryPlan deliveryPlan);

    public Page<DeliveryPlan> searchDeliveryPlan(DeliveryPlanSearch search);

    public DeliveryPlan getDeliveryPlan(Long id);
    public List<Solution> getSolutionByDriver(Long vehicleId);
    public Boolean checkRouter(String idRouter);
    public Journey updateOrderInRouter(UpdateRouterRequest updateRouterRequest);

}
