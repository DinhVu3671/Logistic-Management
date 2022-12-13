package com.vrptwga.operator.crossover;

import com.vrptwga.operator.Operator;

import java.util.HashMap;

public abstract class Crossover implements Operator {

    protected HashMap<String, Object> parameters = new HashMap<>();

    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

}




