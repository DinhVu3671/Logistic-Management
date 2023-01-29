package com.vrp.demo.models.solution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRouterRequest {
    private String journeyId;
    private String routesId;
    private Integer orderId;
    private String status;
}
