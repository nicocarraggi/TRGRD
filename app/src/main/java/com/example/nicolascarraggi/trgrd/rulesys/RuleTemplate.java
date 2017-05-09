package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.ArrayList;

/**
 * Created by nicolascarraggi on 9/05/17.
 */

public class RuleTemplate {

    private int id;
    private String name;
    private ArrayList<Type> triggerTypes;
    private ArrayList<Type> actionTypes;

    public RuleTemplate(int id, String name, ArrayList<Type> triggerTypes, ArrayList<Type> actionTypes) {
        this.id = id;
        this.name = name;
        this.triggerTypes = triggerTypes;
        this.actionTypes = actionTypes;
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

    public ArrayList<Type> getActionTypes() {
        return actionTypes;
    }

    public void setActionTypes(ArrayList<Type> actionTypes) {
        this.actionTypes = actionTypes;
    }
}
