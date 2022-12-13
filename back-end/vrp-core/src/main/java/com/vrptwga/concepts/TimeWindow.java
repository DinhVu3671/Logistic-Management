package com.vrptwga.concepts;

public class TimeWindow {

    int startTime;
    int endTime;

    public TimeWindow(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeWindow() {
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public static TimeWindow createTimeWindowAtStartDepot(int startTime, int leaveStartDepotAt) {
        return new TimeWindow(startTime, leaveStartDepotAt);
    }

    public static TimeWindow createTimeWindowAtEndDepot(int arriveEndDepotAt) {
        return new TimeWindow(arriveEndDepotAt, 0);
    }

    public static TimeWindow createTimeWindowAtCustomer(int arriveCustomerAt, int timeService) {
        return new TimeWindow(arriveCustomerAt, arriveCustomerAt + timeService);
    }

    @Override
    public String toString() {
        return "TimeWindow{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }


}
