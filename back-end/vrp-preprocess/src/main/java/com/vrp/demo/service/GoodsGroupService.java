package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.GoodsGroup;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.GoodsGroupModel;
import com.vrp.demo.models.search.GoodsGroupSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GoodsGroupService extends BaseService<GoodsGroup, Long> {

    public List<GoodsGroupModel> find(GoodsGroupSearch search);

    public Page<GoodsGroupModel> search(GoodsGroupSearch search);

    public GoodsGroupModel create(GoodsGroupModel goodsGroupModel) throws CustomException;

    public GoodsGroupModel update(GoodsGroupModel goodsGroupModel) throws CustomException;

    public int delete(Long id) throws CustomException;

    public GoodsGroupModel findOne(Long id);

    public List<GoodsGroup> getGoodsGroups(GoodsGroupSearch search);

}
