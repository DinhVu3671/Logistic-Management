package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.ProductModel;
import com.vrp.demo.models.search.ProductSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService extends BaseService<Product, Long> {

    public List<ProductModel> find(ProductSearch search);

    public Page<ProductModel> search(ProductSearch search);

    public ProductModel create(ProductModel productModel) throws CustomException;

    public ProductModel update(ProductModel productModel) throws CustomException;

    public int delete(Long id) throws CustomException;

    public ProductModel findOne(Long id);

    public List<Product> getProducts(ProductSearch search);

}
