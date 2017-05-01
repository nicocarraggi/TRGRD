package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.content.Context;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;

/**
 * Created by nicolascarraggi on 1/05/17.
 */

public class Geofences extends Device {

    private Context mContext;

    private Event mEvLocationArrivingAt, mEvLocationLeaving;
    private State mStLocationCurrentlyAt;

    public Geofences(Context context, EventType evLocationArrivingAt, EventType evLocationLeaving, StateType stLocationCurrentlyAt, DeviceManager deviceManager) {
        super(4, "Geofences", "Google", "Android", R.drawable.ic_my_location_black_24dp, deviceManager);
        this.mContext = context;
        this.eventTypes.put(evLocationArrivingAt.getId(),evLocationArrivingAt);
        this.eventTypes.put(evLocationLeaving.getId(),evLocationLeaving);
        this.stateTypes.put(stLocationCurrentlyAt.getId(),stLocationCurrentlyAt);
        mEvLocationArrivingAt = new Event(deviceManager.getNewId(),"Location arriving at",R.drawable.ic_my_location_black_24dp,this,evLocationArrivingAt);
        mEvLocationLeaving = new Event(deviceManager.getNewId(),"Location leaving",R.drawable.ic_my_location_black_24dp,this,evLocationLeaving);
        mStLocationCurrentlyAt = new State(deviceManager.getNewId(),"Location currently at",R.drawable.ic_my_location_black_24dp,this,stLocationCurrentlyAt,false);
        this.events.put(mEvLocationArrivingAt.getId(),mEvLocationArrivingAt);
        this.events.put(mEvLocationLeaving.getId(),mEvLocationLeaving);
        this.states.put(mStLocationCurrentlyAt.getId(),mStLocationCurrentlyAt);
    }


}
