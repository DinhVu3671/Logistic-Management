package com.vrp.demo.entity.tenant;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.models.VehicleModel;
import com.vrp.demo.models.VehicleProductModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vehicles_products")
public class VehicleProduct extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int maxNumber;

    public static VehicleProductModel convertToModel(VehicleProduct vehicleProduct) {
        ModelMapper modelMapper = new ModelMapper();
        VehicleProductModel vehicleProductModel = modelMapper.map(vehicleProduct,VehicleProductModel.class);
        VehicleModel vehicleModel = new VehicleModel();
        vehicleModel.setId(vehicleProduct.getId());
        vehicleProductModel.setVehicle(vehicleModel);
        return vehicleProductModel;
    }

    public VehicleProduct update(VehicleProductModel vehicleProductModel) {
        this.setMaxNumber(vehicleProductModel.getMaxNumber());
        return this;
    }

}
