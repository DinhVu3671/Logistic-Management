package com.vrp.demo.service.imp;

import com.vrp.demo.repository.OrderItemRepository;
import com.vrp.demo.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service("dashboardService")
public class DashboardServiceImp implements DashboardService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Override
    @Transactional(readOnly = false)
    public Map<String, Object> orderItemStat(String startDate, String endDate) {
        Map<String, Object> statData = orderItemRepository.stat(startDate, endDate);
        return statData;
    }
}
