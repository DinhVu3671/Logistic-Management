package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Depot;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.DepotModel;
import com.vrp.demo.models.search.DepotSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepotService extends BaseService<Depot, Long> {

    public List<DepotModel> find(DepotSearch search);

    public Page<DepotModel> search(DepotSearch search);

    public DepotModel create(DepotModel depotModel);

    public DepotModel update(DepotModel depotModel) throws CustomException;

    public int delete(Long id) throws CustomException;

    public DepotModel findOne(Long id);

    public List<DepotModel> findAllWithCorrelations();

}
