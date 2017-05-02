package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * An event that occurs when a locating device ARRIVES or LEAVES a location.
 *
 */

public class LocationEvent extends Event {

    private LocationEventType locationEventType;
    private Location location;

    public LocationEvent(int id, String name, int iconResource, Device device, EventType eventType, LocationEventType locationEventType) {
        super(id, name, iconResource, device, eventType);
        this.eventValueType = EventValueType.LOCATION;
        this.locationEventType = locationEventType;
        this.location = null;
    }

    public LocationEvent(int id, String name, int iconResource, Device device, EventType eventType, LocationEventType locationEventType, Location location) {
        this(id, name, iconResource, device, eventType, locationEventType);
        this.location = location;
        this.isSkeleton=false;
    }

    // copy constructor
    public LocationEvent(int id, LocationEvent locationEvent, Location location){
        this(id, locationEvent.getName(),locationEvent.getIconResource(),locationEvent.getDevice(),locationEvent.getEventType(),locationEvent.getLocationEventType(),location);
    }

    public LocationEventType getLocationEventType() {
        return locationEventType;
    }

    public void setLocationEventType(LocationEventType locationEventType) {
        this.locationEventType = locationEventType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public enum LocationEventType {
        ARRIVING,LEAVING
    }

}
