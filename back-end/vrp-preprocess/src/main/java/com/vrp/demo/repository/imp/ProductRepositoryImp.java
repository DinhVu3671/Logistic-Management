package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.repository.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "productRepository")
public class ProductRepositoryImp extends BaseRepositoryImp<Product,Long> implements ProductRepository {

    public ProductRepositoryImp() {
        super(Product.class);
    }

}
