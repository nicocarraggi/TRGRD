package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * A state that is true when the time is between the FROM and TO time.
 *
 */

public class TimeState extends State {

    private MyTime timeFrom, timeTo;

    public TimeState(int id, String name, int iconResource, Device device, StateType stateType, boolean state) {
        super(id, name, iconResource, device, stateType, state);
        this.stateValueType = StateValueType.TIME;
        this.timeFrom = null;
        this.timeTo = null;
    }

    public TimeState(int id, String name, int iconResource, Device device, StateType stateType, boolean state, MyTime timeFrom, MyTime timeTo) {
        super(id, name, iconResource, device, stateType, state);
        this.stateValueType = StateValueType.TIME;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.isSkeleton = false;
    }

    // copy constructor
    public TimeState(int id, TimeState timeState, MyTime timeFrom, MyTime timeTo){
        super(id, timeState.getName(), timeState.getIconResource(), timeState.getDevice(), timeState.getStateType(), timeState.isState());
        this.stateValueType = StateValueType.TIME;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.isSkeleton = false;
    }

    public MyTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(MyTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public MyTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(MyTime timeTo) {
        this.timeTo = timeTo;
    }
}
