package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 3/05/17.
 */

public class ValueAction extends Action {

    private int value;
    private ValueActionType valueActionType;

    // SKELETON constructor
    public ValueAction(int id, String name, int iconResource, Device device, ActionType actionType) {
        super(id, name, iconResource, device, actionType, null);
        this.actionValueType = ActionValueType.VALUE;
        this.valueActionType = ValueActionType.NONE;
        this.value = 0;
    }

    public ValueAction(int id, String name, int iconResource, Device device, ActionType actionType, int value, ValueActionType valueActionType, Callable callable) {
        this(id, name, iconResource, device, actionType);
        this.callable = callable;
        this.valueActionType = valueActionType;
        this.value = value;
        this.isSkeleton = false;
    }

    // copy constructor
    public ValueAction(int id, ValueAction notificationAction, int value, ValueActionType valueActionType, Callable callable){
        this(id, notificationAction.getName(),notificationAction.getIconResource(),notificationAction.getDevice(),notificationAction.getActionType(),value, valueActionType, callable);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ValueActionType getValueActionType() {
        return valueActionType;
    }

    public void setValueActionType(ValueActionType valueActionType) {
        this.valueActionType = valueActionType;
    }

    public void setCallable(Callable callable) {
        this.callable = callable;
    }

    public enum ValueActionType {
        NONE,ADD,SUBTRACT,SET
    }

    @Override
    public String toString() {
        return "ValueAction{value="+value+"}";
    }
}
