package com.vrp.demo.models.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DepotSearch extends BaseSearch {
    private String name;
    private String code;
}
