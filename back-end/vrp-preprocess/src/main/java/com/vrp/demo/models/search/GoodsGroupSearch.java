package com.vrp.demo.models.search;

import com.vrp.demo.models.GoodsGroupModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GoodsGroupSearch extends BaseSearch {
    private String name;
    private List<GoodsGroupModel> goodsGroups;
}
