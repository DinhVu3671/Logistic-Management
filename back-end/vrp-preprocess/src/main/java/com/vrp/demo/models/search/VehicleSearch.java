package com.vrp.demo.models.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleSearch extends BaseSearch{

    private String name;
    private Boolean available;
}
