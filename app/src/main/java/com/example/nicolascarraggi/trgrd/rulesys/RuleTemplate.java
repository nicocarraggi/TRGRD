package com.example.nicolascarraggi.trgrd.rulesys;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by nicolascarraggi on 9/05/17.
 */

public class RuleTemplate {

    private int id;
    private String name;
    private ArrayList<Type> triggerTypes;
    private ArrayList<Type> actionTypes;
    private boolean isSkeleton;
    private RuleTemplate basedOn;
    private Rule rule;

    // SKELETON constructor
    public RuleTemplate(int id, String name){
        this.id = id;
        this.name = name;
        this.triggerTypes = new ArrayList<>();
        this.actionTypes = new ArrayList<>();
        this.rule = null;
    }

    // SKELETON constructor
    public RuleTemplate(int id, String name, ArrayList<Type> triggerTypes, ArrayList<Type> actionTypes) {
        this(id,name);
        this.triggerTypes = triggerTypes;
        this.actionTypes = actionTypes;
    }

    // INSTANCE constructor ( also creates instances of every type )
    // *** pass DeviceManager to get new IDs for type instances...
    public RuleTemplate(int id, RuleTemplate ruleTemplate, DeviceManager deviceManager){
        this(id,ruleTemplate.getName());
        this.isSkeleton = false;
        this.basedOn = ruleTemplate;
        for (Type t: ruleTemplate.getTriggerTypes()){
            if (t.isEventType()){
                EventType et = (EventType) t;
                EventType instance = new EventType(deviceManager.getNewId(),et);
                this.triggerTypes.add(instance);
                deviceManager.addEventTypeInstance(instance);
            } else if (t.isStateType()){
                StateType st = (StateType) t;
                StateType instance = new StateType(deviceManager.getNewId(),st);
                this.triggerTypes.add(instance);
                deviceManager.addStateTypeInstance(instance);
            }
        }
        for (Type t: ruleTemplate.getActionTypes()){
            if (t.isActionType()){
                ActionType at = (ActionType) t;
                ActionType instance = new ActionType(deviceManager.getNewId(),at);
                this.actionTypes.add(instance);
                deviceManager.addActionTypeInstance(instance);
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Type> getTriggerTypes() {
        return triggerTypes;
    }

    public void setTriggerTypes(ArrayList<Type> triggerTypes) {
        this.triggerTypes = triggerTypes;
    }

    public void addTriggerType(Type type){
        triggerTypes.add(type);
    }

    public ArrayList<Type> getActionTypes() {
        return actionTypes;
    }

    public void setActionTypes(ArrayList<Type> actionTypes) {
        this.actionTypes = actionTypes;
    }

    public void addActionType(Type type){
        actionTypes.add(type);
    }

    public RuleTemplate getBasedOn() {
        return basedOn;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    private boolean checkValidTypes(ArrayList<Type> types){
        for (Type t: types){
            if(t.isSkeleton() || !t.isHasInstanceItem()){
                return false;
            }
        }
        return true;
    }

    public boolean isInstanceValid(){
        if (this.isSkeleton){
            return false;
        } else {
            return (checkValidTypes(triggerTypes) && checkValidTypes(actionTypes));
        }
    }

}
