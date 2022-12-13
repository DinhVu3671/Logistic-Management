package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.GoodsGroup;
import com.vrp.demo.repository.GoodsGroupRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "goodsGroupRepository")
public class GoodsGroupRepositoryImp extends BaseRepositoryImp<GoodsGroup, Long> implements GoodsGroupRepository {

    public GoodsGroupRepositoryImp() {
        super(GoodsGroup.class);
    }

}
