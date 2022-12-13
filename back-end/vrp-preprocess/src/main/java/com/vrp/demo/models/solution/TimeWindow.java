package com.vrp.demo.models.solution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeWindow {

    private int startTime;
    private int endTime;

    public TimeWindow(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeWindow() {
    }

    public TimeWindow(com.vrptwga.concepts.TimeWindow timeWindow) {
        this.startTime = timeWindow.getStartTime();
        this.endTime = timeWindow.getEndTime();
    }

}
