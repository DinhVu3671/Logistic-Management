package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.Correlation;
import com.vrp.demo.entity.tenant.Depot;
import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.DepotModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.search.CorrelationSearch;
import com.vrp.demo.models.search.DepotSearch;
import com.vrp.demo.models.search.ProductSearch;
import com.vrp.demo.repository.DepotRepository;
import com.vrp.demo.service.CorrelationService;
import com.vrp.demo.service.DepotService;
import com.vrp.demo.service.ProductService;
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

@Service("depotService")
public class DepotServiceImp extends BaseServiceImp<DepotRepository, Depot, Long> implements DepotService {

    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CorrelationService correlationService;
    @Autowired
    private ProductService productService;

    private QueryTemplate buildQuery(DepotSearch search) {
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
    public List<DepotModel> find(DepotSearch search) {
        List<DepotModel> depotModels = new ArrayList<>();
        QueryTemplate queryTemplate = buildQuery(search);
        List<Depot> depots = find(queryTemplate);
        for (Depot depot : depots) {
            depotModels.add(Depot.convertToModel(depot));
        }
        return depotModels;
    }

    @Override
    public Page<DepotModel> search(DepotSearch search) {
        CommonUtils.setDefaultPageIfNull(search);
        QueryTemplate queryTemplate = buildQuery(search);
        Page<Depot> depots = search(queryTemplate);
        return depots.map(depot -> {
            DepotModel model = Depot.convertToModel(depot);
            return model;
        });
    }

    @Override
    @Transactional(readOnly = false)
    public DepotModel create(DepotModel depotModel) {
        ProductSearch search = new ProductSearch();
        search.setProducts(depotModel.getProducts());
        List<Product> products = productService.getProducts(search);
        Depot depot = DepotModel.convertToEntity(depotModel);
        depot.setProducts(null);
        depot = create(depot);
        depot.setCode("D" + depot.getId());
        depot.setProducts(products);
        depot = update(depot);
        depotModel = Depot.convertToModel(depot);
        correlationService.createCorrelations(depotModel);
        return depotModel;
    }

    @Override
    @Transactional(readOnly = false)
    public DepotModel update(DepotModel depotModel) throws CustomException {
        ProductSearch search = new ProductSearch();
        search.setProducts(depotModel.getProducts());
        List<Product> products = productService.getProducts(search);
        Depot depot = depotRepository.find(depotModel.getId());
        if (depot == null)
            throw CommonUtils.createException(Code.DEPOT_ID_NOT_EXISTED);
        depot = depot.updateDepot(depotModel);
        depot.setProducts(products);
        depot = update(depot);
        depotModel = Depot.convertToModel(depot);
        return depotModel;
    }

    @Override
    @Transactional(readOnly = false)
    public int delete(Long id) throws CustomException {
        Depot Depot = depotRepository.find(id);
        if (Depot == null)
            throw CommonUtils.createException(Code.DEPOT_ID_NOT_EXISTED);
        return depotRepository.delete(id);
    }

    @Override
    public DepotModel findOne(Long id) {
        Depot depot = find(id);
        CorrelationSearch correlationSearch = new CorrelationSearch();
        correlationSearch.setFromNodeCode(depot.getCode());
        List<Correlation> correlations = correlationService.findCorrelations(correlationSearch);
        depot.setCorrelations(correlations);
        DepotModel DepotModel = Depot.convertToModel(depot);
        return DepotModel;
    }

    private List<Correlation> getCorrelations(Depot depot, List<Correlation> correlations) {
        List<Correlation> correlationsOfDepot = correlations.stream().filter(correlation -> correlation.getFromNodeCode().equals(depot.getCode())).collect(Collectors.toList());
        return correlationsOfDepot;
    }

    @Override
    public List<DepotModel> findAllWithCorrelations() {
        List<Depot> depots = findAll();
        List<Correlation> correlations = correlationService.findAll();
        List<DepotModel> depotModels = new ArrayList<>();
        for (Depot depot : depots) {
            DepotModel depotModel = null;
            depot.setCorrelations(getCorrelations(depot, correlations));
            depotModel = Depot.convertToModel(depot);
            depotModels.add(depotModel);
        }
        return depotModels;
    }

    @Override
    public DepotRepository getRepository() {
        return this.depotRepository;
    }
}
