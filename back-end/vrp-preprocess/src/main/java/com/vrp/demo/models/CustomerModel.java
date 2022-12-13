package com.vrp.demo.models;

import com.vrp.demo.models.enu.NodeType;
import com.vrptwga.concepts.Correlation;
import com.vrptwga.concepts.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModel extends NodeModel {

    private DepotModel clusterDepot;
    private Double penaltyCost;
    private List<DepotModel> availableDepots;

    public static com.vrp.demo.entity.tenant.Customer convertToEntity(CustomerModel customerModel) {
        ModelMapper modelMapper = new ModelMapper();
        com.vrp.demo.entity.tenant.Customer customer = modelMapper.map(customerModel, com.vrp.demo.entity.tenant.Customer.class);
        return customer;
    }

    public static Customer getCustomerInput(CustomerModel customer) {
        Customer customerInput = new Customer();
        customerInput.setId(customer.getId().intValue());
        customerInput.setCode(customer.getCode());
        HashMap<Integer, Double> distanceCustomers = new HashMap<>();
        HashMap<Integer, Double> distanceDepots = new HashMap<>();
        HashMap<Integer, Double> riskProbabilityCustomers = new HashMap<>();
        HashMap<Integer, Double> riskProbabilityDepots = new HashMap<>();
        HashMap<String, Correlation> correlations = new HashMap<>();
        for (CorrelationModel correlation : customer.getCorrelations()) {
            Correlation correlationInput = CorrelationModel.getCorrelationInput(correlation);
            correlations.put(correlationInput.getToNodeCode(), correlationInput);
            if (correlation.getToNodeType().equals(NodeType.CUSTOMER)) {
                distanceCustomers.put(correlation.getToNodeId().intValue(), new Double(correlation.getTime()));
                riskProbabilityCustomers.put(correlation.getToNodeId().intValue(), correlation.getRiskProbability());
            }
            if (correlation.getToNodeType().equals(NodeType.DEPOT)) {
                distanceDepots.put(correlation.getToNodeId().intValue(), new Double(correlation.getTime()));
                riskProbabilityDepots.put(correlation.getToNodeId().intValue(), correlation.getRiskProbability());
            }
        }
        customerInput.setCorrelations(correlations);
        customerInput.setStartTime(customer.getStartTime().intValue());
        customerInput.setEndTime(customer.getEndTime().intValue());
        customerInput.setPenaltyCost(customer.getPenaltyCost());
        customerInput.setDistanceCustomers(distanceCustomers);
        customerInput.setDistanceDepots(distanceDepots);
        customerInput.setRiskProbabilityCustomers(riskProbabilityCustomers);
        customerInput.setRiskProbabilityDepots(riskProbabilityDepots);
        return customerInput;
    }

    public static List<CustomerModel> getListCustomerModelsByID(List<Integer> ids, List<CustomerModel> customers) {
        List<CustomerModel> result = new ArrayList<>();
        for (Integer id : ids) {
            CustomerModel customer = new CustomerModel();
            for (CustomerModel customerFind : customers) {
                if (id == (customerFind.getId().intValue())) {
                    customer = customerFind;
//                    customer.setCorrelations(null);
//                    customer.setClusterDepot(null);
                }
            }
            result.add(customer);
        }
        return result;
    }

}
