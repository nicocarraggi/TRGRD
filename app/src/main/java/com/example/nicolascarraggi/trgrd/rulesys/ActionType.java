package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class ActionType extends Type {

    private Set<Action> actions;

    public ActionType(String name) {
        super(name);
    }

    public Set<Action> getEvents() {
        return actions;
    }

    public void addAction(Action action){
        actions.add(action);
    }

    public void removeAction(Action action){
        actions.remove(action);
    }

}
