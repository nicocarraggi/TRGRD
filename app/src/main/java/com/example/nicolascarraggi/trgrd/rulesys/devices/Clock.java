package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;
import com.example.nicolascarraggi.trgrd.rulesys.TimeEvent;
import com.example.nicolascarraggi.trgrd.rulesys.TimeState;

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
            Log.d("TRGRD","Clock mReceiver "+action);

            if (action.equals(Intent.ACTION_TIME_TICK)){
                evTimeAt();
                stTimeFromTo();
            }
        }
    };

    // Events, states & actions

    private TimeEvent mEvTimeAt;
    private TimeState mStTimeFromTo;

    public Clock(Context context, EventType evClockAt, StateType stClockFromTo) {
        super(3, "Clock", "Google", "Android", R.drawable.ic_access_time_black_24dp);
        this.mContext = context;
        this.getEventTypes().put(evClockAt.getId(),evClockAt);
        this.getStateTypes().put(stClockFromTo.getId(),stClockFromTo);
        mEvTimeAt = new TimeEvent(18,"Clock At", R.drawable.ic_access_time_black_24dp, this, evClockAt);
        mStTimeFromTo = new TimeState(19,"Clock From To", R.drawable.ic_code_black_24dp, this, stClockFromTo, false);
        this.events.put(mEvTimeAt.getId(),mEvTimeAt);
        this.states.put(mStTimeFromTo.getId(),mStTimeFromTo);
    }

    public TimeEvent getTimeAt() {
        return mEvTimeAt;
    }

    public TimeState getTimeFromTo() {
        return mStTimeFromTo;
    }

    public void evTimeAt() {
        // check all instances of mEvTimeAt ???
    }

    public void stTimeFromTo() {
        // check all instances of mStTimeFromTo ???
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
