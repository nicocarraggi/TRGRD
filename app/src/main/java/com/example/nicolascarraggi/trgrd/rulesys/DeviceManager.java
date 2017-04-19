package com.example.nicolascarraggi.trgrd.rulesys;

import android.content.Context;

import com.example.nicolascarraggi.trgrd.rulesys.devices.AndroidPhone;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Pebble;

import java.util.HashMap;

/**
 * Created by nicolascarraggi on 11/04/17.
 */

public class DeviceManager {

    private HashMap<Integer,Device> devices;
    private HashMap<Integer,EventType> eventTypes;
    private HashMap<Integer,StateType> stateTypes;
    private HashMap<Integer,ActionType> actionTypes;

    // EventTypes
    private EventType evAlarmAlert = new EventType(1,"alarm alert");
    private EventType evAlarmSnooze = new EventType(2,"alarm snooze");
    private EventType evAlarmDismiss = new EventType(3,"alarm dismiss");
    private EventType evAlarmDone = new EventType(4,"alarm done");
    private EventType evCallInc = new EventType(5,"call incoming");
    private EventType evButtonPress = new EventType(6,"button press"); // change to evOneInput? too restrictive now, could also be a gesture from myo?
    private EventType evHeartRateReading = new EventType(7,"heart rate reading");

    // StateTypes
    private StateType stAlarmGoing = new StateType(8,"alarm going");
    private StateType stCallIncGoing = new StateType(9,"call incoming going");

    // ActionTypes
    private ActionType acAlarmSnooze = new ActionType(10,"alarm snooze");
    private ActionType acAlarmDismiss = new ActionType(11,"alarm dismiss");
    private ActionType acAlarmVibrate = new ActionType(12,"alarm vibrate");
    private ActionType acAlarmDisplay = new ActionType(13,"alarm display");
    private ActionType acTimeDisplay = new ActionType(14,"time display");

    // Devices
    private AndroidPhone mAndroidPhone;
    private Pebble mPebble;

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
        this.stateTypes.put(stAlarmGoing.getId(),stAlarmGoing);
        this.stateTypes.put(stCallIncGoing.getId(),stCallIncGoing);
        this.actionTypes.put(acAlarmSnooze.getId(),acAlarmSnooze);
        this.actionTypes.put(acAlarmDismiss.getId(),acAlarmDismiss);
        this.actionTypes.put(acAlarmVibrate.getId(),acAlarmVibrate);
        this.actionTypes.put(acAlarmDisplay.getId(),acAlarmDisplay);
        this.actionTypes.put(acTimeDisplay.getId(),acTimeDisplay);
        this.mAndroidPhone = new AndroidPhone(mContext, evAlarmAlert, evAlarmSnooze, evAlarmDismiss, evAlarmDone, evCallInc, stAlarmGoing, stCallIncGoing, acAlarmSnooze, acAlarmDismiss);
        this.mPebble = new Pebble(mContext, evButtonPress, evHeartRateReading, acAlarmVibrate, acAlarmDisplay, acTimeDisplay);
        this.devices.put(mAndroidPhone.getId(),mAndroidPhone);
        this.devices.put(mPebble.getId(),mPebble);
    }

    public AndroidPhone getAndroidPhone() {
        return mAndroidPhone;
    }

    public Pebble getPebble() {
        return mPebble;
    }

    public HashMap<Integer, Device> getDevices() {
        return devices;
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
    }

    public void stopDevices(){
        // Services
        mPebble.stopCommunicationService();
        // Receivers
        mAndroidPhone.unRegisterAndroidPhoneReceiver();
        mPebble.unRegisterPebbleReceiver();
    }


}
