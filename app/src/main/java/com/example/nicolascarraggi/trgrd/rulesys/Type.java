package com.example.nicolascarraggi.trgrd.rulesys;

import java.util.Set;

/**
 * Created by nicolascarraggi on 4/04/17.
 */

public class Type {

    private int id;
    private String name;
    private TypeType typeType;
    private Set<Device> devices;

    public Type(int id, String name, TypeType typeType) {
        this.id = id;
        this.name = name;
        this.typeType = typeType;
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

    public void addDevice(Device device){
        devices.add(device);
    }

    public void removeDevice(Device device){
        devices.remove(device);
    }

    public enum TypeType {
        EVENT, STATE, ACTION
    }

}
