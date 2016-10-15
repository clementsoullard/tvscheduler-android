package com.clement.tvscheduler.task;

import java.util.Date;

/**
 *
 * Created by Clément on 17/07/2016.
 *
 */
public class TVStatus {

    private Boolean statusRelay;

    private String remainingTime;

    private String consumedToday;

    private Date nextCreditOn;

    private Integer nextCreditAmount;

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

    public Date getNextCreditOn() {
        return nextCreditOn;
    }

    public void setNextCreditOn(Date nextCreditOn) {
        this.nextCreditOn = nextCreditOn;
    }

    public Integer getNextCreditAmount() {
        return nextCreditAmount;
    }

    public void setNextCreditAmount(Integer nextCreditAmount) {
        this.nextCreditAmount = nextCreditAmount;
    }

    public void setConsumedToday(String consumaedToday) {
        this.consumedToday = consumaedToday;
    }

    public String getConsumedToday() {
        return consumedToday;
    }
}