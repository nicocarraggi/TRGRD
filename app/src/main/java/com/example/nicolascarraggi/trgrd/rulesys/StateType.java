package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class StateType extends Type {

    private Set<State> states;
    private State instanceState;

    // SKELETON constructor
    public StateType(int id, String name) {
        super(id, name, TypeType.STATE);
        this.states = new HashSet<>();
    }

    // INSTANCE constructor
    public StateType(int id, StateType stateType) {
        super(id, stateType.getName(), TypeType.STATE);
        this.devices = stateType.getDevices();
        this.states = stateType.getStates();
        this.isSkeleton = false;
        this.instanceState = null;
        this.hasInstanceItem = false;
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<State> getSkeletonStates() {
        Set<State> skeletonStates = new HashSet<>();
        for (State s: states){
            if (s.isSkeleton()) skeletonStates.add(s);
        }
        return skeletonStates;
    }

    public void addState(State state){
        states.add(state);
    }

    public void removeState(State state){
        states.remove(state);
    }

    public State getInstanceState() {
        return instanceState;
    }

    public void setInstanceState(State instanceState) {
        this.hasInstanceItem = (instanceState != null);
        this.instanceState = instanceState;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
