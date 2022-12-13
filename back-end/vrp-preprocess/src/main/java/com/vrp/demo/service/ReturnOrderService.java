package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.ReturnOrder;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.ReturnOrderModel;
import com.vrp.demo.models.search.OrderSearch;

public interface ReturnOrderService extends BaseService<ReturnOrder, Long>{

    public ReturnOrderModel find(OrderSearch search);

    public ReturnOrderModel create(ReturnOrderModel returnOrderModel) throws CustomException;

    public ReturnOrderModel update(ReturnOrderModel returnOrderModel) throws CustomException;

    public int delete(Long id) throws CustomException;

}
