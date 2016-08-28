package com.clement.tvscheduler.task;

/**
 * Created by Clément on 17/07/2016.
 */
public class TVStatus {
    private Boolean statusRelay;


    private String remainingTime;

    public Boolean getStatusRelay() {
        return statusRelay;
    }

    public void setStatusRelay(Boolean statusRelay) {
        this.statusRelay = statusRelay;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingSecond(String remainingTime) {
        this.remainingTime = remainingTime;
    }
}