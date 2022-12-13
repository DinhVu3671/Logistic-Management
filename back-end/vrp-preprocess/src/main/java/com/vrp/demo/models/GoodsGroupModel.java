package com.vrp.demo.models;

import com.vrp.demo.entity.tenant.GoodsGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsGroupModel extends BaseModel{
    
    private String name;
    private String detail;

    public static GoodsGroup convertToEntity(GoodsGroupModel goodsGroupModel) {
        ModelMapper modelMapper = new ModelMapper();
        GoodsGroup goodsGroup = modelMapper.map(goodsGroupModel, GoodsGroup.class);
        return goodsGroup;
    }
    
}
