//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.vrp.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vrp.demo.entity.common.User;
import com.vrp.demo.entity.tenant.Vehicle;
import com.vrp.demo.validator.NullOrNotBlank;
import com.vrp.demo.validator.PhoneNumber;
import com.vrp.demo.validator.UniqueField;
import com.vrp.demo.validator.UniqueField.List;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@List({@UniqueField(
        fieldName = "email",
        entityClass = User.class,
        message = "{user.validation.email.existed}"
), @UniqueField(
        fieldName = "userName",
        entityClass = User.class,
        message = "{user.validation.userName.existed}"
)})
public class DriverModel extends BaseModel {
    @NullOrNotBlank(
            message = "{user.validation.userName.null-or-blank}"
    )
    private @Size(
            max = 64,
            min = 6,
            message = "{user.validation.userName.length}"
    ) String userName;
    @NullOrNotBlank(
            message = "{user.validation.email.null-or-blank}"
    )
    private @Email(
            message = "{user.pattern.email}"
    ) String email;
    @NullOrNotBlank(
            message = "{user.validation.password.null-or-blank}"
    )
    private @Size(
            max = 64,
            min = 6,
            message = "{user.validation.password.length}"
    ) String password;
    @NullOrNotBlank(
            message = "{user.validation.fullName.null-or-blank}"
    )
    private String fullName;
    private String avatar;
    @PhoneNumber(
            message = "{user.validation.phoneNumber.format}"
    )
    private String phoneNumber;
    private Boolean active;
    private RoleModel role;
    @JsonInclude(Include.NON_NULL)
    private MultipartFile image;
    private Double averageFreeTransport;
    private Double averageGasConsume;
    private Double averageVelocity;
    private Double fixedCost;
    private Double gasPrice;
    private Double height;
    private Double width;
    private Double length;
    private Double maxCapacity;
    private Double maxLoadWeight;
    private Double maxVelocity;
    private Double minVelocity;
    private String name;
    private String type;




    public static User convertToUserEntity(DriverModel driverModel) {
        ModelMapper modelMapper = new ModelMapper();
        User user = (User)modelMapper.map(driverModel, User.class);
        return user;
    }

    public static Vehicle convertToVehicleEntity(DriverModel driverModel) {
        ModelMapper modelMapper = new ModelMapper();
        Vehicle vehicle = (Vehicle) modelMapper.map(driverModel, Vehicle.class);
        return vehicle;
    }

    public static DriverModel ignoreField(DriverModel driverModel) {
        driverModel.setDeleted((Boolean)null);
        driverModel.getRole().setDeleted((Boolean)null);
        return driverModel;
    }
}
