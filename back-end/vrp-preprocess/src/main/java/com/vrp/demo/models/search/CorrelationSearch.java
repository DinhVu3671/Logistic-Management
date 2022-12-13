package com.vrp.demo.models.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CorrelationSearch extends BaseSearch{

    private String fromNodeCode;
    private String fromNodeName;
    private String toNodeCode;
    private String toNodeName;
}
