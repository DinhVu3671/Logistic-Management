package com.vrp.demo.utils;

import org.springframework.data.domain.Pageable;

import java.util.HashMap;

public class QueryTemplate {

    private String query;
    private HashMap<String, Object> parameterMap = new HashMap<>();
    private Pageable pageable;
    private boolean isNative = false;
    private Class resultType;

    public QueryTemplate(String query, HashMap<String, Object> parameterMap, Pageable pageable) {
        this.query = query;
        this.parameterMap = parameterMap;
        this.pageable = pageable;
    }

    public QueryTemplate(String query, HashMap<String, Object> parameterMap) {
        this.query = query;
        this.parameterMap = parameterMap;
    }

    public QueryTemplate(String query) {
        this.query = query;
    }

    public QueryTemplate(String query, HashMap<String, Object> parameterMap, Pageable pageable, boolean isNative, Class resultType) {
        this.query = query;
        this.parameterMap = parameterMap;
        this.pageable = pageable;
        this.isNative = isNative;
        this.resultType = resultType;
    }

    public QueryTemplate() {
    }

    public String getQuery() {
        return this.query;
    }

    public HashMap<String, Object> getParameterMap() {
        return this.parameterMap;
    }

    public Pageable getPageable() {
        return this.pageable;
    }

    public boolean isNative() {
        return this.isNative;
    }

    public Class getResultType() {
        return this.resultType;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setParameterMap(HashMap<String, Object> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public void setNative(boolean isNative) {
        this.isNative = isNative;
    }

    public void setResultType(Class resultType) {
        this.resultType = resultType;
    }
}
