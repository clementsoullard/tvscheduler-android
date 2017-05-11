package com.clement.tvscheduler.object;

import java.util.Date;

/**
 * Created by Clément on 17/07/2016.
 */
public class TVStatus {
    public static final String TIME_CONSUMED_TODAY = "timeConsumedToday";

    public static final String TIME_PC_CONSUMED_TODAY = "timePcConsumedToday";

    public static final String DATE_OF_CREDIT = "dateOfCredit";

    public static final String RELAY_STATUS = "relayStatus";

    public static final String ACTIVE_STANDBY_STATE = "activeStandbyState";

    public static final String AMOUNT_OF_CREDIT_IN_MINUTES = "amountOfCreditInMinutes";

    public static final String REMAINING_TIME = "remainingTime";

    public static final String CURRENT_LOGGED_USER = "currentLoggedUser";


    private Boolean statusRelay;

    private String remainingTime;

    private String consumedTvToday;

    private String timePcConsumedToday;

    private String connectedUser;

    private Date nextCreditOn;

    private Boolean activeTV;

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

    public void setTvConsumedToday(String consumaedToday) {
        this.consumedTvToday = consumaedToday;
    }

    public String getTvConsumedToday() {
        return consumedTvToday;
    }

    public Boolean getActiveTV() {
        return activeTV;
    }

    public void setActiveTV(Boolean activeTV) {
        this.activeTV = activeTV;
    }

    public String getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(String connectedUser) {
        this.connectedUser = connectedUser;
    }

    public String getTimePcConsumedToday() {
        return timePcConsumedToday;
    }

    public void setTimePcConsumedToday(String timePcConsumedToday) {
        this.timePcConsumedToday = timePcConsumedToday;
    }
}