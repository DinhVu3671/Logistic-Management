package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.repository.VehicleRepository;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;

@Repository(value = "vehicleRepository")
public class VehicleRepositoryImp extends BaseRepositoryImp<Vehicle,Long> implements VehicleRepository {

    @PersistenceContext
    private EntityManager entityManager;
    public VehicleRepositoryImp() {
        super(Vehicle.class);
    }

    @Override
    public Vehicle getInfoByUserId(Long userId) {
        QueryTemplate queryTemplate = new QueryTemplate();
        HashMap<String, Object> paramsQuery = queryTemplate.getParameterMap();

        String querysentence = "Select * from vehicles where ";
        querysentence += " user_id = :id ";
        paramsQuery.put("id", userId);
        queryTemplate.setQuery(querysentence);
        queryTemplate.setParameterMap(paramsQuery);

        Query query = entityManager.createNativeQuery(queryTemplate.getQuery(), Vehicle.class);
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        Vehicle vehicle = (Vehicle) query.getSingleResult();
        return vehicle;
    }
    @Override
    public Vehicle findByUserId(long userId) {
        String query = " from Vehicle e where e.userId = '+userId+'";
        QueryTemplate queryTemplate = new QueryTemplate();
        queryTemplate.setQuery(query);
        return findOne(queryTemplate);
    }

}
