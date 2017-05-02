package com.example.nicolascarraggi.trgrd.rulesys;

import android.content.Context;

import com.example.nicolascarraggi.trgrd.rulesys.devices.AndroidPhone;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Clock;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Geofences;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Pebble;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 11/04/17.
 */

public class DeviceManager {

    // TODO add rule ID system
    private int newId = 1;
    // TODO get and write Events,States,Actions & their types from and to database?

    private HashMap<Integer,Device> devices;
    private HashMap<Integer,EventType> eventTypes;
    private HashMap<Integer,StateType> stateTypes;
    private HashMap<Integer,ActionType> actionTypes;

    // EventTypes
    private EventType evAlarmAlert = new EventType(getNewId(),"alarm alert");
    private EventType evAlarmSnooze = new EventType(getNewId(),"alarm snooze");
    private EventType evAlarmDismiss = new EventType(getNewId(),"alarm dismiss");
    private EventType evAlarmDone = new EventType(getNewId(),"alarm done");
    private EventType evCallInc = new EventType(getNewId(),"call incoming");
    private EventType evButtonPress = new EventType(getNewId(),"button press"); // change to evOneInput? too restrictive now, could also be a gesture from myo?
    private EventType evHeartRateReading = new EventType(getNewId(),"heart rate reading");
    private EventType evTimeAt = new EventType(getNewId(),"time at");
    private EventType evLocationArrivingAt = new EventType(getNewId(),"arriving at");
    private EventType evLocationLeaving = new EventType(getNewId(),"leaving");


    // StateTypes
    private StateType stAlarmGoing = new StateType(getNewId(),"alarm going");
    private StateType stCallIncGoing = new StateType(getNewId(),"call incoming going");
    private StateType stTimeFromTo = new StateType(getNewId(),"time from to");
    private StateType stLocationCurrentlyAt = new StateType(getNewId(),"currently at");


    // ActionTypes
    private ActionType acAlarmSnooze = new ActionType(getNewId(),"alarm snooze");
    private ActionType acAlarmDismiss = new ActionType(getNewId(),"alarm dismiss");
    private ActionType acAlarmVibrate = new ActionType(getNewId(),"alarm vibrate");
    private ActionType acAlarmDisplay = new ActionType(getNewId(),"alarm display");
    private ActionType acTimeDisplay = new ActionType(getNewId(),"time display");

    // Devices
    private AndroidPhone mAndroidPhone;
    private Pebble mPebble;
    private Clock mClock;
    private Geofences mGeofences;

    public DeviceManager(RuleSystemService ruleSystemService) {
        this.devices = new HashMap<>();
        this.eventTypes = new HashMap<>();
        this.stateTypes = new HashMap<>();
        this.actionTypes = new HashMap<>();
        this.eventTypes.put(evAlarmAlert.getId(),evAlarmAlert);
        this.eventTypes.put(evAlarmSnooze.getId(),evAlarmSnooze);
        this.eventTypes.put(evAlarmDismiss.getId(),evAlarmDismiss);
        this.eventTypes.put(evAlarmDone.getId(),evAlarmDone);
        this.eventTypes.put(evCallInc.getId(),evCallInc);
        this.eventTypes.put(evButtonPress.getId(),evButtonPress);
        this.eventTypes.put(evHeartRateReading.getId(),evHeartRateReading);
        this.eventTypes.put(evTimeAt.getId(),evTimeAt);
        this.eventTypes.put(evLocationArrivingAt.getId(),evLocationArrivingAt);
        this.eventTypes.put(evLocationLeaving.getId(),evLocationLeaving);
        this.stateTypes.put(stAlarmGoing.getId(),stAlarmGoing);
        this.stateTypes.put(stCallIncGoing.getId(),stCallIncGoing);
        this.stateTypes.put(stTimeFromTo.getId(),stTimeFromTo);
        this.stateTypes.put(stLocationCurrentlyAt.getId(),stLocationCurrentlyAt);
        this.actionTypes.put(acAlarmSnooze.getId(),acAlarmSnooze);
        this.actionTypes.put(acAlarmDismiss.getId(),acAlarmDismiss);
        this.actionTypes.put(acAlarmVibrate.getId(),acAlarmVibrate);
        this.actionTypes.put(acAlarmDisplay.getId(),acAlarmDisplay);
        this.actionTypes.put(acTimeDisplay.getId(),acTimeDisplay);
        this.mAndroidPhone = new AndroidPhone(ruleSystemService, this, evAlarmAlert, evAlarmSnooze, evAlarmDismiss, evAlarmDone, evCallInc, stAlarmGoing, stCallIncGoing, acAlarmSnooze, acAlarmDismiss);
        this.mPebble = new Pebble(ruleSystemService, this, evButtonPress, evHeartRateReading, acAlarmVibrate, acAlarmDisplay, acTimeDisplay);
        this.mClock = new Clock(ruleSystemService, this, evTimeAt, stTimeFromTo);
        this.mGeofences = new Geofences(ruleSystemService, this, evLocationArrivingAt, evLocationLeaving, stLocationCurrentlyAt);
        this.devices.put(mAndroidPhone.getId(),mAndroidPhone);
        this.devices.put(mPebble.getId(),mPebble);
        this.devices.put(mClock.getId(),mClock);
        this.devices.put(mGeofences.getId(),mGeofences);
    }

    public AndroidPhone getAndroidPhone() {
        return mAndroidPhone;
    }

    public Pebble getPebble() {
        return mPebble;
    }

    public Clock getClock() {
        return mClock;
    }

    public Geofences getGeofences() {
        return mGeofences;
    }

    public HashSet<Device> getDevices() {
        return new HashSet(devices.values());
    }

    public Device getDevice(int id){
        return devices.get(id);
    }

    public HashMap<Integer, EventType> getEventTypes() {
        return eventTypes;
    }

    public EventType getEventType(int id){
        return eventTypes.get(id);
    }

    public HashMap<Integer, StateType> getStateTypes() {
        return stateTypes;
    }

    public StateType getStateType(int id){
        return stateTypes.get(id);
    }

    public HashMap<Integer, ActionType> getActionTypes() {
        return actionTypes;
    }

    public ActionType getActionType(int id){
        return actionTypes.get(id);
    }

    public Set<Event> getAllEvents(){
        Set<Event> events = new HashSet<>();
        for (Device d: devices.values()){
            events.addAll(d.getEvents().values());
        }
        return events;
    }

    public Set<State> getAllStates(){
        Set<State> states = new HashSet<>();
        for (Device d: devices.values()){
            states.addAll(d.getStates().values());
        }
        return states;
    }

    public Set<Action> getAllActions(){
        Set<Action> actions = new HashSet<>();
        for (Device d: devices.values()){
            actions.addAll(d.getActions().values());
        }
        return actions;
    }

    public int getNewId(){
        return this.newId++;
    }

    // Start & Stop Devices:
    // Start & Stop Services from all devices!!!
    // Register & unRegister Receivers from all devices!!!
    // startDevices must be called in onResume method of Activity OR onCreate of Service
    // stopDevices must be called in onPause method of Activity OR onDestroy of Service

    public void startDevices(){
        mAndroidPhone.start();
        mPebble.start();
        mClock.start();
        mGeofences.start();
    }

    public void stopDevices(){
        mAndroidPhone.stop();
        mPebble.stop();
        mClock.stop();
        mGeofences.stop();
    }


}
