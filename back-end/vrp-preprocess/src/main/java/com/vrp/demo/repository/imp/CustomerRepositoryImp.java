package com.vrp.demo.repository.imp;

import com.vrp.demo.entity.tenant.Customer;
import com.vrp.demo.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "customerRepository")
public class CustomerRepositoryImp extends BaseRepositoryImp<Customer, Long> implements CustomerRepository {

    public CustomerRepositoryImp() {
        super(Customer.class);
    }

}
