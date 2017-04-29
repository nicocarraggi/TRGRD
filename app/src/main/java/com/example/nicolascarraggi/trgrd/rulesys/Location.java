package com.example.nicolascarraggi.trgrd.rulesys;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Location {

    private String id, name, address;
    private int iconResource;
    private LatLng latLng;
    // Save Place object?

    public Location(String id, String name, String address, int iconResource, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.iconResource = iconResource;
        this.latLng = latLng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
