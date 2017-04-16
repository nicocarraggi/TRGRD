package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Type {

    private String name;
    private Set<Device> devices;

    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void addDevice(Device device){
        devices.add(device);
    }

    public void removeDevice(Device device){
        devices.remove(device);
    }

}
