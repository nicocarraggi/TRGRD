package com.example.nicolascarraggi.trgrd.rulesys;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Location {

    private String id, name, address;
    private int iconResource;
    private LatLng latLng;
    // Save Place object?
    private boolean hasGeofence;
    private Geofence geofence;

    public Location(String id, String name, String address, int iconResource, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.iconResource = iconResource;
        this.latLng = latLng;
        this.hasGeofence = false;
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

    public boolean isHasGeofence() {
        return hasGeofence;
    }

    public Geofence getGeofence() {
        return geofence;
    }

    public void setGeofence(Geofence geofence) {
        this.geofence = geofence;
        this.hasGeofence = (geofence != null);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", iconResource=" + iconResource +
                ", latLng=" + latLng +
                ", hasGeofence=" + hasGeofence +
                '}';
    }
}
