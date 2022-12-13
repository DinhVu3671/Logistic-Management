package com.vrp.demo.entity.tenant;

import com.vrp.demo.models.DepotModel;
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
@Table(name = "depots")
public class Depot extends Node {

    @Column(name = "unloading_cost")
    private double unloadingCost;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "depots_products",
            joinColumns = @JoinColumn(name = "depot_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    public static DepotModel convertToModel(Depot depot) {
        ModelMapper modelMapper = new ModelMapper();
        DepotModel depotModel = modelMapper.map(depot, DepotModel.class);
        for (ProductModel product : depotModel.getProducts()) {
            if (product.getExcludingProducts() != null && !product.getExcludingProducts().isEmpty())
            product.ignoreRecursiveExcludingProducts();
        }
        return depotModel;
    }

    public Depot updateDepot(DepotModel customerModel) {
        this.setAddress(customerModel.getAddress());
        this.setName(customerModel.getName());
        this.setLatitude(customerModel.getLatitude());
        this.setLongitude(customerModel.getLongitude());
        this.setStartTime(customerModel.getStartTime());
        this.setEndTime(customerModel.getEndTime());
        this.setUnloadingCost(customerModel.getUnloadingCost());
        return this;
    }

}
