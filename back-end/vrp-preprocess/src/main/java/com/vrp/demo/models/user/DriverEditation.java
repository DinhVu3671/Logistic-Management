package com.vrp.demo.models.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverEditation {

    private String driverEmail;
    private String phoneNumber;
    private double maxLoadWeight;
    private double maxCapacity;
    private double maxVelocity;

}
