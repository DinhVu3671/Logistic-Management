package com.vrptwga.concepts;


public class Correlation {

    private String fromNodeCode;
    private String toNodeCode;
    private int distance;
    private int time;
    private double riskProbability;

    public Correlation(String fromNodeCode, String toNodeCode, int distance, int time, double riskProbability) {
        this.fromNodeCode = fromNodeCode;
        this.toNodeCode = toNodeCode;
        this.distance = distance;
        this.time = time;
        this.riskProbability = riskProbability;
    }

    public Correlation() {
    }

    public String getFromNodeCode() {
        return fromNodeCode;
    }

    public void setFromNodeCode(String fromNodeCode) {
        this.fromNodeCode = fromNodeCode;
    }

    public String getToNodeCode() {
        return toNodeCode;
    }

    public void setToNodeCode(String toNodeCode) {
        this.toNodeCode = toNodeCode;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getRiskProbability() {
        return riskProbability;
    }

    public void setRiskProbability(double riskProbability) {
        this.riskProbability = riskProbability;
    }
}
