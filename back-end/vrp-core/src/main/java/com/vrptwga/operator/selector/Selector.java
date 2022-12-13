package com.vrptwga.operator.selector;

import com.vrptwga.operator.Operator;

import java.util.*;

public abstract class Selector implements Operator {

    protected HashMap<String,Object> parameters = new HashMap<>();

    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

}
