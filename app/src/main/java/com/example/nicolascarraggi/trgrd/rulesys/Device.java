package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Device {

    protected String name, manufacturer, platform;
    protected Set<Event> events;
    protected Set<State> states;
    protected Set<Action> actions;

    public Device(String name, String manufacturer, String platform) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.platform = platform;
        this.events = new HashSet<>();
        this.states = new HashSet<>();
        this.actions = new HashSet<>();
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

    public Set<Event> getEvents() {
        return events;
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<Action> getActions() {
        return actions;
    }

}
