package com.vrp.demo.service.imp;

import com.vrp.demo.entity.tenant.Correlation;
import com.vrp.demo.entity.tenant.Customer;
import com.vrp.demo.entity.tenant.Depot;
import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.CustomerModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.enu.NodeType;
import com.vrp.demo.models.search.CorrelationSearch;
import com.vrp.demo.models.search.CustomerSearch;
import com.vrp.demo.repository.CustomerRepository;
import com.vrp.demo.repository.DepotRepository;
import com.vrp.demo.repository.OrderRepository;
import com.vrp.demo.service.CorrelationService;
import com.vrp.demo.service.CustomerService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service("customerService")
public class CustomerServiceImp extends BaseServiceImp<CustomerRepository, Customer, Long> implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CorrelationService correlationService;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DistanceMatrixService distanceMatrixService;
    @Autowired
    private OrderRepository orderRepository;

    private QueryTemplate buildQuery(CustomerSearch search) {
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

    private List<Correlation> getCorrelations(Customer customer) {
        CorrelationSearch correlationSearch = new CorrelationSearch();
        correlationSearch.setFromNodeCode(customer.getCode());
        return correlationService.findCorrelations(correlationSearch);
    }

    private List<Correlation> getCorrelations(Customer customer, List<Correlation> correlations) {
        List<Correlation> correlationsOfCustomer = correlations.stream().filter(correlation -> correlation.getFromNodeCode().equals(customer.getCode())).collect(Collectors.toList());
        return correlationsOfCustomer;
    }

    @Override
    public List<CustomerModel> find(CustomerSearch search) {
        List<CustomerModel> customerModels = new ArrayList<>();
        QueryTemplate queryTemplate = buildQuery(search);
        List<Customer> customers = find(queryTemplate);
        for (Customer customer : customers) {
            customerModels.add(Customer.convertToModel(customer));
        }
        return customerModels;
    }

    @Override
    public Page<CustomerModel> search(CustomerSearch search) {
        CommonUtils.setDefaultPageIfNull(search);
        QueryTemplate queryTemplate = buildQuery(search);
        Page<Customer> customers = search(queryTemplate);
        return customers.map(customer -> {
            CustomerModel model = Customer.convertToModel(customer);
            return model;
        });
    }

    @Override
    @Transactional(readOnly = false)
    public CustomerModel create(CustomerModel customerModel) {
        Customer customer = CustomerModel.convertToEntity(customerModel);
        distanceMatrixService.getCoordinate(customer);
        customer = create(customer);
        customer.setCode("C" + customer.getId());
        customer = update(customer);
        customerModel = Customer.convertToModel(customer);
        correlationService.createCorrelations(customerModel);
        return customerModel;
    }

    @Override
    @Transactional(readOnly = false)
    public CustomerModel update(CustomerModel customerModel) throws CustomException {
        Customer customer = customerRepository.find(customerModel.getId());
        if (customer == null)
            throw CommonUtils.createException(Code.CUSTOMER_ID_NOT_EXISTED);
        customer = customer.updateCustomer(customerModel);
        customer = update(customer);
        customerModel = Customer.convertToModel(customer);
        return customerModel;
    }

    @Override
    @Transactional(readOnly = false)
    public int delete(Long id) throws CustomException {
        Customer customer = customerRepository.find(id);
        if (customer == null)
            throw CommonUtils.createException(Code.CUSTOMER_ID_NOT_EXISTED);
        return customerRepository.delete(id);
    }

    @Override
    public CustomerModel findOne(Long id) {
        Customer customer = find(id);
        customer.setCorrelations(getCorrelations(customer));
        CustomerModel customerModel = Customer.convertToModel(customer);
        return customerModel;
    }

    @Override
    public List<CustomerModel> getClusteringCustomers() {
        List<Customer> customers = findAll();
        List<Order> orders = orderRepository.findAll();
        List<Correlation> correlations = correlationService.findAll();
        List<Depot> depots = depotRepository.findAll();
        List<CustomerModel> clusteringCustomers = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerModel clusteringCustomer = null;
            customer.setCorrelations(getCorrelations(customer, correlations));
            customer.setClusterDepot(CommonUtils.getDepotByCode(depots, customer.getNearestNode(NodeType.DEPOT).getToNodeCode()));
            clusteringCustomer = Customer.convertToModel(customer);
            clusteringCustomer.setClusterDepot(Depot.convertToModel(customer.getClusterDepot()));
            clusteringCustomer.setAvailableDepots(CommonUtils.getAvailableDepotsByCustomer(orders,customer,depots).stream().map(depot -> Depot.convertToModel(depot)).collect(Collectors.toList()));
            clusteringCustomers.add(clusteringCustomer);
        }
        return clusteringCustomers;
    }

    @Override
    public CustomerRepository getRepository() {
        return this.customerRepository;
    }

    @Override
    @Async
    @Transactional
    public void createCustomerData() {
        List<Customer> customers = null;
        try {
            customers = readCustomers();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        for (Customer customer : customers) {
            distanceMatrixService.getCoordinate(customer);
            customer = create(customer);
            customer.setCode("C" + customer.getId());
            customer = update(customer);
            correlationService.createCorrelationsData(Customer.convertToModel(customer));
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    @Override
    public List<CustomerModel> findAllWithCorrelations() {
        List<Customer> customers = findAll();
        List<Correlation> correlations = correlationService.findAll();
        List<CustomerModel> customerModels = new ArrayList<>();
        for (Customer customer : customers) {
            CustomerModel customerModel = null;
            customer.setCorrelations(getCorrelations(customer, correlations));
            customerModel = Customer.convertToModel(customer);
            customerModels.add(customerModel);
        }
        return customerModels;
    }

    private List<Customer> readCustomers() throws Exception {
        String fileExcel = "C:\\Users\\dell\\DATN\\data\\customers-chua-con-2.xlsx";
        List<Customer> customers = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(fileExcel);
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
//        rowIterator.next();         // đọc từ dòng thứ 2
        while (rowIterator.hasNext()) {
            Customer customer = new Customer();
            Row row = rowIterator.next();
//            Cell cellDistrict = row.getCell(1);
            Cell cellName = row.getCell(2);
//            Cell cellAddress = row.getCell(3);
            // lấy address
            customer.setName(cellName.getStringCellValue());
            customer.setAddress(cellName.getStringCellValue());
            customers.add(customer);
        }
        return customers;
    }
}
