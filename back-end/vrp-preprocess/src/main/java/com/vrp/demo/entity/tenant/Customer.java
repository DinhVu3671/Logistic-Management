package com.vrp.demo.entity.tenant;

import com.vrp.demo.models.CustomerModel;
import com.vrp.demo.models.enu.NodeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends Node {

    @Transient
    private Depot clusterDepot;
    @Column(name = "penalty_cost")
    private double penaltyCost;

    public static CustomerModel convertToModel(Customer customer) {
        ModelMapper modelMapper = new ModelMapper();
        CustomerModel customerModel = modelMapper.map(customer, CustomerModel.class);
        return customerModel;
    }

    public Customer updateCustomer(CustomerModel customerModel) {
        this.setAddress(customerModel.getAddress());
        this.setName(customerModel.getName());
        this.setLatitude(customerModel.getLatitude());
        this.setLongitude(customerModel.getLongitude());
        this.setStartTime(customerModel.getStartTime());
        this.setEndTime(customerModel.getEndTime());
        this.setPenaltyCost(customerModel.getPenaltyCost());
        return this;
    }

    public Correlation getNearestNode(NodeType nodeType) {
        List<Correlation> correlations = this.getCorrelations().stream()
                .filter(correlation -> (correlation.getToNodeType().equals(nodeType) && correlation.getDistance() > 0))
                .collect(Collectors.toList());
        List<Correlation> sortedCorrelations = correlations.stream()
                .sorted(Comparator.comparingInt(Correlation::getDistance))
                .collect(Collectors.toList());
        return sortedCorrelations.get(0);
    }

}
