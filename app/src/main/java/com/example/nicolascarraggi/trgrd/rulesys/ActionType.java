package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class ActionType extends Type {

    private Set<Action> actions;
    private Action instanceAction;

    // SKELETON constructor
    public ActionType(int id, String name, int iconResource) {
        super(id, name, TypeType.ACTION,iconResource);
        this.actions = new HashSet<>();
    }

    // INSTANCE constructor
    public ActionType(int id, ActionType actionType, int iconResource) {
        super(id, actionType.getName(), TypeType.ACTION,iconResource);
        this.devices = actionType.getDevices();
        this.actions = actionType.getActions();
        this.isSkeleton = false;
        this.instanceAction = null;
        this.hasInstanceItem = false;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public Set<Action> getSkeletonActions() {
        Set<Action> skeletonActions = new HashSet<>();
        for (Action a: actions){
            if (a.isSkeleton()) skeletonActions.add(a);
        }
        return skeletonActions;
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
        this.hasInstanceItem = (action != null);
        this.instanceAction = action;
    }
}
