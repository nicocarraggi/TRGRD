package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.MyTime;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;
import com.example.nicolascarraggi.trgrd.rulesys.TimeEvent;
import com.example.nicolascarraggi.trgrd.rulesys.TimeState;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by nicolascarraggi on 26/04/17.
 */

public class Clock extends Device {

    private Context mContext;

    // Clock Broadcast Receiver

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.d("TRGRD","Clock mReceiver "+action);
            if (action.equals(Intent.ACTION_TIME_TICK)){
                evTimeAt();
                stTimeFromTo();
            }
        }
    };

    // Events, states & actions

    private TimeEvent mEvTimeAt;
    private TimeState mStTimeFromTo;

    public Clock(Context context, EventType evClockAt, StateType stClockFromTo, DeviceManager deviceManager) {
        super(3, "Clock", "Google", "Android", R.drawable.ic_access_time_black_24dp, deviceManager);
        this.mContext = context;
        this.getEventTypes().put(evClockAt.getId(),evClockAt);
        this.getStateTypes().put(stClockFromTo.getId(),stClockFromTo);
        mEvTimeAt = new TimeEvent(deviceManager.getNewId(),"Time at ...", R.drawable.ic_keyboard_arrow_down_black_24dp, this, evClockAt);
        mStTimeFromTo = new TimeState(deviceManager.getNewId(),"Time from ... to ...", R.drawable.ic_code_black_24dp, this, stClockFromTo, false);
        this.events.put(mEvTimeAt.getId(),mEvTimeAt);
        this.states.put(mStTimeFromTo.getId(),mStTimeFromTo);
    }

    public TimeEvent getTimeAt() {
        return mEvTimeAt;
    }

    public TimeEvent getTimeAtInstance(TimeEvent timeEvent, MyTime time){
        TimeEvent instance = new TimeEvent(deviceManager.getNewId(),timeEvent,time);
        eventInstances.put(instance.getId(),instance);
        return instance;
    }

    public TimeState getTimeFromTo() {
        return mStTimeFromTo;
    }

    public TimeState getTimeFromToInstance(TimeState timeState, MyTime timeFrom, MyTime timeTo){
        TimeState instance = new TimeState(deviceManager.getNewId(),timeState,timeFrom,timeTo);
        stateInstances.put(instance.getId(),instance);
        return instance;
    }

    public void evTimeAt() {
        MyTime time = new MyTime();
        // check all instances of mEvTimeAt ???
        for (Event e : eventInstances.values()){
            TimeEvent timeEvent = (TimeEvent) e;
            if(timeEvent.getTime().equals(time)) {
                timeEvent.trigger();
            }
        }
    }

    public void stTimeFromTo() {
        // check all instances of mStTimeFromTo ???
        MyTime time = new MyTime();
        // check all instances of mEvTimeAt ???
        for (State s : stateInstances.values()){
            TimeState timeState = (TimeState) s;
            timeState.setState(time.isBetween(timeState.getTimeFrom(),timeState.getTimeTo()));
        }
    }

    // Register & UnRegister Android Phone Broadcast Receiver

    public void registerClockReceiver(){
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        mContext.registerReceiver(mReceiver, filter);
        Log.d("TRGRD","Clock mReceiver registered!");
    }

    public void unRegisterClockReceiver(){
        mContext.unregisterReceiver(mReceiver);
        Log.d("TRGRD","Clock mReceiver unRegistered!");
    }
}
