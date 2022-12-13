package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.GoodsGroup;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.BaseModel;
import com.vrp.demo.models.GoodsGroupModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.search.GoodsGroupSearch;
import com.vrp.demo.repository.GoodsGroupRepository;
import com.vrp.demo.service.GoodsGroupService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("goodsGroupService")
public class GoodsGroupServiceImp extends BaseServiceImp<GoodsGroupRepository, GoodsGroup, Long> implements GoodsGroupService {

    @Autowired
    private GoodsGroupRepository goodsGroupRepository;

    private QueryTemplate buildQuery(GoodsGroupSearch search) {
        QueryTemplate queryTemplate = getBaseQuery(search);
        String query = queryTemplate.getQuery();
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (search.getName() != null && !search.getName().isEmpty()) {
            query += " and e.name like :name ";
            params.put("name", "%" + search.getName() + "%");
        }
        queryTemplate.setQuery(query);
        queryTemplate.setParameterMap(params);
        return queryTemplate;
    }

    @Override
    public List<GoodsGroupModel> find(GoodsGroupSearch search) {
        List<GoodsGroupModel> goodsGroupModels = new ArrayList<>();
        QueryTemplate queryTemplate = buildQuery(search);
        List<GoodsGroup> goodsGroups = find(queryTemplate);
        for (GoodsGroup goodsGroup : goodsGroups) {
            goodsGroupModels.add(GoodsGroup.convertToModel(goodsGroup));
        }
        return goodsGroupModels;
    }

    @Override
    public List<GoodsGroup> getGoodsGroups(GoodsGroupSearch search) {
        List<Long> ids = search.getGoodsGroups().stream().map(BaseModel::getId).collect(Collectors.toList());
        search.setIds(ids);
        QueryTemplate queryTemplate = buildQuery(search);
        List<GoodsGroup> goodsGroups = find(queryTemplate);
        return goodsGroups;
    }

    @Override
    public Page<GoodsGroupModel> search(GoodsGroupSearch search) {
        CommonUtils.setDefaultPageIfNull(search);
        QueryTemplate queryTemplate = buildQuery(search);
        Page<GoodsGroup> goodsGroups = search(queryTemplate);
        return goodsGroups.map(goodsGroup -> {
            GoodsGroupModel model = GoodsGroup.convertToModel(goodsGroup);
            return model;
        });
    }

    @Override
    @Transactional(readOnly = false)
    public GoodsGroupModel create(GoodsGroupModel goodsGroupModel) throws CustomException {
        GoodsGroup goodsGroup = GoodsGroupModel.convertToEntity(goodsGroupModel);
        goodsGroup = create(goodsGroup);
        goodsGroup = update(goodsGroup);
        goodsGroupModel = GoodsGroup.convertToModel(goodsGroup);
        return goodsGroupModel;
    }

    @Override
    @Transactional(readOnly = false)
    public GoodsGroupModel update(GoodsGroupModel goodsGroupModel) throws CustomException {
        GoodsGroup goodsGroup = goodsGroupRepository.find(goodsGroupModel.getId());
        if (goodsGroup == null)
            throw CommonUtils.createException(Code.GOODS_GROUP_ID_NOT_EXISTED);
        goodsGroup = goodsGroup.updateGoodsGroup(goodsGroupModel);
        goodsGroup = update(goodsGroup);
        goodsGroupModel = GoodsGroup.convertToModel(goodsGroup);
        return goodsGroupModel;
    }

    @Override
    @Transactional(readOnly = false)
    public int delete(Long id) throws CustomException {
        GoodsGroup GoodsGroup = goodsGroupRepository.find(id);
        if (GoodsGroup == null)
            throw CommonUtils.createException(Code.VEHICLE_ID_NOT_EXISTED);
        return goodsGroupRepository.delete(id);
    }

    @Override
    public GoodsGroupModel findOne(Long id) {
        GoodsGroup GoodsGroup = find(id);
        GoodsGroupModel GoodsGroupModel = GoodsGroup.convertToModel(GoodsGroup);
        return GoodsGroupModel;
    }

    @Override
    public GoodsGroupRepository getRepository() {
        return this.goodsGroupRepository;
    }
}
