package com.vrp.demo.models.enu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCategory {

    PERISHABLE("dễ hỏng"),
    AVOID_MOISTURE("tránh ẩm"),
    AVOID_HEAT("tránh nhiệt độ cao"),
    AVOID_STACKING("tránh xếp chồng"),
    ;

    private final String description;
}
