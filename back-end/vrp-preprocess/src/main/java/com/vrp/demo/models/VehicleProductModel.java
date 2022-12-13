package com.vrp.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleProductModel extends BaseModel{

    private VehicleModel vehicle;
    private ProductModel product;
    private Integer maxNumber;
}
