package com.vrp.demo.entity.tenant;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.models.VehicleModel;
import com.vrp.demo.models.VehicleProductModel;
import com.vrp.demo.models.enu.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {

    private String name;
    @Column(name = "max_load_weight")
    private double maxLoadWeight;
    @Column(name = "max_capacity")
    private double maxCapacity;
    private double length;
    private double width;
    private double height;
    @Column(name = "driver_name")
    private String driverName;
    @Column(name = "average_gas_consume")
    private double averageGasConsume;
    @Column(name = "gas_price")
    private double gasPrice;
    @Column(name = "average_fee_transport")
    private double averageFeeTransport;
    @Column(name = "min_velocity")
    private double minVelocity;
    @Column(name = "max_velocity")
    private double maxVelocity;
    @Column(name = "average_velocity")
    private double averageVelocity;
    @Column(name = "fixed_cost")
    private double fixedCost;
    @Enumerated(EnumType.STRING)
    private VehicleType type;
    private boolean available;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "vehicle")
    private List<VehicleProduct> vehicleProducts;
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "vehicle_exclude_goods_group",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "goods_group_id"))
    private List<GoodsGroup> excludedGoodsGroups;

    public static VehicleModel convertToModel(Vehicle vehicle) {
        ModelMapper modelMapper = new ModelMapper();
        VehicleModel vehicleModel = modelMapper.map(vehicle, VehicleModel.class);
        List<VehicleProductModel> vehicleProductModels = new ArrayList<>();
        if (vehicle.getVehicleProducts() != null && vehicle.getVehicleProducts().size() > 0)
            for (VehicleProduct vehicleProduct : vehicle.getVehicleProducts()) {
                VehicleModel model = new VehicleModel();
                model.setId(vehicle.getId());
                VehicleProductModel vehicleProductModel = VehicleProduct.convertToModel(vehicleProduct);
                vehicleProductModel.getProduct().ignoreRecursiveExcludingProducts();
                vehicleProductModels.add(vehicleProductModel);
            }
        vehicleModel.setVehicleProducts(vehicleProductModels);
        return vehicleModel;
    }

    public Vehicle updateVehicle(VehicleModel vehicleModel) {
        this.setName(vehicleModel.getName());
        this.setMaxLoadWeight(vehicleModel.getMaxLoadWeight());
        this.setLength(vehicleModel.getLength());
        this.setWidth(vehicleModel.getWidth());
        this.setHeight(vehicleModel.getHeight());
        this.setDriverName(vehicleModel.getDriverName());
        this.setMaxCapacity(Math.round(vehicleModel.getMaxCapacity() * 100.0) / 100.0);
        this.setAverageGasConsume(vehicleModel.getAverageGasConsume());
        this.setGasPrice(vehicleModel.getGasPrice());
        this.setAverageFeeTransport(Math.round(vehicleModel.getAverageFeeTransport() * 100.0) / 100.0);
        this.setMinVelocity(vehicleModel.getMinVelocity());
        this.setMaxVelocity(vehicleModel.getMaxVelocity());
        this.setAverageVelocity(vehicleModel.getAverageVelocity());
        this.setAvailable(vehicleModel.isAvailable());
        this.setType(vehicleModel.getType());
        this.setFixedCost(vehicleModel.getFixedCost());
        return this;
    }

    public int calculateMaxProduct(Product product) {
        int maxLoadNumProduct = 0;
        for (GoodsGroup goodsGroup : excludedGoodsGroups) {
            if (goodsGroup.equals(product.getGoodsGroups()))
                return maxLoadNumProduct;
        }
        int numCellVehicle = VehicleModel.calculateNumberCell(this.length, this.width, this.height);
        int numCellProduct = VehicleModel.calculateNumberCell(product.getLength(), product.getWidth(), product.getHeight());
        maxLoadNumProduct = numCellVehicle / numCellProduct;
        return maxLoadNumProduct;
    }

}
