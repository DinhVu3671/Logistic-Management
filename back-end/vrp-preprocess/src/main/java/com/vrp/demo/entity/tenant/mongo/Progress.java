package com.vrp.demo.entity.tenant.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Progress {

    private Float percent;
    private Integer numDeliveredOrder;
    private Integer numNotYetDeliveredOrder;
    private Boolean isOutOfRoute;
    private Boolean isWarningLate;
    private Integer numOrderLate;

}
