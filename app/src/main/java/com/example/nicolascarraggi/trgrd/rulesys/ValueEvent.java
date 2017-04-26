package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * An event that occurs when something is GREATER than, LESS than or equal to a value.
 *
 * TODO keep old value ( to avoid repeating an event? maybe this is wanted? )
 *
 */

public class ValueEvent extends Event {

    private ValueEventType valueEventType;
    private int value;

    public ValueEvent(int id, String name, int iconResource, Device device, EventType eventType) {
        super(id, name, iconResource, device, eventType);
        this.eventValueType = EventValueType.VALUE;
        this.value = 0;
        this.valueEventType = null;
    }

    public ValueEvent(int id, String name, int iconResource, Device device, EventType eventType, ValueEventType valueEventType, int value) {
        super(id, name, iconResource, device, eventType);
        this.eventValueType = EventValueType.VALUE;
        this.valueEventType = valueEventType;
        this.value = value;
    }

    public ValueEventType getValueEventType() {
        return valueEventType;
    }

    public void setValueEventType(ValueEventType valueEventType) {
        this.valueEventType = valueEventType;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public enum ValueEventType {
        GREATER,EQUALS,LESS
    }

    public boolean check(int value){
        boolean result = false;
        if (valueEventType == ValueEventType.GREATER){
            return (value > this.value);
        } else if (valueEventType == ValueEventType.EQUALS){
            return (value == this.value);
        } else if (valueEventType == ValueEventType.LESS){
            return (value < this.value);
        }
        return result;
    }

    public void newValue(int value){
        if (check(value)){
            this.trigger();
        }
    }

}
