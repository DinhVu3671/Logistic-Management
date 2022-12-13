package com.vrp.demo.repository;

import com.vrp.demo.entity.tenant.mongo.JourneyDriver;
import com.vrp.demo.entity.tenant.mongo.DeliveryPlan;
import com.vrp.demo.models.search.JourneyDriverSearch;
import com.vrp.demo.models.solution.Journey;

import java.util.List;

public interface JourneyDriverRepository {

    public JourneyDriver createJourney(DeliveryPlan solution, Journey journey);

    public List<JourneyDriver> createJourneys(DeliveryPlan solution);

    public List<JourneyDriver> findJourneys(JourneyDriverSearch search);

}
