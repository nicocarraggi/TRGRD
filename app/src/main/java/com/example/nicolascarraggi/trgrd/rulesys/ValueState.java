package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * A state that is true when the value is between the FROM and TO values.
 *
 */

public class ValueState extends State {

    private int valueFrom, valueTo;

    // SKELETON constructor
    public ValueState(int id, String name, int iconResource, Device device, StateType stateType, boolean state) {
        super(id, name, iconResource, device, stateType, state);
        this.stateValueType = StateValueType.VALUE;
        this.valueFrom = 0;
        this.valueTo = 0;
    }

    public ValueState(int id, String name, int iconResource, Device device, StateType stateType, boolean state, int valueFrom, int valueTo) {
        super(id, name, iconResource, device, stateType, state);
        this.stateValueType = StateValueType.VALUE;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    public int getValueFrom() {
        return valueFrom;
    }

    public void setValueFrom(int valueFrom) {
        this.valueFrom = valueFrom;
    }

    public int getValueTo() {
        return valueTo;
    }

    public void setValueTo(int valueTo) {
        this.valueTo = valueTo;
    }
}
