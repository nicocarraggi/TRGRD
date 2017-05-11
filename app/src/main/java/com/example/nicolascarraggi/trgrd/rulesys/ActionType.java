package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class ActionType extends Type {

    private Set<Action> actions;
    private Action instanceAction;

    // SKELETON constructor
    public ActionType(int id, String name) {
        super(id, name, TypeType.ACTION);
    }

    // INSTANCE constructor
    public ActionType(int id, ActionType actionType) {
        super(id, actionType.getName(), TypeType.ACTION);
        this.devices = actionType.getDevices();
        this.actions = actionType.getActions();
        this.isSkeleton = false;
        this.instanceAction = null;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public void addAction(Action action){
        actions.add(action);
    }

    public void removeAction(Action action){
        actions.remove(action);
    }

    public Action getInstanceAction() {
        return instanceAction;
    }

    public void setInstanceAction(Action action) {
        this.instanceAction = action;
    }
}
