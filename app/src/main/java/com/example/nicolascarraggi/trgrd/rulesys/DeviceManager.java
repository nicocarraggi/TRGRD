package com.example.nicolascarraggi.trgrd.rulesys;

import android.content.Context;

import com.example.nicolascarraggi.trgrd.rulesys.devices.AndroidPhone;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Clock;
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

    // StateTypes
    private StateType stAlarmGoing = new StateType(getNewId(),"alarm going");
    private StateType stCallIncGoing = new StateType(getNewId(),"call incoming going");
    private StateType stTimeFromTo = new StateType(getNewId(),"time from to");

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

    public DeviceManager(Context mContext) {
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
        this.stateTypes.put(stAlarmGoing.getId(),stAlarmGoing);
        this.stateTypes.put(stCallIncGoing.getId(),stCallIncGoing);
        this.stateTypes.put(stTimeFromTo.getId(),stTimeFromTo);
        this.actionTypes.put(acAlarmSnooze.getId(),acAlarmSnooze);
        this.actionTypes.put(acAlarmDismiss.getId(),acAlarmDismiss);
        this.actionTypes.put(acAlarmVibrate.getId(),acAlarmVibrate);
        this.actionTypes.put(acAlarmDisplay.getId(),acAlarmDisplay);
        this.actionTypes.put(acTimeDisplay.getId(),acTimeDisplay);
        this.mAndroidPhone = new AndroidPhone(mContext, evAlarmAlert, evAlarmSnooze, evAlarmDismiss, evAlarmDone, evCallInc, stAlarmGoing, stCallIncGoing, acAlarmSnooze, acAlarmDismiss, this);
        this.mPebble = new Pebble(mContext, evButtonPress, evHeartRateReading, acAlarmVibrate, acAlarmDisplay, acTimeDisplay, this);
        this.mClock = new Clock(mContext, evTimeAt, stTimeFromTo, this);
        this.devices.put(mAndroidPhone.getId(),mAndroidPhone);
        this.devices.put(mPebble.getId(),mPebble);
        this.devices.put(mClock.getId(),mClock);
    }

    public AndroidPhone getAndroidPhone() {
        return mAndroidPhone;
    }

    public Pebble getPebble() {
        return mPebble;
    }

    public Clock getmClock() {
        return mClock;
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
        // Services
        mPebble.startCommunicationService();
        // Receivers
        mAndroidPhone.registerAndroidPhoneReceiver();
        mPebble.registerPebbleReceiver();
        mClock.registerClockReceiver();
    }

    public void stopDevices(){
        // Services
        mPebble.stopCommunicationService();
        // Receivers
        mAndroidPhone.unRegisterAndroidPhoneReceiver();
        mPebble.unRegisterPebbleReceiver();
        mClock.unRegisterClockReceiver();
    }


}
