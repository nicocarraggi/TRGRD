package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class EventType extends Type {

    private Set<Event> events;
    private Event instanceEvent;

    // SKELETON constructor
    public EventType(int id, String name) {
        super(id, name, TypeType.EVENT);
    }

    // INSTANCE constructor
    public EventType(int id, EventType eventType) {
        super(id, eventType.getName(), TypeType.EVENT);
        this.devices = eventType.getDevices();
        this.events = eventType.getEvents();
        this.isSkeleton = false;
        this.instanceEvent = null;
        this.hasInstanceItem = false;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event){
        events.add(event);
    }

    public void removeEvent(Event event){
        events.remove(event);
    }

    public Event getInstanceEvent() {
        return instanceEvent;
    }

    public void setInstanceEvent(Event instanceEvent) {
        this.hasInstanceItem = (instanceEvent != null);
        this.instanceEvent = instanceEvent;
    }
}
