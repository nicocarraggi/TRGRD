package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class State extends Event {

    protected StateType stateType;
    protected boolean state = false;
    protected StateValueType stateValueType;

    public State(int id, String name, int iconResource, Device device, StateType stateType, boolean state, RuleEngine ruleEngine) {
        super(id, name, iconResource, device, null, ruleEngine);
        this.stateType = stateType;
        stateType.addState(this);
        this.state = state;
        this.stateValueType = State.StateValueType.NONE;
    }

    public StateType getStateType() {
        return stateType;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        // IF state changes, tell listeners!
        boolean stateChanged = (this.state != state);
        this.state = state;
        if(stateChanged) stateChanged();
    }

    private void stateChanged(){
        Set<Rule> toCheck = new HashSet<>();
        for (Rule rule : super.rules){
            if(rule.stateChangedNeedsCheck(this)) toCheck.add(rule);
        }
        ruleEngine.checkRules(toCheck);
    }

    public StateValueType getStateValueType() {
        return stateValueType;
    }

    @Override
    public synchronized void trigger() {
        this.state = !state;
        stateChanged();
    }

    public boolean isNormalState() {
        return stateValueType == StateValueType.NONE;
    }

    public boolean isValueState() {
        return stateValueType == StateValueType.VALUE;
    }

    public boolean isTimeState() {
        return stateValueType == StateValueType.TIME;
    }

    public boolean isCalendarState() {
        return stateValueType == StateValueType.CALENDAR;
    }

    public boolean isLocationState() {
        return stateValueType == StateValueType.LOCATION;
    }

    public enum StateValueType {
        NONE, VALUE, TIME, CALENDAR, LOCATION
    }

    @Override
    public String toString() {
        return "State{" +
                "stateType=" + stateType +
                ", state=" + state +
                ", stateValueType=" + stateValueType +
                '}';
    }
}