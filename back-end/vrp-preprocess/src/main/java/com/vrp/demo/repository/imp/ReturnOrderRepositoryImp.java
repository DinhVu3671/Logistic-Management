package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.ReturnOrder;
import com.vrp.demo.repository.ReturnOrderRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "returnOrderRepository")
public class ReturnOrderRepositoryImp extends BaseRepositoryImp<ReturnOrder,Long> implements ReturnOrderRepository {
    public ReturnOrderRepositoryImp() {
        super(ReturnOrder.class);
    }

}
