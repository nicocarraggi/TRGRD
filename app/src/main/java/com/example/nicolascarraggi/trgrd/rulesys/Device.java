package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Device {

    protected int id;
    protected String name, manufacturer, platform;
    protected int iconResource;
    protected HashMap<Integer, Event> events;
    protected HashMap<Integer, State> states;
    protected HashMap<Integer, Action> actions;
    protected HashMap<Integer, EventType> eventTypes;
    protected HashMap<Integer, StateType> stateTypes;
    protected HashMap<Integer, ActionType> actionTypes;

    public Device(int id, String name, String manufacturer, String platform, int iconResource) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.platform = platform;
        this.iconResource = iconResource;
        this.events = new HashMap<>();
        this.states = new HashMap<>();
        this.actions = new HashMap<>();
        this.eventTypes = new HashMap<>();
        this.stateTypes = new HashMap<>();
        this.actionTypes = new HashMap<>();
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

    public HashMap<Integer, Event> getEvents() {
        return events;
    }

    public HashMap<Integer, State> getStates() {
        return states;
    }

    public HashMap<Integer, Action> getActions() {
        return actions;
    }

    public HashMap<Integer, EventType> getEventTypes() {
        return eventTypes;
    }

    public HashMap<Integer, StateType> getStateTypes() {
        return stateTypes;
    }

    public HashMap<Integer, ActionType> getActionTypes() {
        return actionTypes;
    }

}
