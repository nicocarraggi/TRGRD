package com.example.nicolascarraggi.trgrd.rulesys;

import android.content.Context;
import android.util.Log;

import com.example.nicolascarraggi.trgrd.rulesys.devices.AndroidPhone;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Pebble;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 11/04/17.
 */

public class DeviceManager {

    private Set<Device> devices;
    private Set<EventType> eventTypes;
    private Set<StateType> stateTypes;
    private Set<ActionType> actionTypes;

    // EventTypes
    private EventType evAlarmAlert = new EventType("alarm alert");
    private EventType evAlarmSnooze = new EventType("alarm snooze");
    private EventType evAlarmDismiss = new EventType("alarm dismiss");
    private EventType evAlarmDone = new EventType("alarm done");
    private EventType evCallInc = new EventType("call incoming");
    private EventType evButtonPress = new EventType("button press"); // change to evOneInput? too restrictive now, could also be a gesture from myo?
    private EventType evHeartRateReading = new EventType("heart rate reading");

    // StateTypes
    private StateType stAlarmGoing = new StateType("alarm going");
    private StateType stCallIncGoing = new StateType("call incoming going");

    // ActionTypes
    private ActionType acAlarmSnooze = new ActionType("alarm snooze");
    private ActionType acAlarmDismiss = new ActionType("alarm dismiss");
    private ActionType acAlarmVibrate = new ActionType("alarm vibrate");
    private ActionType acAlarmDisplay = new ActionType("alarm display");
    private ActionType acTimeDisplay = new ActionType("time display");

    // Devices
    private AndroidPhone mAndroidPhone;
    private Pebble mPebble;

    public DeviceManager(Context mContext) {
        this.devices = new HashSet<>();
        this.eventTypes = new HashSet<>();
        this.stateTypes = new HashSet<>();
        this.actionTypes = new HashSet<>();
        this.eventTypes.add(evAlarmAlert);
        this.eventTypes.add(evAlarmSnooze);
        this.eventTypes.add(evAlarmDismiss);
        this.eventTypes.add(evAlarmDone);
        this.eventTypes.add(evCallInc);
        this.eventTypes.add(evButtonPress);
        this.eventTypes.add(evHeartRateReading);
        this.stateTypes.add(stAlarmGoing);
        this.stateTypes.add(stCallIncGoing);
        this.actionTypes.add(acAlarmSnooze);
        this.actionTypes.add(acAlarmDismiss);
        this.actionTypes.add(acAlarmVibrate);
        this.actionTypes.add(acAlarmDisplay);
        this.actionTypes.add(acTimeDisplay);
        this.mAndroidPhone = new AndroidPhone(mContext, evAlarmAlert, evAlarmSnooze, evAlarmDismiss, evAlarmDone, evCallInc, stAlarmGoing, stCallIncGoing, acAlarmSnooze, acAlarmDismiss);
        this.mPebble = new Pebble(mContext, evButtonPress, evHeartRateReading, acAlarmVibrate, acAlarmDisplay, acTimeDisplay);
        this.devices.add(mAndroidPhone);
        this.devices.add(mPebble);
    }

    public AndroidPhone getAndroidPhone() {
        return mAndroidPhone;
    }

    public Pebble getPebble() {
        return mPebble;
    }

    public Set<EventType> getEventTypes() {
        return eventTypes;
    }

    public Set<StateType> getStateTypes() {
        return stateTypes;
    }

    public Set<ActionType> getActionTypes() {
        return actionTypes;
    }

    public Set<Device> getDevices(){
        return devices;
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
