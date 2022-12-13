package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.mongo.JourneyDriver;
import com.vrp.demo.models.search.JourneyDriverSearch;
import com.vrp.demo.repository.JourneyDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JourneyDriverService {

    @Autowired
    private JourneyDriverRepository journeyDriverRepository;

    public List<JourneyDriver> find(JourneyDriverSearch search) {
        List<JourneyDriver> journeyDrivers = journeyDriverRepository.findJourneys(search);
        return journeyDrivers;
    }

}
