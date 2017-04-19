package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.ActionType;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 5/04/17.
 */

public class Pebble extends Wearable {

    private Context mContext;

    // Constants

    public static final String PEBBLE_VIBRATE_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.VIBRATE";
    public static final String PEBBLE_SCREEN_TIME_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_TIME";
    public static final String PEBBLE_SCREEN_ALARM_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_ALARM";
    public static final String PEBBLE_SCREEN_CLEAN_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_CLEAN";

    // Pebble Broadcast Receiver

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TRGRD","Pebble mReceiver "+action);

            if(action.equals(PebbleCommunicationService.PEBBLE_BUTTON_UP_ACTION)){
                evBtnUp();
            } else if(action.equals(PebbleCommunicationService.PEBBLE_BUTTON_SELECT_ACTION)){
                evBtnSelect();
            } else if(action.equals(PebbleCommunicationService.PEBBLE_BUTTON_DOWN_ACTION)){
                evBtnDown();
            }
        }
    };

    private Event mEvBtnUp, mEvBtnSelect, mEvBtnDown;
    private Action mAcVibrate, mAcScreenTime, mAcScreenAlarm, mAcScreenClean;

    public Pebble(Context context, EventType evButtonPress, EventType evHeartRateReading, ActionType acAlarmVibrate, ActionType acAlarmDisplay, ActionType acTimeDisplay) {
        super("Pebble Steel", "Pebble", "Pebble OS", "Watch", "Wrist", R.drawable.ic_watch_black_24dp);
        this.mContext = context;
        this.eventTypes.add(evButtonPress);
        this.eventTypes.add(evHeartRateReading);
        this.actionTypes.add(acAlarmVibrate);
        this.actionTypes.add(acAlarmDisplay);
        this.actionTypes.add(acTimeDisplay);
        mEvBtnUp = new Event("Pebble Button Up", this, evButtonPress);
        mEvBtnSelect = new Event("Pebble Button Select", this, evButtonPress);
        mEvBtnDown = new Event("Pebble Button Down", this, evButtonPress);
        mAcVibrate = new Action("Pebble Vibrate", this, acAlarmVibrate, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acVibrate();
                return null;
            }
        });
        mAcScreenTime = new Action("Pebble Watch Mode Time", this, acTimeDisplay, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenTime();
                return null;
            }
        });
        mAcScreenAlarm = new Action("Pebble Watch Mode Alarm", this, acAlarmDisplay, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenAlarm();
                return null;
            }
        });
        mAcScreenClean = new Action("Pebble Watch Mode Clean", this, acTimeDisplay, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenClean();
                return null;
            }
        });
        this.events.add(mEvBtnUp);
        this.events.add(mEvBtnSelect);
        this.events.add(mEvBtnDown);
        this.actions.add(mAcVibrate);
        this.actions.add(mAcScreenTime);
        this.actions.add(mAcScreenAlarm);
        this.actions.add(mAcScreenClean);
    }

    // These getters only needed for testing!

    public Event getBtnUp() {
        return mEvBtnUp;
    }

    public Event getBtnSelect() {
        return mEvBtnSelect;
    }

    public Event getBtnDown() {
        return mEvBtnDown;
    }

    public Action getVibrate() {
        return mAcVibrate;
    }

    public Action getScreenTime() {
        return mAcScreenTime;
    }

    public Action getScreenAlarm() {
        return mAcScreenAlarm;
    }

    public Action getScreenClean() {
        return mAcScreenClean;
    }

    public Action getSimVibrate() {
        Action simVibrate = new Action("Pebble Vibrate", this, null, new Callable<String>() {
            @Override
            public String call() throws Exception {
                simulateActionVibrate();
                return null;
            }
        });
        return simVibrate;
    }

    public Action getSimScreenTime() {
        Action simScreenTime = new Action("Pebble Watch Mode Time", this, null, new Callable<String>() {
            @Override
            public String call() throws Exception {
                simulateActionScreenTime();
                return null;
            }
        });
        return simScreenTime;
    }

    // Events

    public void evBtnUp(){
        //Log.d("TRGRD", "Pebble Event Button Up");
        //System.out.println("[Pebble] Button Up!");
        mEvBtnUp.trigger();
    }

    public void evBtnSelect(){
        //Log.d("TRGRD", "Pebble Event Button Select");
        //System.out.println("[Pebble] Button Select!");
        mEvBtnSelect.trigger();
    }

    public void evBtnDown(){
        //Log.d("TRGRD", "Pebble Event Button Down");
        //System.out.println("[Pebble] Button Down!");
        mEvBtnDown.trigger();
    }

    public void acVibrate(){
        System.out.println("[Pebble] Vibrates!");
        Intent newIntent = new Intent(PEBBLE_VIBRATE_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
        Toast.makeText(mContext, "Pebble Vibrate triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    public void acScreenTime(){
        System.out.println("[Pebble] shows time!");
        Intent newIntent = new Intent(PEBBLE_SCREEN_TIME_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
        Toast.makeText(mContext, "Pebble Screen Time triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    public void acScreenAlarm(){
        // TEST: first vibrate pebble!
        Intent vibrateIntent = new Intent(PEBBLE_VIBRATE_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(vibrateIntent);
        System.out.println("[Pebble] shows alarm!");
        Intent newIntent = new Intent(PEBBLE_SCREEN_ALARM_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
        Toast.makeText(mContext, "Pebble Screen Alarm triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    public void acScreenClean(){
        System.out.println("[Pebble] cleans screen!");
        Intent newIntent = new Intent(PEBBLE_SCREEN_CLEAN_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(newIntent);
        Toast.makeText(mContext, "Pebble Screen Clean triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    // Simulate for testing!

    public void simulateActionVibrate(){
        System.out.println("[Pebble] Vibrates!");
    }

    public void simulateActionScreenTime(){
        System.out.println("[Pebble] shows time!");
    }

    // Register & UnRegister Pebble Broadcast Receiver

    public void registerPebbleReceiver(){
        IntentFilter filter = new IntentFilter(PebbleCommunicationService.PEBBLE_BUTTON_UP_ACTION);
        filter.addAction(PebbleCommunicationService.PEBBLE_BUTTON_SELECT_ACTION);
        filter.addAction(PebbleCommunicationService.PEBBLE_BUTTON_DOWN_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);
        Log.d("TRGRD","Pebble mReceiver registered!");
    }

    public void unRegisterPebbleReceiver(){
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        Log.d("TRGRD","Pebble mReceiver unRegistered!");
    }

    // Start & Stop Pebble Communication Service

    public void startCommunicationService(){
        Intent pebbleService = new Intent(mContext, PebbleCommunicationService.class);
        mContext.startService(pebbleService);
    }

    public void stopCommunicationService(){
        Intent pebbleService = new Intent(mContext, PebbleCommunicationService.class);
        mContext.stopService(pebbleService);
    }

}
