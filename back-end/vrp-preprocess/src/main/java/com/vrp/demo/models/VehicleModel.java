package com.vrp.demo.models;

import com.vrp.demo.models.enu.VehicleType;
import com.vrptwga.concepts.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("vehicle")
public class VehicleModel extends BaseModel {

    private static final double CELL_LENGTH = 0.01;
    private static final double CELL_WIDTH = 0.01;
    private static final double CELL_HEIGHT = 0.01;

    private String name;
    private double maxLoadWeight;
    private double maxCapacity;
    private double length;
    private double width;
    private double height;
    private String driverName;
    private double averageGasConsume;
    private double gasPrice;
    private double averageFeeTransport;
    private double minVelocity;
    private double maxVelocity;
    private double averageVelocity;
    private double fixedCost;
    private boolean available;
    private VehicleType type;
    private List<VehicleProductModel> vehicleProducts;
    private List<GoodsGroupModel> excludedGoodsGroups;

    public static com.vrp.demo.entity.tenant.Vehicle convertToEntity(VehicleModel vehicleModel) {
        ModelMapper modelMapper = new ModelMapper();
        com.vrp.demo.entity.tenant.Vehicle vehicle = modelMapper.map(vehicleModel, com.vrp.demo.entity.tenant.Vehicle.class);
        return vehicle;
    }

    public static int calculateNumberCell(double length, double width, double height) {
        return (int) (length * width * height / (CELL_LENGTH * CELL_WIDTH * CELL_HEIGHT));
    }

    public Integer getMaxNumProduct(long productId){
        for (VehicleProductModel vehicleProduct : vehicleProducts) {
            if (vehicleProduct.getProduct().getId() == productId)
                return vehicleProduct.getMaxNumber();
        }
        return null;
    }

    public static Vehicle getVehicleInput(VehicleModel vehicle) {
        Vehicle vehicleInput = new Vehicle();
        vehicleInput.setId(vehicle.getId().intValue());
        vehicleInput.setFuelCost(vehicle.getAverageFeeTransport());
        vehicleInput.setFixedCost(vehicle.getFixedCost());
        vehicleInput.setMaxCapacity(vehicle.getMaxCapacity());
        vehicleInput.setMaxLoadWeight(vehicle.getMaxLoadWeight());
        vehicleInput.setName(vehicle.getName());
        vehicleInput.setCapacity((int) vehicle.getMaxLoadWeight());
        vehicleInput.setVelocity(vehicle.getAverageVelocity());
        vehicleInput.setArea(vehicle.getLength()*vehicle.getWidth());
        return vehicleInput;
    }

}
