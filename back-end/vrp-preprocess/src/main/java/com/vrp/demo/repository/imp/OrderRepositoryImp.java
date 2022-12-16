package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.models.OrderModel;
import com.vrp.demo.models.dashboard.SalesModels;
import com.vrp.demo.repository.OrderRepository;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Repository(value = "orderRepository")
public class OrderRepositoryImp extends BaseRepositoryImp<Order,Long> implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;
    public OrderRepositoryImp() {
        super(Order.class);
    }

    @Override
    public List<SalesModels> searchByYear(String year) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date date1 = formatter.parse(year);
        Date date2 = formatter.parse(String.valueOf(Long.parseLong(year) + 1));
        Timestamp startTime = new Timestamp(date1.getTime());

        String queryString = String.format("Select od.order_value, od.created_at from orders as od" +
                " where od.created_at between '%s' and '%s'", startTime.getTime(), date2.getTime());
        Query query = entityManager.createNativeQuery(queryString);
//        List<SalesModel> salesModelList = query.unwrap(List<SalesModel.class> );
        return query.getResultList();
    }
}