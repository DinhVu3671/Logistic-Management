package com.vrp.demo.repository;

import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.models.dashboard.SalesModels;

import java.text.ParseException;
import java.util.List;

public interface OrderRepository extends BaseRepository<Order, Long>{
    List<SalesModels> searchByYear(String year) throws ParseException;
}
