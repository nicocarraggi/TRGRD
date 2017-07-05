package com.example.nicolascarraggi.trgrd.rulesys;

/**
 * Created by nicolascarraggi on 26/04/17.
 *
 * An event that occurs when the time is equal to the time value.
 *
 */

public class TimeEvent extends Event {

    private MyTime time;

    // SKELETON constructor
    public TimeEvent(int id, String name, int iconResource, Device device, EventType eventType, RuleEngine ruleEngine) {
        super(id, name, iconResource, device, eventType, ruleEngine);
        this.eventValueType = EventValueType.TIME;
        this.time = null;
    }

    public TimeEvent(int id, String name, int iconResource, Device device, EventType eventType, MyTime time, RuleEngine ruleEngine) {
        this(id, name, iconResource, device, eventType,ruleEngine);
        this.time = time;
        this.isSkeleton=false;
    }

    // copy constructor
    public TimeEvent(int id, TimeEvent timeEvent, MyTime time){
        this(id, timeEvent.getName(),timeEvent.getIconResource(),timeEvent.getDevice(),timeEvent.getEventType(), time, timeEvent.getRuleEngine());
    }

    public MyTime getTime() {
        return time;
    }

    public void setTime(MyTime time) {
        this.time = time;
    }
}
