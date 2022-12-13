package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Correlation;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.CorrelationModel;
import com.vrp.demo.models.NodeModel;
import com.vrp.demo.models.search.CorrelationSearch;

import java.util.List;

public interface CorrelationService extends BaseService<Correlation, Long> {

    public List<CorrelationModel> find(CorrelationSearch search);

    public List<Correlation> findCorrelations(CorrelationSearch search);

    public CorrelationModel findOne(Long id);

    public CorrelationModel update(CorrelationModel correlationModel) throws CustomException;

    public List<Correlation> create(List<Correlation> correlations) throws CustomException;

    public void createCorrelations(NodeModel node);

    public void createCorrelationsData(NodeModel node);

}
