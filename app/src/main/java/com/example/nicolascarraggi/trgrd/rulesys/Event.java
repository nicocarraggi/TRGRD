package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Event {

    private EventType eventType;
    protected String name;
    protected int iconResource;
    protected Device device;
    protected boolean usedInRule;
    protected Set<Rule> rules; // = listeners

    public Event(String name, int iconResource, Device device, EventType eventType) {
        this.name = name;
        this.iconResource = iconResource;
        this.device = device;
        this.eventType = eventType;
        this.usedInRule = false;
        this.rules = new HashSet<Rule>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public Device getDevice() {
        return device;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public boolean isUsedInRule() {
        return usedInRule;
    }

    public synchronized void addRule(Rule rule){
        rules.add(rule);
        this.usedInRule = true;
    }

    public synchronized void removeRule(Rule rule){
        rules.remove(rule);
        if(rules.isEmpty()) this.usedInRule = false;
    }

    public synchronized void trigger(){
        for (Rule rule : rules){
            rule.eventTriggered(this);
        }
    }
}
