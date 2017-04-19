package com.example.nicolascarraggi.trgrd.rulesys.devices;

import com.example.nicolascarraggi.trgrd.rulesys.Device;

/**
 * Created by nicolascarraggi on 5/04/17.
 */

public class Wearable extends Device {

    protected String wearType;
    protected String wearLocation;

    public Wearable(String name, String manufacturer, String platform, String wearType, String wearLocation, int iconResource) {
        super(name, manufacturer, platform, iconResource);
        this.wearType = wearType;
        this.wearLocation = wearLocation;
    }

    public String getWearType() {
        return wearType;
    }

    public void setWearType(String wearType) {
        this.wearType = wearType;
    }

    public String getWearLocation() {
        return wearLocation;
    }

    public void setWearLocation(String wearLocation) {
        this.wearLocation = wearLocation;
    }
}
