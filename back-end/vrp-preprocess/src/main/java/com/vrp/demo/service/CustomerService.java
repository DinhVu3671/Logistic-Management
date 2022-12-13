package com.vrp.demo.service;

import com.vrp.demo.entity.tenant.Customer;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.CustomerModel;
import com.vrp.demo.models.search.CustomerSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService extends BaseService<Customer, Long>{

    public List<CustomerModel> find(CustomerSearch search);

    public Page<CustomerModel> search(CustomerSearch search);

    public CustomerModel create(CustomerModel customerModel);

    public CustomerModel update(CustomerModel customerModel) throws CustomException;

    public int delete(Long id) throws CustomException;

    public CustomerModel findOne(Long id);

    public List<CustomerModel> getClusteringCustomers();

    public void createCustomerData();

    public List<CustomerModel> findAllWithCorrelations();

}
