package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.ActionType;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 5/04/17.
 */

public class AndroidPhone extends Device {

    private Context mContext;

    // Constants

    public static final String ALARM_SONY_ALERT_ACTION = "com.sonyericsson.alarm.ALARM_ALERT";
    public static final String ALARM_SONY_SNOOZE_ACTION = "com.sonyericsson.alarm.ALARM_SNOOZE";
    public static final String ALARM_SONY_DISMISS_ACTION = "com.sonyericsson.alarm.ALARM_DISMISS";
    public static final String ALARM_SONY_DONE_ACTION = "com.sonyericsson.alarm.ALARM_DONE";

    public static final String ALARM_ALERT_ACTION = "com.android.deskclock.ALARM_ALERT";
    public static final String ALARM_SNOOZE_ACTION = "com.android.deskclock.ALARM_SNOOZE";
    public static final String ALARM_DISMISS_ACTION = "com.android.deskclock.ALARM_DISMISS";
    public static final String ALARM_DONE_ACTION = "com.android.deskclock.ALARM_DONE";

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED"; // WORKS on emulator & sony phone ( if permission in settings ) !!!

    // Android Phone Broadcast Receiver

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TRGRD","AndroidPhone mReceiver "+action);

            if(action.equals(ALARM_SONY_ALERT_ACTION)){
                evAlarmStart();
            } else if(action.equals(ALARM_SONY_SNOOZE_ACTION)){
                evAlarmSnooze();
            } else if(action.equals(ALARM_SONY_DISMISS_ACTION)){
                evAlarmDismiss();
            } else if(action.equals(ALARM_SONY_DONE_ACTION)){
                evAlarmDone();
            }
        }
    };

    // Events, states & actions

    private Event mEvAlarmStart, mEvAlarmSnooze, mEvAlarmDismiss, mEvAlarmDone, mEvCallIncStart, mEvCallIncStop;
    private State mStAlarmGoing, mStCallIncGoing;
    private Action mAcAlarmDismiss, mAcAlarmSnooze;

    public AndroidPhone(Context context, EventType evAlarmAlert, EventType evAlarmSnooze, EventType evAlarmDismiss, EventType evAlarmDone, EventType evCallInc, StateType stAlarmGoing, StateType stCallIncGoing, ActionType acAlarmSnooze, ActionType acAlarmDismiss, DeviceManager deviceManager) {
        super(1, "Phone", "Google", "Android", R.drawable.ic_phone_android_black_24dp, deviceManager);
        this.mContext = context;
        this.eventTypes.put(evAlarmAlert.getId(),evAlarmAlert);
        this.eventTypes.put(evAlarmSnooze.getId(),evAlarmSnooze);
        this.eventTypes.put(evAlarmDismiss.getId(),evAlarmDismiss);
        this.eventTypes.put(evAlarmDone.getId(),evAlarmDone);
        this.eventTypes.put(evCallInc.getId(),evCallInc);
        this.stateTypes.put(stAlarmGoing.getId(),stAlarmGoing);
        this.stateTypes.put(stCallIncGoing.getId(),stCallIncGoing);
        this.actionTypes.put(acAlarmSnooze.getId(),acAlarmSnooze);
        this.actionTypes.put(acAlarmDismiss.getId(),acAlarmDismiss);
        this.mEvAlarmStart = new Event(deviceManager.getNewId(),"Phone Alarm Start", R.drawable.ic_alarm_black_24dp, this, evAlarmAlert);
        this.mEvAlarmSnooze = new Event(deviceManager.getNewId(),"Phone Alarm Snooze", R.drawable.ic_alarm_off_black_24dp, this, evAlarmSnooze);
        this.mEvAlarmDismiss = new Event(deviceManager.getNewId(),"Phone Alarm Dismiss", R.drawable.ic_alarm_off_black_24dp, this, evAlarmDismiss);
        this.mEvAlarmDone = new Event(deviceManager.getNewId(),"Phone Alarm Done", R.drawable.ic_alarm_off_black_24dp, this, evAlarmDone);
        this.mEvCallIncStart = new Event(deviceManager.getNewId(),"Phone Call Incoming Start", R.drawable.ic_call_black_24dp, this, evCallInc);
        this.mEvCallIncStop = new Event(deviceManager.getNewId(),"Phone Call Incoming Stop", R.drawable.ic_call_end_black_24dp, this, evCallInc);
        this.mStAlarmGoing = new State(deviceManager.getNewId(),"Phone Alarm Going", R.drawable.ic_alarm_black_24dp, this, stAlarmGoing, false);
        this.mStCallIncGoing = new State(deviceManager.getNewId(),"Phone Call Incoming Going", R.drawable.ic_call_black_24dp, this, stCallIncGoing, false);
        this.mAcAlarmDismiss = new Action(deviceManager.getNewId(),"Phone Alarm Dismiss", R.drawable.ic_alarm_off_black_24dp, this, acAlarmDismiss, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acAlarmDismiss();
                return null;
            }
        });
        this.mAcAlarmSnooze = new Action(deviceManager.getNewId(),"Phone Alarm Snooze", R.drawable.ic_alarm_off_black_24dp, this, acAlarmSnooze, new Callable<String>(){
            @Override
            public String call() throws Exception {
                acAlarmSnooze();
                return null;
            }
        });
        this.events.put(mEvAlarmStart.getId(),mEvAlarmStart);
        this.events.put(mEvAlarmSnooze.getId(),mEvAlarmSnooze);
        this.events.put(mEvAlarmDismiss.getId(),mEvAlarmDismiss);
        this.events.put(mEvAlarmDone.getId(),mEvAlarmDone);
        this.events.put(mEvCallIncStart.getId(),mEvCallIncStart);
        this.events.put(mEvCallIncStop.getId(),mEvCallIncStop);
        this.states.put(mStAlarmGoing.getId(),mStAlarmGoing);
        this.states.put(mStCallIncGoing.getId(),mStCallIncGoing);
        this.actions.put(mAcAlarmDismiss.getId(),mAcAlarmDismiss);
        this.actions.put(mAcAlarmSnooze.getId(),mAcAlarmSnooze);
    }

    // These getters only needed for testing!

    public Event getAlarmStart() {
        return mEvAlarmStart;
    }

    public Event getAlarmSnooze() {
        return mEvAlarmSnooze;
    }

    public Event getAlarmDismiss() {
        return mEvAlarmDismiss;
    }

    public Event getAlarmDone() {
        return mEvAlarmDone;
    }

    public Event getCallIncStart() {
        return mEvCallIncStart;
    }

    public Event getCallIncStop() {
        return mEvCallIncStop;
    }

    public State getAlarmGoing() {
        return mStAlarmGoing;
    }

    public State getCallIncGoing() {
        return mStCallIncGoing;
    }

    public Action getAcAlarmDismiss() {
        return mAcAlarmDismiss;
    }

    public Action getAcAlarmSnooze() {
        return mAcAlarmSnooze;
    }

    // FOR SIMULATION TESTS:

    public Action getSimAcAlarmDismiss(){
        Action alarmDismiss = new Action(0,"Phone Alarm Dismiss", 0, this, null, new Callable<String>() {
            @Override
            public String call() throws Exception {
                evAlarmDismiss();
                return null;
            }
        });
        return alarmDismiss;
    }

    public Action getSimAcAlarmSnooze(){
        Action alarmSnooze = new Action(0,"Phone Alarm Snooze", 0, this, null, new Callable<String>() {
            @Override
            public String call() throws Exception {
                evAlarmSnooze();
                return null;
            }
        });
        return alarmSnooze;
    }

    // Real Events and Actions

    public void evAlarmStart(){
        System.out.println("[Phone Event] Alarm starts going off!");
        mEvAlarmStart.trigger();
        mStAlarmGoing.trigger();
    }

    public void evAlarmSnooze(){
        System.out.println("[Phone Event] Alarm snoozed!");
        mStAlarmGoing.setState(false);
        mEvAlarmSnooze.trigger();
    }

    public void evAlarmDismiss(){
        System.out.println("[Phone Event] Alarm dismissed!");
        mStAlarmGoing.setState(false);
        mEvAlarmDismiss.trigger();
    }

    public void evAlarmDone(){
        System.out.println("[Phone Event] Alarm done!");
        mStAlarmGoing.setState(false);
        mEvAlarmDone.trigger();
    }

    public void acAlarmSnooze(){
        Intent newIntent = new Intent(ALARM_SONY_SNOOZE_ACTION);
        mContext.getApplicationContext().sendBroadcast(newIntent);
        Toast.makeText(mContext, "Alarm snoozed by TRGRD", Toast.LENGTH_SHORT).show();
        //evAlarmSnooze(); THIS IS DONE in mReceiver already!
    }

    public void acAlarmDismiss(){
        Intent newIntent = new Intent(ALARM_SONY_DISMISS_ACTION);
        mContext.getApplicationContext().sendBroadcast(newIntent);
        Toast.makeText(mContext, "Alarm dismissed by TRGRD", Toast.LENGTH_SHORT).show();
        //evAlarmDismiss(); THIS IS DONE in mReceiver already!
    }

    // Register & UnRegister Android Phone Broadcast Receiver

    public void registerAndroidPhoneReceiver(){
        IntentFilter filter = new IntentFilter(ALARM_SONY_ALERT_ACTION);
        filter.addAction(ALARM_SONY_SNOOZE_ACTION);
        filter.addAction(ALARM_SONY_DISMISS_ACTION);
        filter.addAction(ALARM_SONY_DONE_ACTION);
        mContext.registerReceiver(mReceiver, filter);
        Log.d("TRGRD","AndroidPhone mReceiver registered!");
    }

    public void unRegisterAndroidPhoneReceiver(){
        mContext.unregisterReceiver(mReceiver);
        Log.d("TRGRD","AndroidPhone mReceiver unRegistered!");
    }

}
