package com.vrp.demo.models.enu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NodeType {
    DEPOT("điểm kho"),
    CUSTOMER("điểm khách"),
    ;
    private final String description;
}
