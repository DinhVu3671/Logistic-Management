package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.mongo.JourneyDriver;
import com.vrp.demo.entity.tenant.mongo.DeliveryPlan;
import com.vrp.demo.models.search.JourneyDriverSearch;
import com.vrp.demo.models.solution.Journey;
import com.vrp.demo.repository.JourneyDriverRepository;
import com.vrp.demo.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository(value = "journeyDriverRepositoryImp")
public class JourneyDriverRepositoryImp implements JourneyDriverRepository {

    @Autowired
    private MongoTemplate mongoTemplate;


    private Query getQuery(JourneyDriverSearch search) {
        Query query = new Query();
        if (search.getSolutionRoutingId() != null)
            query.addCriteria(Criteria.where("solutionRoutingId").is(search.getSolutionRoutingId()));
        if (search.getIntendReceiveTime() != null)
            query.addCriteria(Criteria.where("intendReceiveTime").is(search.getIntendReceiveTime()));
        return query;
    }

    public long getMaxId() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        query.limit(1);
        JourneyDriver maxObject = mongoTemplate.findOne(query, JourneyDriver.class);
        if (maxObject == null) {
            return 0L;
        }
        return maxObject.getId();
    }


    @Override
    public JourneyDriver createJourney(DeliveryPlan solution, Journey journey) {
        JourneyDriver journeyDriver = new JourneyDriver(solution, journey);
        journeyDriver.setId(getMaxId() + 1);
        journeyDriver.setCreatedAt(new Date(CommonUtils.getCurrentTime().getTime()));
        journeyDriver.setUpdatedAt(new Date(CommonUtils.getCurrentTime().getTime()));
        return mongoTemplate.save(journeyDriver);
    }

    @Override
    public List<JourneyDriver> createJourneys(DeliveryPlan solution) {
        List<JourneyDriver> journeyDrivers = new ArrayList<>();
        for (Journey journey : solution.getSolution().getJourneys()) {
            JourneyDriver journeyDriver = createJourney(solution, journey);
            journeyDrivers.add(journeyDriver);
        }
        return journeyDrivers;
    }

    @Override
    public List<JourneyDriver> findJourneys(JourneyDriverSearch search) {
        Query query = getQuery(search);
        List<JourneyDriver> results = mongoTemplate.find(query, JourneyDriver.class);
        return results;
    }
}
