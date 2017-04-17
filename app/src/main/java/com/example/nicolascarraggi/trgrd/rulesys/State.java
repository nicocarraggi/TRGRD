package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class State extends Event {

    private StateType stateType;
    private boolean state = false;

    public State(String name, Device device, StateType stateType, boolean state) {
        super(name, device, null);
        this.stateType = stateType;
        this.state = state;
    }

    public StateType getStateType() {
        return stateType;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        // IF state changes, tell listeners!
        boolean stateChanged = (this.state != state);
        this.state = state;
        if(stateChanged) stateChanged();
    }

    private void stateChanged(){
        for (Rule listener : super.rules){
            listener.stateChanged(this);
        }
    }

    @Override
    public synchronized void trigger() {
        this.state = !state;
        stateChanged();
    }

}