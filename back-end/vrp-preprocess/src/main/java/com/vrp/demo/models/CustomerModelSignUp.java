package com.vrp.demo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vrp.demo.entity.common.User;
import com.vrp.demo.entity.tenant.Customer;
import com.vrp.demo.validator.NullOrNotBlank;
import com.vrp.demo.validator.PhoneNumber;
import com.vrp.demo.validator.UniqueField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UniqueField.List({
        @UniqueField(fieldName = "email", entityClass = User.class, message = "{user.validation.email.existed}"),
        @UniqueField(fieldName = "userName", entityClass = User.class, message = "{user.validation.userName.existed}")
})
public class CustomerModelSignUp extends BaseModel {

    @NullOrNotBlank(message = "{user.validation.userName.null-or-blank}")
    @Size(max = 64, min = 6, message = "{user.validation.userName.length}")
    private String userName;
    @NullOrNotBlank(message = "{user.validation.email.null-or-blank}")
    @Email(message = "{user.pattern.email}")
    private String email;
    @NullOrNotBlank(message = "{user.validation.password.null-or-blank}")
    @Size(max = 64, min = 6, message = "{user.validation.password.length}")
    private String password;
    @NullOrNotBlank(message = "{user.validation.fullName.null-or-blank}")
    private String fullName;
    private String avatar;
    @PhoneNumber(message = "{user.validation.phoneNumber.format}")
    private String phoneNumber;
    private Boolean active;
    private RoleModel role;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MultipartFile image;
    private String address;
    private Long endTime;
    private Long startTime;
    private String name;
    private Double latitude;
    private Double longitude;

    public static User convertToUserEntity (CustomerModelSignUp customerModelSignUp) {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(customerModelSignUp, User.class);
        return user;
    }
    public static Customer convertToCustomerEntity (CustomerModelSignUp customerModelSignUp) {
        ModelMapper modelMapper = new ModelMapper();
        Customer customer = modelMapper.map(customerModelSignUp, Customer.class);
        return customer;
    }
}
