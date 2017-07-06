package com.example.nicolascarraggi.trgrd.rulesys;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class EventType extends Type {

    private Set<Event> events;
    private Event instanceEvent;

    // SKELETON constructor
    public EventType(int id, String name, int iconResource) {
        super(id, name, TypeType.EVENT,iconResource);
        this.events = new HashSet<>();
    }

    // INSTANCE constructor
    public EventType(int id, EventType eventType,int iconResource) {
        super(id, eventType.getName(), TypeType.EVENT,iconResource);
        this.devices = eventType.getDevices();
        this.events = eventType.getEvents();
        this.isSkeleton = false;
        this.instanceEvent = null;
        this.hasInstanceItem = false;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Set<Event> getSkeletonEvents() {
        Set<Event> skeletonEvents = new HashSet<>();
        for (Event e: events){
            if (e.isSkeleton()) skeletonEvents.add(e);
        }
        return skeletonEvents;
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
