package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Event {

    protected int id;
    private EventType eventType;
    protected String name;
    protected int iconResource;
    protected EventValueType eventValueType;
    protected Device device;
    protected boolean usedInRule;
    protected RuleEngine ruleEngine;
    private MyTime lastTimeTriggered;
    protected boolean isSkeleton; // for events & states with values...
    protected Set<Rule> rules; // = listeners

    public Event(int id, String name, int iconResource, Device device, EventType eventType, RuleEngine ruleEngine) {
        this.id = id;
        this.name = name;
        this.iconResource = iconResource;
        this.device = device;
        this.eventType = eventType;
        if(eventType != null) eventType.addEvent(this); // eventType = null if this is a State
        this.usedInRule = false;
        this.ruleEngine = ruleEngine;
        this.eventValueType = EventValueType.NONE;
        this.isSkeleton = true;
        this.rules = new HashSet<Rule>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public EventValueType getEventValueType() {
        return eventValueType;
    }

    public Set<Rule> getRules() {
        return rules;
    }

    public RuleEngine getRuleEngine() {
        return ruleEngine;
    }

    public boolean isUsedInRule() {
        return usedInRule;
    }

    public boolean isSkeleton() {
        return isSkeleton;
    }

    public MyTime getLastTimeTriggered() {
        return lastTimeTriggered;
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
        this.lastTimeTriggered = new MyTime();
        //for (Rule rule : rules){
        //    rule.eventTriggered(this);
        //}
        ruleEngine.checkRules(rules);
    }

    public boolean isNormalEvent(){
        return eventValueType == EventValueType.NONE;
    }

    public boolean isValueEvent(){
        return eventValueType == EventValueType.VALUE;
    }

    public boolean isTimeEvent(){
        return eventValueType == EventValueType.TIME;
    }

    public boolean isInputActionEvent() { return eventValueType == EventValueType.INPUTACTION;}

    public boolean isLocationEvent(){
        return eventValueType == EventValueType.LOCATION;
    }

    public enum EventValueType {
        NONE, VALUE, INPUTACTION, TIME, LOCATION
    }
}
