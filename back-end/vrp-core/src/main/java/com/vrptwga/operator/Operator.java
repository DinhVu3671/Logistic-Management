package com.vrptwga.operator;

import com.vrptwga.representation.Individual;

import java.util.HashMap;
import java.util.List;

public interface Operator {

    public List<Individual> execute();

    public void setParameters(HashMap<String, Object> parameters);

    public HashMap<String, Object> getParameters();

}
