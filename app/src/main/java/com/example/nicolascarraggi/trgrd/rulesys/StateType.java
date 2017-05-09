package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class StateType extends Type {

    private Set<State> states;

    public StateType(int id, String name) {
        super(id, name, TypeType.STATE);
    }

    public Set<State> getEvents() {
        return states;
    }

    public void addState(State state){
        states.add(state);
    }

    public void removeState(State state){
        states.remove(state);
    }
}
