package com.example.nicolascarraggi.trgrd.rulesys;

import android.util.Log;

import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Type {

    private int id;
    private String name;
    private TypeType typeType;
    protected Set<Device> devices;
    protected boolean isSkeleton;
    protected boolean hasInstanceItem;
    protected int iconResource;

    public Type(int id, String name, TypeType typeType, int iconResource) {
        this.id = id;
        this.name = name;
        this.typeType = typeType;
        this.isSkeleton = true;
        this.hasInstanceItem = false;
        this.iconResource = iconResource;
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

    public TypeType getTypeType() {
        return typeType;
    }

    public boolean isEventType(){
        return typeType == TypeType.EVENT;
    }

    public boolean isStateType(){
        return typeType == TypeType.STATE;
    }

    public boolean isActionType(){
        return typeType == TypeType.ACTION;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public void addDevice(Device device){
        devices.add(device);
    }

    public void removeDevice(Device device){
        devices.remove(device);
    }

    public boolean isSkeleton() {
        return isSkeleton;
    }

    public boolean isHasInstanceItem() {
        return hasInstanceItem;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public enum TypeType {
        EVENT, STATE, ACTION
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeType=" + typeType +
                ", isSkeleton=" + isSkeleton +
                ", hasInstanceItem=" + hasInstanceItem +
                '}';
    }
}
