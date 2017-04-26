package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.Date;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * A state that is true when the time is between the FROM and TO time.
 *
 */

public class TimeState extends State {

    private Date timeFrom, timeTo;

    public TimeState(int id, String name, int iconResource, Device device, StateType stateType, boolean state) {
        super(id, name, iconResource, device, stateType, state);
        this.stateValueType = StateValueType.TIME;
        this.timeFrom = null;
        this.timeTo = null;
    }

    public TimeState(int id, String name, int iconResource, Device device, StateType stateType, boolean state, Date timeFrom, Date timeTo) {
        super(id, name, iconResource, device, stateType, state);
        this.stateValueType = StateValueType.TIME;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    // copy constructor
    public TimeState(int id, TimeState timeState, Date timeFrom, Date timeTo){
        super(id, timeState.getName(), timeState.getIconResource(), timeState.getDevice(), timeState.getStateType(), timeState.isState());
        this.stateValueType = StateValueType.TIME;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public Date getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Date getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Date timeTo) {
        this.timeTo = timeTo;
    }
}
