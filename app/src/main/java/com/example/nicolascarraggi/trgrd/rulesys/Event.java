package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Event {

    protected String name;
    protected Device device;
    protected boolean usedInRule;
    protected Set<Rule> rules; // = listeners

    public Event(String name, Device device) {
        this.name = name;
        this.device = device;
        this.usedInRule = false;
        this.rules = new HashSet<Rule>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Device getDevice() {
        return device;
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
