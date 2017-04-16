package com.example.nicolascarraggi.trgrd.rulesys;

import android.content.Context;

import com.example.nicolascarraggi.trgrd.rulesys.devices.AndroidPhone;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Pebble;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 11/04/17.
 */

public class DeviceManager {

    private Set<EventType> eventTypes;
    private Set<StateType> stateTypes;
    private Set<ActionType> actionTypes;

    // EventTypes
    private EventType evAlarmAlert = new EventType("alarm alert");
    private EventType evAlarmSnooze = new EventType("alarm snooze");
    private EventType evAlarmDismiss = new EventType("alarm dismiss");
    private EventType evAlarmDone = new EventType("alarm done");
    private EventType evButtonPress = new EventType("button press");
    private EventType evHeartRateReading = new EventType("heart rate reading");

    // StateTypes
    private StateType stAlarmGoing = new StateType("alarm going");

    // ActionTypes
    private ActionType acAlarmSnooze = new ActionType("alarm snooze");
    private ActionType acAlarmDismiss = new ActionType("alarm dismiss");
    private ActionType acAlarmVibrate = new ActionType(("alarm vibrate");
    private ActionType acAlarmDisplay = new ActionType(("alarm display");

    // Devices
    private AndroidPhone mAndroidPhone;
    private Pebble mPebble;

    public DeviceManager(Context mContext) {
        this.mAndroidPhone = new AndroidPhone(mContext);
        this.mPebble = new Pebble(mContext);
        this.eventTypes = new HashSet<>();
        this.stateTypes = new HashSet<>();
        this.actionTypes = new HashSet<>();
        eventTypes.add(evAlarmAlert);
        eventTypes.add(evAlarmSnooze);
        eventTypes.add(evAlarmDismiss);
        eventTypes.add(evAlarmDone);
        eventTypes.add(evButtonPress);
        eventTypes.add(evHeartRateReading;
        stateTypes.add(stAlarmGoing);
        actionTypes.add(acAlarmSnooze);
        actionTypes.add(acAlarmDismiss);
        actionTypes.add(acAlarmVibrate);
        actionTypes.add(acAlarmDisplay);
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
