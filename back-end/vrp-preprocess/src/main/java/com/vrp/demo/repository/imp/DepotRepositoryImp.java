package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.Depot;
import com.vrp.demo.repository.DepotRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "depotRepository")
public class DepotRepositoryImp extends BaseRepositoryImp<Depot,Long> implements DepotRepository {

    public DepotRepositoryImp() {
        super(Depot.class);
    }

}
