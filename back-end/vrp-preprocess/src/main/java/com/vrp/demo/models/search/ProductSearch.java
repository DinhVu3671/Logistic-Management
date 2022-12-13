package com.vrp.demo.models.search;

import com.vrp.demo.models.ProductModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductSearch extends BaseSearch {

    private String name;
    private List<ProductModel> products;

}
