package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.mongo.DeliveryPlan;
import com.vrp.demo.models.search.DeliveryPlanSearch;
import com.vrp.demo.models.solution.Journey;
import com.vrp.demo.models.solution.Solution;
import com.vrp.demo.repository.DeliveryPlanRepository;
import com.vrp.demo.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository(value = "deliveryPlanRepositoryImp")
public class DeliveryPlanRepositoryImp implements DeliveryPlanRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    protected Page<DeliveryPlan> wrapResult(List<DeliveryPlan> results, Pageable page, long count) {
        if (results == null) {
            results = Collections.emptyList();
        }
        return new PageImpl<>(results, page, count);
    }

    private Query getQuery(DeliveryPlanSearch search) {
        Query query = new Query();
        if (search.getName() != null)
            query.addCriteria(Criteria.where("name").regex(".*" + search.getName() + ".*", "i"));
        if (search.getIntendReceiveTime() != null)
            query.addCriteria(Criteria.where("intend_receive_time").is(search.getIntendReceiveTime()));
        return query;
    }

    public long getMaxId() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        query.limit(1);
        DeliveryPlan maxObject = mongoTemplate.findOne(query, DeliveryPlan.class);
        if (maxObject == null) {
            return 0L;
        }
        return maxObject.getId();
    }

    @Override
    public DeliveryPlan createDeliveryPlan(DeliveryPlan deliveryPlan) {
        deliveryPlan.setId(getMaxId() + 1);
        deliveryPlan.getSolution().setObjectId();
        deliveryPlan.getProblemAssumption().setObjectId();
        deliveryPlan.setCreatedAt(new Date(CommonUtils.getCurrentTime().getTime()));
        deliveryPlan.setUpdatedAt(new Date(CommonUtils.getCurrentTime().getTime()));
        return mongoTemplate.save(deliveryPlan);
    }

    @Override
    public DeliveryPlan updateDeliveryPlan(DeliveryPlan deliveryPlan) {
        deliveryPlan.setUpdatedAt(new Date(CommonUtils.getCurrentTime().getTime()));
        return mongoTemplate.save(deliveryPlan);
    }

    @Override
    public Page<DeliveryPlan> searchDeliveryPlan(DeliveryPlanSearch search) {
        Query query = getQuery(search);
        long count = mongoTemplate.count(query, DeliveryPlan.class);
        final Pageable pageableRequest = search.getPageable();
        query.with(pageableRequest);
        query.with(Sort.by(Sort.Direction.DESC, "id"));
        List<DeliveryPlan> results = mongoTemplate.find(query, DeliveryPlan.class);
        return wrapResult(results, pageableRequest, count);
    }

    @Override
    public DeliveryPlan getDeliveryPlan(Long id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, DeliveryPlan.class);
    }

    @Override
    public List<Solution> getSolutionByDriver(Long vehicleId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("solution.journeys").elemMatch(Criteria.where("vehicle._id").is(vehicleId)));

        List<DeliveryPlan> results = mongoTemplate.find(query, DeliveryPlan.class);
        List<Solution> solutionList = new ArrayList<>();
        for(DeliveryPlan deliveryPlan : results) {
            Solution solution = deliveryPlan.getSolution();
            List<Journey> journeys = solution.getJourneys();
            List<Journey> journeyList = new ArrayList<>();
            journeys.forEach((item) -> {
                if(item.getVehicle().getId().equals(vehicleId)) {
                    journeyList.add(item);
                    solution.setJourneys(journeyList);
                }
            });
            solutionList.add(solution);
        }
        return solutionList;
    }


}
