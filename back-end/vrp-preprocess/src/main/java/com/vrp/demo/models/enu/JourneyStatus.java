package com.vrp.demo.models.enu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JourneyStatus {

    NOT_YET_START("chưa bắt đầu"),
    IN_PROGRESS("đang tiến hành"),
    FINISHED("đã kết thúc"),
    ;

    private final String description;
}
