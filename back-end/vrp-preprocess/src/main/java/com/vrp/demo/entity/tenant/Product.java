package com.vrp.demo.entity.tenant;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.models.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String code;
    private String name;
    private double price;
    private double weight;
    private double capacity;
    private double length;
    private double width;
    private double height;
    @ManyToOne
    @JoinColumn(name = "goods_group_id")
    private GoodsGroup goodsGroups;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "product_exclude",
            joinColumns = {@JoinColumn(name = "product_excluding_id")},
            inverseJoinColumns = {@JoinColumn(name = "excluded_product_id")})
    private List<Product> excludingProducts;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "product_exclude",
            joinColumns = {@JoinColumn(name = "excluded_product_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_excluding_id")})
    private List<Product> excludedProducts;


    public static ProductModel convertToModel(Product product) {
        ModelMapper modelMapper = new ModelMapper();
        ProductModel productModel = modelMapper.map(product, ProductModel.class);
        productModel.ignoreRecursiveExcludingProducts();
        return productModel;
    }

    public Product updateProduct(ProductModel productModel) {
        this.setName(productModel.getName());
        this.setPrice(productModel.getPrice());
        this.setWeight(productModel.getWeight());
        this.setCapacity(productModel.getCapacity());
        this.setLength(productModel.getLength());
        this.setWidth(productModel.getWidth());
        this.setHeight(productModel.getHeight());
        return this;
    }

}
