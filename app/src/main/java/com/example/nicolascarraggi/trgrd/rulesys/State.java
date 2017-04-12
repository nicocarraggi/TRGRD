package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class State extends Event {

    private boolean state = false;

    public State(String name, Device device, boolean state) {
        super(name, device);
        this.state = state;
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