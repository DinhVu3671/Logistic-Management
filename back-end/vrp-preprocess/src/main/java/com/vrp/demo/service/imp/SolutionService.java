package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.mongo.DeliveryPlan;
import com.vrp.demo.models.search.DeliveryPlanSearch;
import com.vrp.demo.repository.DeliveryPlanRepository;
import com.vrp.demo.repository.JourneyDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class SolutionService {

    @Autowired
    private DeliveryPlanRepository deliveryPlanRepository;

    @Autowired
    private JourneyDriverRepository journeyDriverRepository;

    public DeliveryPlan create(DeliveryPlan deliveryPlan) {
        return deliveryPlanRepository.createDeliveryPlan(deliveryPlan);
    }

    public Page<DeliveryPlan> search(DeliveryPlanSearch search) {
        return deliveryPlanRepository.searchDeliveryPlan(search);
    }

    public DeliveryPlan tracking(Long id) {
        DeliveryPlan trackingRouting = deliveryPlanRepository.getDeliveryPlan(id);
        return trackingRouting;
    }

    public DeliveryPlan select(Long id) {
        DeliveryPlan deliveryPlan = deliveryPlanRepository.getDeliveryPlan(id);
        deliveryPlan.setIsSelected(true);
        DeliveryPlan selectedRouting = deliveryPlanRepository.updateDeliveryPlan(deliveryPlan);
        journeyDriverRepository.createJourneys(selectedRouting);
        return selectedRouting;
    }

}
