package com.example.nicolascarraggi.trgrd.rulesys;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.devices.AndroidPhone;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Clock;
import com.example.nicolascarraggi.trgrd.rulesys.devices.HomeCoffeeMachine;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Geofences;
import com.example.nicolascarraggi.trgrd.rulesys.devices.HomeTv;
import com.example.nicolascarraggi.trgrd.rulesys.devices.MyoDevice;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Pebble;
import com.example.nicolascarraggi.trgrd.rulesys.devices.VubCoffeeMachine;

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
    public EventType evAlarmAlert = new EventType(getNewId(),"an alarm alert starts", R.drawable.ic_alarm_black_24dp);
    public EventType evAlarmSnooze = new EventType(getNewId(),"an alarm snoozed", R.drawable.ic_alarm_black_24dp);
    public EventType evAlarmDismiss = new EventType(getNewId(),"an alarm dismissed", R.drawable.ic_alarm_black_24dp);
    public EventType evAlarmDone = new EventType(getNewId(),"an alarm stopped", R.drawable.ic_alarm_black_24dp);
    public EventType evCallInc = new EventType(getNewId(),"an incoming call starts or stops", R.drawable.ic_call_black_24dp);
    public EventType evButtonPress = new EventType(getNewId(),"a button is pressed", R.drawable.ic_adjust_black_24dp); // change to evOneInput? too restrictive now, could also be a gesture from myo?
    public EventType evHeartRateReading = new EventType(getNewId(),"a heart rate reading comes in", R.drawable.ic_favorite_border_black_24dp);
    public EventType evTimeAt = new EventType(getNewId(),"time changes to a certain time", R.drawable.ic_access_time_black_24dp); // TODO other name
    public EventType evLocationArrivingAt = new EventType(getNewId(),"arrived at a location", R.drawable.ic_my_location_black_24dp);
    public EventType evLocationLeaving = new EventType(getNewId(),"left a location", R.drawable.ic_my_location_black_24dp);
    public EventType evGesture = new EventType(getNewId(),"gesture is made", R.drawable.ic_gesture_black_24dp);
    public EventType evWakesUp = new EventType(getNewId(),"user wakes up", R.drawable.ic_wb_sunny_black_24dp);
    public EventType evActivityChange = new EventType(getNewId(),"user activity changed", R.drawable.ic_directions_run_black_24dp);


    // StateTypes
    public StateType stAlarmGoing = new StateType(getNewId(),"an alarm is going", R.drawable.ic_alarm_black_24dp);
    public StateType stCallIncGoing = new StateType(getNewId(),"an incoming call is going", R.drawable.ic_call_black_24dp);
    public StateType stTimeFromTo = new StateType(getNewId(),"time is between two times", R.drawable.ic_access_time_black_24dp);
    public StateType stLocationCurrentlyAt = new StateType(getNewId(),"currently at a location", R.drawable.ic_my_location_black_24dp);
    public StateType stWatchMode = new StateType(getNewId(),"watch is in a certain mode", R.drawable.ic_watch_black_24dp);


    // ActionTypes
    public ActionType acAlarmSnooze = new ActionType(getNewId(),"snooze an alarm", R.drawable.ic_alarm_black_24dp);
    public ActionType acAlarmDismiss = new ActionType(getNewId(),"dismiss an alarm", R.drawable.ic_alarm_black_24dp);
    public ActionType acVibrate = new ActionType(getNewId(),"vibrate something", R.drawable.ic_vibration_black_24dp);
    public ActionType acAlarmDisplay = new ActionType(getNewId(),"display an alarm somewhere", R.drawable.ic_alarm_black_24dp);
    public ActionType acWatchMode = new ActionType(getNewId(),"set watch to a certain mode", R.drawable.ic_watch_black_24dp);
    public ActionType acTimeDisplay = new ActionType(getNewId(),"display time", R.drawable.ic_watch_black_24dp);
    public ActionType acSportDisplay = new ActionType(getNewId(),"display sport info", R.drawable.ic_watch_black_24dp);
    public ActionType acNotify = new ActionType(getNewId(),"notify something somewhere", R.drawable.ic_notifications_active_black_24dp);
    public ActionType acStartCoffee = new ActionType(getNewId(),"start making coffee", R.drawable.ic_local_cafe_black_24dp);
    public ActionType acScoreAdjust = new ActionType(getNewId(),"adjust a score", R.drawable.ic_exposure_plus_1_black_24dp);
    public ActionType acSendMessage = new ActionType(getNewId(),"send a message", R.drawable.ic_message_black_24dp);
    public ActionType acAudioMode = new ActionType(getNewId(),"set audio mode", R.drawable.ic_volume_up_black_24dp);
    public ActionType acCallIncReject = new ActionType(getNewId(),"reject an incoming call", R.drawable.ic_call_end_black_24dp);
    public ActionType acTurnOnOffTv = new ActionType(getNewId(),"turn a TV on or off", R.drawable.ic_tv_black_24dp);

    // Devices
    private AndroidPhone mAndroidPhone;
    private Pebble mPebble;
    private Clock mClock;
    private Geofences mGeofences;
    private HomeCoffeeMachine mHomeCoffeeMachine;
    private VubCoffeeMachine mVubCoffeeMachine;
    private MyoDevice mMyoDevice;
    private HomeTv mHomeTv;

    public DeviceManager(RuleSystemService ruleSystemService) {
        this.ruleSystemService = ruleSystemService;
        this.devices = new HashMap<>();
        this.eventTypes = new HashMap<>();
        this.stateTypes = new HashMap<>();
        this.actionTypes = new HashMap<>();
        this.eventTypeInstances = new HashMap<>();
        this.stateTypeInstances = new HashMap<>();
        this.actionTypeInstances = new HashMap<>();
        // EventTypes
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
        this.eventTypes.put(evGesture.getId(),evGesture);
        this.eventTypes.put(evWakesUp.getId(), evWakesUp);
        this.eventTypes.put(evActivityChange.getId(),evActivityChange);
        // StateTypes
        this.stateTypes.put(stAlarmGoing.getId(),stAlarmGoing);
        this.stateTypes.put(stCallIncGoing.getId(),stCallIncGoing);
        this.stateTypes.put(stTimeFromTo.getId(),stTimeFromTo);
        this.stateTypes.put(stLocationCurrentlyAt.getId(),stLocationCurrentlyAt);
        this.stateTypes.put(stWatchMode.getId(),stWatchMode);
        // ActionTypes
        this.actionTypes.put(acAlarmSnooze.getId(),acAlarmSnooze);
        this.actionTypes.put(acAlarmDismiss.getId(),acAlarmDismiss);
        this.actionTypes.put(acVibrate.getId(),acVibrate);
        this.actionTypes.put(acAlarmDisplay.getId(),acAlarmDisplay);
        this.actionTypes.put(acWatchMode.getId(),acWatchMode);
        this.actionTypes.put(acTimeDisplay.getId(),acTimeDisplay);
        this.actionTypes.put(acSportDisplay.getId(),acSportDisplay);
        this.actionTypes.put(acNotify.getId(),acNotify);
        this.actionTypes.put(acStartCoffee.getId(),acStartCoffee);
        this.actionTypes.put(acScoreAdjust.getId(),acScoreAdjust);
        this.actionTypes.put(acAudioMode.getId(),acAudioMode);
        this.actionTypes.put(acCallIncReject.getId(),acCallIncReject);
        this.actionTypes.put(acTurnOnOffTv.getId(),acTurnOnOffTv);
        // Devices
        this.mAndroidPhone = new AndroidPhone(ruleSystemService, this);
        this.mPebble = new Pebble(ruleSystemService, this);
        this.mClock = new Clock(ruleSystemService, this);
        this.mGeofences = new Geofences(ruleSystemService, this);
        this.mHomeCoffeeMachine = new HomeCoffeeMachine(ruleSystemService,this);
        this.mVubCoffeeMachine = new VubCoffeeMachine(ruleSystemService,this);
        this.mMyoDevice = new MyoDevice(ruleSystemService,this);
        this.mHomeTv = new HomeTv(ruleSystemService,this);
        this.devices.put(mAndroidPhone.getId(),mAndroidPhone);
        this.devices.put(mPebble.getId(),mPebble);
        this.devices.put(mClock.getId(),mClock);
        this.devices.put(mGeofences.getId(),mGeofences);
        this.devices.put(mHomeCoffeeMachine.getId(), mHomeCoffeeMachine);
        this.devices.put(mVubCoffeeMachine.getId(), mVubCoffeeMachine);
        this.devices.put(mMyoDevice.getId(), mMyoDevice);
        this.devices.put(mHomeTv.getId(), mHomeTv);
    }

    // ------------
    // TYPE GETTERS
    // ------------

    public EventType getEvAlarmAlert() {
        return evAlarmAlert;
    }

    public EventType getEvAlarmSnooze() {
        return evAlarmSnooze;
    }

    public EventType getEvAlarmDismiss() {
        return evAlarmDismiss;
    }

    public EventType getEvAlarmDone() {
        return evAlarmDone;
    }

    public EventType getEvCallInc() {
        return evCallInc;
    }

    public EventType getEvButtonPress() {
        return evButtonPress;
    }

    public EventType getEvHeartRateReading() {
        return evHeartRateReading;
    }

    public EventType getEvTimeAt() {
        return evTimeAt;
    }

    public EventType getEvLocationArrivingAt() {
        return evLocationArrivingAt;
    }

    public EventType getEvLocationLeaving() {
        return evLocationLeaving;
    }

    public EventType getEvGesture() {
        return evGesture;
    }

    public EventType getEvWakesUp() {
        return evWakesUp;
    }

    public EventType getEvActivityChange() {
        return evActivityChange;
    }

    public StateType getStAlarmGoing() {
        return stAlarmGoing;
    }

    public StateType getStCallIncGoing() {
        return stCallIncGoing;
    }

    public StateType getStTimeFromTo() {
        return stTimeFromTo;
    }

    public StateType getStLocationCurrentlyAt() {
        return stLocationCurrentlyAt;
    }

    public StateType getStWatchMode() {
        return stWatchMode;
    }

    public ActionType getAcAlarmSnooze() {
        return acAlarmSnooze;
    }

    public ActionType getAcAlarmDismiss() {
        return acAlarmDismiss;
    }

    public ActionType getAcVibrate() {
        return acVibrate;
    }

    public ActionType getAcAlarmDisplay() {
        return acAlarmDisplay;
    }

    public ActionType getAcWatchMode() {
        return acWatchMode;
    }

    public ActionType getAcTimeDisplay() {
        return acTimeDisplay;
    }

    public ActionType getAcSportDisplay() {
        return acSportDisplay;
    }

    public ActionType getAcNotify() {
        return acNotify;
    }

    public ActionType getAcStartCoffee() {
        return acStartCoffee;
    }

    public ActionType getAcScoreAdjust() {
        return acScoreAdjust;
    }


    // --------------
    // DEVICE GETTERS
    // --------------

    public AndroidPhone getAndroidPhone() {
        return mAndroidPhone;
    }

    public Pebble getPebble() {
        return mPebble;
    }

    public MyoDevice getMyoDevice() {
        return mMyoDevice;
    }

    public Clock getClock() {
        return mClock;
    }

    public Geofences getGeofences() {
        return mGeofences;
    }

    public HomeCoffeeMachine getHomeCoffeeMachine() {
        return mHomeCoffeeMachine;
    }

    public VubCoffeeMachine getVubCoffeeMachine() {
        return mVubCoffeeMachine;
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
        mHomeCoffeeMachine.start();
        mVubCoffeeMachine.start();
        //mMyoDevice.start(); Manually start in GUI !!
        mHomeTv.start();
    }

    public void stopDevices(){
        mAndroidPhone.stop();
        mPebble.stop();
        mClock.stop();
        mGeofences.stop();
        mHomeCoffeeMachine.stop();
        mVubCoffeeMachine.stop();
        mMyoDevice.stop();
        mHomeTv.stop();
    }


}
