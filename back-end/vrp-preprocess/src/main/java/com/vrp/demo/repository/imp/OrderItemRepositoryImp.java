package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.OrderItem;
import com.vrp.demo.repository.OrderItemRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Repository(value = "orderItemRepository")
public class OrderItemRepositoryImp extends BaseRepositoryImp<OrderItem,Long> implements OrderItemRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<OrderItem> create(List<OrderItem> orderItems){
        for (OrderItem orderItem : orderItems) {
            orderItem = insert(orderItem);
        }
        return orderItems;
    }

    @Override
    public Map<String, Object> stat(String startDate, String endDate){
        String quantityQueryStr = String.format("select p.name, count(p.id) as quantity from order_items as o\n" +
                "        left join products p on o.product_id = p.id\n" +
                "        where from_unixtime(o.created_at / 1000) > '%s' and from_unixtime(o.created_at / 1000) < '%s'\n" +
                "        group by p.id\n" +
                "        order by quantity desc", startDate, endDate);
        String revenueQueryStr = String.format("select p.name, sum(p.price) as revenue from order_items as o\n" +
                "        left join products p on o.product_id = p.id\n" +
                "        where from_unixtime(o.created_at / 1000) > '%s' and from_unixtime(o.created_at / 1000) < '%s'\n" +
                "        group by p.id\n" +
                "        order by revenue desc", startDate, endDate);
        Query quantityQuery = entityManager.unwrap(Session.class).createNativeQuery(quantityQueryStr);
        Query revenueQuery = entityManager.unwrap(Session.class).createNativeQuery(revenueQueryStr);
        Map<String, Object> statData = new HashMap<String, Object>();

        statData.put("quantity", quantityQuery.getResultList());
        statData.put("revenue", revenueQuery.getResultList());

        return statData;
    }

    public OrderItemRepositoryImp() {
        super(OrderItem.class);
    }

}
