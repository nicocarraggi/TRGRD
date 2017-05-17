package com.example.nicolascarraggi.trgrd.rulesys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.nicolascarraggi.trgrd.rulesys.devices.AndroidPhone;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Clock;
import com.example.nicolascarraggi.trgrd.rulesys.devices.CoffeeMachine;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Geofences;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Pebble;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 11/04/17.
 */

public class DeviceManager {

    public static final String DEVICES_REFRESH_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.REFRESH";

    // TODO add rule ID system
    private int newId = 1;
    // TODO get and write Events,States,Actions & their types from and to database?

    private RuleSystemService ruleSystemService;

    private HashMap<Integer,Device> devices;
    private HashMap<Integer,EventType> eventTypes;
    private HashMap<Integer,StateType> stateTypes;
    private HashMap<Integer,ActionType> actionTypes;
    private HashMap<Integer,EventType> eventTypeInstances;
    private HashMap<Integer,StateType> stateTypeInstances;
    private HashMap<Integer,ActionType> actionTypeInstances;

    // EventTypes
    public EventType evAlarmAlert = new EventType(getNewId(),"an alarm alert starts");
    public EventType evAlarmSnooze = new EventType(getNewId(),"an alarm snoozed");
    public EventType evAlarmDismiss = new EventType(getNewId(),"an alarm dismissed");
    public EventType evAlarmDone = new EventType(getNewId(),"an alarm stopped");
    public EventType evCallInc = new EventType(getNewId(),"a call comes in");
    public EventType evButtonPress = new EventType(getNewId(),"a button is pressed"); // change to evOneInput? too restrictive now, could also be a gesture from myo?
    public EventType evHeartRateReading = new EventType(getNewId(),"a heart rate reading comes in");
    public EventType evTimeAt = new EventType(getNewId(),"time changes to a certain time"); // TODO other name
    public EventType evLocationArrivingAt = new EventType(getNewId(),"arrived at a location");
    public EventType evLocationLeaving = new EventType(getNewId(),"left a location");


    // StateTypes
    public StateType stAlarmGoing = new StateType(getNewId(),"an alarm is going");
    public StateType stCallIncGoing = new StateType(getNewId(),"an incoming call is going");
    public StateType stTimeFromTo = new StateType(getNewId(),"time is between two times");
    public StateType stLocationCurrentlyAt = new StateType(getNewId(),"currently at a location");


    // ActionTypes
    public ActionType acAlarmSnooze = new ActionType(getNewId(),"snooze the alarm");
    public ActionType acAlarmDismiss = new ActionType(getNewId(),"dismiss the alarm");
    public ActionType acVibrate = new ActionType(getNewId(),"vibrate something");
    public ActionType acAlarmDisplay = new ActionType(getNewId(),"display the alarm somewhere");
    public ActionType acTimeDisplay = new ActionType(getNewId(),"display time");
    public ActionType acNotify = new ActionType(getNewId(),"notify something somewhere");
    public ActionType acStartCoffee = new ActionType(getNewId(),"start making coffee");

    // Devices
    private AndroidPhone mAndroidPhone;
    private Pebble mPebble;
    private Clock mClock;
    private Geofences mGeofences;
    private CoffeeMachine mCoffeeMachine;

    public DeviceManager(RuleSystemService ruleSystemService) {
        this.ruleSystemService = ruleSystemService;
        this.devices = new HashMap<>();
        this.eventTypes = new HashMap<>();
        this.stateTypes = new HashMap<>();
        this.actionTypes = new HashMap<>();
        this.eventTypeInstances = new HashMap<>();
        this.stateTypeInstances = new HashMap<>();
        this.actionTypeInstances = new HashMap<>();
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
        this.actionTypes.put(acVibrate.getId(),acVibrate);
        this.actionTypes.put(acAlarmDisplay.getId(),acAlarmDisplay);
        this.actionTypes.put(acTimeDisplay.getId(),acTimeDisplay);
        this.actionTypes.put(acNotify.getId(),acNotify);
        this.actionTypes.put(acStartCoffee.getId(),acStartCoffee);
        this.mAndroidPhone = new AndroidPhone(ruleSystemService, this, evAlarmAlert, evAlarmSnooze, evAlarmDismiss, evAlarmDone, evCallInc, stAlarmGoing, stCallIncGoing, acAlarmSnooze, acAlarmDismiss,acNotify);
        this.mPebble = new Pebble(ruleSystemService, this, evButtonPress, evHeartRateReading, acVibrate, acAlarmDisplay, acTimeDisplay, acNotify);
        this.mClock = new Clock(ruleSystemService, this, evTimeAt, stTimeFromTo);
        this.mGeofences = new Geofences(ruleSystemService, this, evLocationArrivingAt, evLocationLeaving, stLocationCurrentlyAt);
        this.mCoffeeMachine = new CoffeeMachine(ruleSystemService,this,acStartCoffee);
        this.devices.put(mAndroidPhone.getId(),mAndroidPhone);
        this.devices.put(mPebble.getId(),mPebble);
        this.devices.put(mClock.getId(),mClock);
        this.devices.put(mGeofences.getId(),mGeofences);
        this.devices.put(mCoffeeMachine.getId(),mCoffeeMachine);
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

    public CoffeeMachine getCoffeeMachine() {
        return mCoffeeMachine;
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

    public HashMap<Integer, EventType> getEventTypeInstances() {
        return eventTypeInstances;
    }

    public EventType getEventTypeInstance(int id){
        return eventTypeInstances.get(id);
    }

    public void addEventTypeInstance(EventType instance){
        eventTypeInstances.put(instance.getId(),instance);
    }

    public HashMap<Integer, StateType> getStateTypeInstances() {
        return stateTypeInstances;
    }

    public StateType getStateTypeInstance(int id){
        return stateTypeInstances.get(id);
    }

    public void addStateTypeInstance(StateType instance){
        stateTypeInstances.put(instance.getId(),instance);
    }

    public HashMap<Integer, ActionType> getActionTypeInstances() {
        return actionTypeInstances;
    }

    public ActionType getActionTypeInstance(int id){
        return actionTypeInstances.get(id);
    }

    public void addActionTypeInstance(ActionType instance){
        actionTypeInstances.put(instance.getId(),instance);
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

    public void sendRefreshBroadcast(){
        Intent newIntent = new Intent(DEVICES_REFRESH_ACTION);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
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
        mCoffeeMachine.start();
    }

    public void stopDevices(){
        mAndroidPhone.stop();
        mPebble.stop();
        mClock.stop();
        mGeofences.stop();
        mCoffeeMachine.stop();
    }


}
