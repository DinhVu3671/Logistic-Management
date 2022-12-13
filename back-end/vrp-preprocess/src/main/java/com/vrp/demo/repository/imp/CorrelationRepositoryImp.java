package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.Correlation;
import com.vrp.demo.repository.CorrelationRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "correlationRepository")
public class CorrelationRepositoryImp extends BaseRepositoryImp<Correlation,Long> implements CorrelationRepository {

    public CorrelationRepositoryImp() {
        super(Correlation.class);
    }

}
