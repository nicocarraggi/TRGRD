package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * A state that is true when a locating device is CURRENTLY AT a location.
 *
 */

public class LocationState extends State {

    private Location location;

    private boolean simulation = true;

    // SKELETON constructor
    public LocationState(int id, String name, int iconResource, Device device, StateType stateType, boolean state, RuleEngine ruleEngine) {
        super(id, name, iconResource, device, stateType, state, ruleEngine);
        this.stateValueType = StateValueType.LOCATION;
        this.location = null;
    }

    public LocationState(int id, String name, int iconResource, Device device, StateType stateType, boolean state, Location location, RuleEngine ruleEngine) {
        this(id, name, iconResource, device, stateType, state, ruleEngine);
        this.location = location;
        this.isSkeleton=false;
        if(simulation) {
            // FOR SIMULATION ... this state is always TRUE TODO remove
            this.state = true;
        }
    }

    // copy constructor
    public LocationState(int id, LocationState locationState, Location location){
        this(id,locationState.getName(),locationState.getIconResource(),locationState.getDevice(),locationState.getStateType(),locationState.isState(),location, locationState.getRuleEngine());
    }

    @Override
    public void setState(boolean state) {
        if(simulation) {
            // FOR SIMULATION ... this state is always TRUE TODO remove
            super.setState(true);
        } else {
            super.setState(state);
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "LocationState{" +
                "state="+ super.toString()+
                ", location=" + location +
                '}';
    }
}
