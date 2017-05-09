package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class EventType extends Type {

    private Set<Event> events;

    public EventType(int id, String name) {
        super(id, name, TypeType.EVENT);
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

}
