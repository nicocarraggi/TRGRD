package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * A state that is true when a locating device is CURRENTLY AT a location.
 *
 */

public class LocationState extends State {

    private Location location;

    public LocationState(int id, String name, int iconResource, Device device, StateType stateType, boolean state) {
        super(id, name, iconResource, device, stateType, state);
        this.stateValueType = StateValueType.LOCATION;
        this.location = null;
    }

    public LocationState(int id, String name, int iconResource, Device device, StateType stateType, boolean state, Location location) {
        super(id, name, iconResource, device, stateType, state);
        this.stateValueType = StateValueType.LOCATION;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
