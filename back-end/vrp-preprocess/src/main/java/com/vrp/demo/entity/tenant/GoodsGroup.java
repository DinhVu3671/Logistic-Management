package com.vrp.demo.entity.tenant;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.models.GoodsGroupModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "good_groups")
public class GoodsGroup extends BaseEntity {

    private String name;
    private String detail;

    public static GoodsGroupModel convertToModel(GoodsGroup goodsGroup) {
        ModelMapper modelMapper = new ModelMapper();
        GoodsGroupModel goodsGroupModel = modelMapper.map(goodsGroup, GoodsGroupModel.class);
        return goodsGroupModel;
    }

    public GoodsGroup updateGoodsGroup(GoodsGroupModel goodsGroup) {
        this.setName(goodsGroup.getName());
        this.setDetail(goodsGroup.getDetail());
        return this;
    }
}
