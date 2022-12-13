package com.vrp.demo.models.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class JourneyDriverSearch extends BaseSearch{

    private Long solutionRoutingId;
    private Date intendReceiveTime;
    private Long driverId;
}
