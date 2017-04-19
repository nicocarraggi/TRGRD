package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Device {

    protected String name, manufacturer, platform;
    protected int iconResource;
    protected Set<Event> events;
    protected Set<State> states;
    protected Set<Action> actions;
    protected Set<EventType> eventTypes;
    protected Set<StateType> stateTypes;
    protected Set<ActionType> actionTypes;

    public Device(String name, String manufacturer, String platform, int iconResource) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.platform = platform;
        this.iconResource = iconResource;
        this.events = new HashSet<>();
        this.states = new HashSet<>();
        this.actions = new HashSet<>();
        this.eventTypes = new HashSet<>();
        this.stateTypes = new HashSet<>();
        this.actionTypes = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<Action> getActions() {
        return actions;
    }

    public Set<EventType> getEventTypes() {
        return eventTypes;
    }

    public Set<StateType> getStateTypes() {
        return stateTypes;
    }

    public Set<ActionType> getActionTypes() {
        return actionTypes;
    }
}
