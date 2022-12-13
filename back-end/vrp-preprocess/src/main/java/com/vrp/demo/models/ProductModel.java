package com.vrp.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vrp.demo.entity.tenant.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel extends BaseModel{

    private String code;
    private String name;
    private double price;
    private double weight;
    private double capacity;
    private double length;
    private double width;
    private double height;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductModel> excludingProducts;
    private GoodsGroupModel goodsGroup;


    public static Product convertToEntity(ProductModel productModel) {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(productModel, Product.class);
        return product;
    }

    public void ignoreRecursiveExcludingProducts(){
        for (ProductModel excludingProduct : excludingProducts) {
            excludingProduct.setExcludingProducts(null);
        }
    }
    
}
