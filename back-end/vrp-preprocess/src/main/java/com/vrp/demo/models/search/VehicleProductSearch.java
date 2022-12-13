package com.vrp.demo.models.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleProductSearch extends BaseSearch {

    private Long productId;
    private Long vehicleId;

}
