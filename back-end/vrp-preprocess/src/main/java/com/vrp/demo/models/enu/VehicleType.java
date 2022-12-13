package com.vrp.demo.models.enu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  VehicleType {

    BIKE("xe máy"),
    TRUCK("xe tải"),
    ;

    private final String description;
}
