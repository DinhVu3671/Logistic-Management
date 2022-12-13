package com.vrp.demo.models.enu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryMode {

    FAST("nhanh"),
    MEDIUM("trung bình"),
    STANDARD("tiêu chuẩn"),
    ;
    private final String description;

}