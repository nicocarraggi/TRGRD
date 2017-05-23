package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

/**
 * Created by nicolascarraggi on 23/05/17.
 */

public class Myo extends Wearable {

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TRGRD","Myo mReceiver "+action);

            if(action.equals(MyoCommunicationService.MYO_GESTURE_EVENT)){
                evGesture();
            }
            /*else if(action.equals(PebbleCommunicationService.PEBBLE_BUTTON_SELECT_EVENT)){
                evBtnSelect();
            } else if(action.equals(PebbleCommunicationService.PEBBLE_BUTTON_DOWN_EVENT)){
                evBtnDown();
            }*/
        }
    };

    private Event mEvGesture;

    public Myo(RuleSystemService ruleSystemService, DeviceManager deviceManager, EventType evGesture){
        super(ruleSystemService.getNewId(), "Myo", "Thalmic Labs", "Myo OS", "Armband", "Arm", R.drawable.ic_pan_tool_black_24dp,ruleSystemService,deviceManager);
        this.eventTypes.put(evGesture.getId(),evGesture);
        mEvGesture = new Event(deviceManager.getNewId(),"Myo gesture",R.drawable.ic_gesture_black_24dp, this,evGesture);
        this.events.put(mEvGesture.getId(),mEvGesture);
    }

    // Events

    public void evGesture(){
        mEvGesture.trigger();
    }

    // Register & UnRegister Myo Broadcast Receiver

    public void registerMyoReceiver(){
        IntentFilter filter = new IntentFilter(MyoCommunicationService.MYO_GESTURE_EVENT);
        //filter.addAction(MyoCommunicationService.PEBBLE_BUTTON_SELECT_EVENT);
        //filter.addAction(MyoCommunicationService.PEBBLE_BUTTON_DOWN_EVENT);
        LocalBroadcastManager.getInstance(ruleSystemService).registerReceiver(mReceiver, filter);
        Log.d("TRGRD","Myo mReceiver registered!");
    }

    public void unRegisterMyoReceiver(){
        LocalBroadcastManager.getInstance(ruleSystemService).unregisterReceiver(mReceiver);
        Log.d("TRGRD","Myo mReceiver unRegistered!");
    }

    // Start & Stop Myo Communication Service

    public void startCommunicationService(){
        Intent myoService = new Intent(ruleSystemService, MyoCommunicationService.class);
        ruleSystemService.startService(myoService);
    }

    public void stopCommunicationService(){
        Intent myoService = new Intent(ruleSystemService, MyoCommunicationService.class);
        ruleSystemService.stopService(myoService);
    }

    public void start(){
        this.startCommunicationService();
        this.registerMyoReceiver();
        this.started = true;
        //deviceManager.sendRefreshBroadcast();
    }

    public void stop(){
        this.unRegisterMyoReceiver();
        this.stopCommunicationService();
        this.started = false;
        //deviceManager.sendRefreshBroadcast();
    }
}
