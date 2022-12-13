package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.GoodsGroup;
import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.BaseModel;
import com.vrp.demo.models.ProductModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.search.GoodsGroupSearch;
import com.vrp.demo.models.search.ProductSearch;
import com.vrp.demo.repository.GoodsGroupRepository;
import com.vrp.demo.repository.ProductRepository;
import com.vrp.demo.service.OrderItemService;
import com.vrp.demo.service.ProductService;
import com.vrp.demo.service.VehicleProductService;
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

@Service("productService")
public class ProductServiceImp extends BaseServiceImp<ProductRepository, Product, Long> implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VehicleProductService vehicleProductService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private GoodsGroupRepository goodsGroupRepository;

    private QueryTemplate buildQuery(ProductSearch search) {
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
    public List<ProductModel> find(ProductSearch search) {
        List<ProductModel> productModels = new ArrayList<>();
        QueryTemplate queryTemplate = buildQuery(search);
        List<Product> products = find(queryTemplate);
        for (Product product : products) {
            productModels.add(Product.convertToModel(product));
        }
        return productModels;
    }

    @Override
    public Page<ProductModel> search(ProductSearch search) {
        CommonUtils.setDefaultPageIfNull(search);
        QueryTemplate queryTemplate = buildQuery(search);
        Page<Product> products = search(queryTemplate);
        return products.map(product -> {
            ProductModel model = Product.convertToModel(product);
            return model;
        });
    }

    @Override
    @Transactional(readOnly = false)
    public ProductModel create(ProductModel productModel) throws CustomException {
        Product product = ProductModel.convertToEntity(productModel);
        List<Product> excludingProducts = product.getExcludingProducts();
        for (int i = 0; i < excludingProducts.size(); i++) {
            Product excludingProduct = excludingProducts.get(i);
            excludingProduct = productRepository.find(excludingProduct.getId());
            if (excludingProduct == null)
                throw CommonUtils.createException(Code.PRODUCT_ID_NOT_EXISTED);
            else
                excludingProducts.set(i, excludingProduct);
        }
        product.setExcludingProducts(excludingProducts);
        product.setExcludedProducts(excludingProducts);
        GoodsGroup goodsGroup = goodsGroupRepository.find(productModel.getGoodsGroup().getId());
        if (goodsGroup == null)
            throw CommonUtils.createException(Code.GOODS_GROUP_ID_NOT_EXISTED);
        else
            product.setGoodsGroups(goodsGroup);
        product = create(product);
        product.setCode("P" + product.getId());
        product = update(product);
        vehicleProductService.updateVehicleProduct(product);
        productModel = Product.convertToModel(product);
        return productModel;
    }

    @Override
    @Transactional(readOnly = false)
    public ProductModel update(ProductModel productModel) throws CustomException {
        Product product = productRepository.find(productModel.getId());
        if (product == null)
            throw CommonUtils.createException(Code.PRODUCT_ID_NOT_EXISTED);
        List<Product> excludingProducts = new ArrayList<>();
        List<Product> excludedProducts = new ArrayList<>();
        for (int i = 0; i < productModel.getExcludingProducts().size(); i++) {
            Product excludingProduct = productRepository.find(productModel.getExcludingProducts().get(i).getId());
            if (excludingProduct == null)
                throw CommonUtils.createException(Code.PRODUCT_ID_NOT_EXISTED);
            else {
                excludingProducts.add(excludingProduct);
                excludedProducts.add(excludingProduct);
            }
        }
        product.setExcludingProducts(excludingProducts);
        product.setExcludedProducts(excludedProducts);
        GoodsGroup goodsGroup = goodsGroupRepository.find(productModel.getGoodsGroup().getId());
        if (goodsGroup == null)
            throw CommonUtils.createException(Code.GOODS_GROUP_ID_NOT_EXISTED);
        else
            product.setGoodsGroups(goodsGroup);
        product = product.updateProduct(productModel);
        product = update(product);
        vehicleProductService.updateVehicleProduct(product);
        orderItemService.updateOrderItems(product);
        productModel = Product.convertToModel(product);
        return productModel;
    }

    @Override
    @Transactional(readOnly = false)
    public int delete(Long id) throws CustomException {
        Product Product = productRepository.find(id);
        if (Product == null)
            throw CommonUtils.createException(Code.VEHICLE_ID_NOT_EXISTED);
        return productRepository.delete(id);
    }

    @Override
    public ProductModel findOne(Long id) {
        Product Product = find(id);
        ProductModel ProductModel = Product.convertToModel(Product);
        return ProductModel;
    }

    @Override
    public List<Product> getProducts(ProductSearch search) {
        List<Long> ids = search.getProducts().stream().map(BaseModel::getId).collect(Collectors.toList());
        search.setIds(ids);
        QueryTemplate queryTemplate = buildQuery(search);
        List<Product> products = find(queryTemplate);
        return products;
    }

    @Override
    public ProductRepository getRepository() {
        return this.productRepository;
    }
}
