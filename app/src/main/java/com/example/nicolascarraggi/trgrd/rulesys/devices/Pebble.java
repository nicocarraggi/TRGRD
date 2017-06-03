package com.example.nicolascarraggi.trgrd.rulesys.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.ActionType;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.InputAction;
import com.example.nicolascarraggi.trgrd.rulesys.InputActionEvent;
import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 5/04/17.
 */

public class Pebble extends Wearable implements NotificationDevice, InputActionDevice {

    // Constants

    public static final String PEBBLE_VIBRATE_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.VIBRATE";
    public static final String PEBBLE_NOTIFICATION_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.NOTIFICATION";
    public static final String PEBBLE_SCREEN_TIME_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_TIME";
    public static final String PEBBLE_SCREEN_ALARM_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_ALARM";
    public static final String PEBBLE_SCREEN_CLEAN_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_CLEAN";

    // Pebble Broadcast Receiver

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TRGRD","Pebble mReceiver "+action);

            if(action.equals(PebbleCommunicationService.PEBBLE_BUTTON_UP_EVENT)){
                evBtnUp();
            } else if(action.equals(PebbleCommunicationService.PEBBLE_BUTTON_SELECT_EVENT)){
                evBtnSelect();
            } else if(action.equals(PebbleCommunicationService.PEBBLE_BUTTON_DOWN_EVENT)){
                evBtnDown();
            }
        }
    };

    private InputAction mIaBtnUp, mIaBtnSelect, mIaBtnDown;
    private InputActionEvent mIaEvBtn, mIaEvBtnUp, mIaEvBtnSelect, mIaEvBtnDown;
    private ArrayList<InputAction> inputActions;
    private HashMap<Integer, InputActionEvent> inputActionEvents;


    private Event mEvBtnUp, mEvBtnSelect, mEvBtnDown;
    private Action mAcVibrate, mAcScreenTime, mAcScreenAlarm, mAcScreenClean;
    private NotificationAction mAcNotify;

    public Pebble(RuleSystemService ruleSystemService, DeviceManager deviceManager, EventType evButtonPress, EventType evHeartRateReading,
                  ActionType acAlarmVibrate, ActionType acAlarmDisplay, ActionType acTimeDisplay, ActionType acNotify) {
        super(ruleSystemService.getNewId(), "Pebble Steel", "Pebble", "Pebble OS", "Watch", "Wrist", R.drawable.ic_watch_black_24dp, ruleSystemService, deviceManager);
        this.inputActions = new ArrayList<>();
        this.inputActionEvents = new HashMap<>();
        this.eventTypes.put(evButtonPress.getId(),evButtonPress);
        this.eventTypes.put(evHeartRateReading.getId(),evHeartRateReading);
        this.actionTypes.put(acAlarmVibrate.getId(),acAlarmVibrate);
        this.actionTypes.put(acAlarmDisplay.getId(),acAlarmDisplay);
        this.actionTypes.put(acTimeDisplay.getId(),acTimeDisplay);
        // INPUT ACTIONS
        this.mIaBtnUp = new InputAction(deviceManager.getNewId(),"UP",R.drawable.ic_keyboard_arrow_up_black_24dp);
        this.mIaBtnSelect = new InputAction(deviceManager.getNewId(),"SELECT",R.drawable.ic_keyboard_arrow_right_black_24dp);
        this.mIaBtnDown = new InputAction(deviceManager.getNewId(),"DOWN",R.drawable.ic_keyboard_arrow_down_black_24dp);
        this.inputActions.add(mIaBtnUp);
        this.inputActions.add(mIaBtnSelect);
        this.inputActions.add(mIaBtnDown);
        // INPUT ACTION EVENTS
        this.mIaEvBtn = new InputActionEvent(deviceManager.getNewId(),"Pebble ... button is pressed", R.drawable.ic_adjust_black_24dp, this, evButtonPress);
        this.mIaEvBtnUp = new InputActionEvent(deviceManager.getNewId(),mIaEvBtn,mIaBtnUp);
        this.mIaEvBtnSelect = new InputActionEvent(deviceManager.getNewId(),mIaEvBtn,mIaBtnSelect);
        this.mIaEvBtnDown = new InputActionEvent(deviceManager.getNewId(),mIaEvBtn,mIaBtnDown);
        this.inputActionEvents.put(mIaBtnUp.getId(), mIaEvBtnUp);
        this.inputActionEvents.put(mIaBtnSelect.getId(), mIaEvBtnSelect);
        this.inputActionEvents.put(mIaBtnDown.getId(), mIaEvBtnDown);
        // EVENTS
        this.mEvBtnUp = new Event(deviceManager.getNewId(),"Pebble UP button is pressed", R.drawable.ic_keyboard_arrow_up_black_24dp, this, evButtonPress);
        this.mEvBtnSelect = new Event(deviceManager.getNewId(),"Pebble SELECT button is pressed", R.drawable.ic_keyboard_arrow_right_black_24dp, this, evButtonPress);
        this.mEvBtnDown = new Event(deviceManager.getNewId(),"Pebble DOWN button is pressed", R.drawable.ic_keyboard_arrow_down_black_24dp, this, evButtonPress);
        // ACTIONS
        this.mAcVibrate = new Action(deviceManager.getNewId(),"Pebble vibrate", R.drawable.ic_vibration_black_24dp, this, acAlarmVibrate, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acVibrate();
                return null;
            }
        });
        this.mAcScreenTime = new Action(deviceManager.getNewId(),"Pebble watch mode time", R.drawable.ic_access_time_black_24dp, this, acTimeDisplay, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenTime();
                return null;
            }
        });
        this.mAcScreenAlarm = new Action(deviceManager.getNewId(),"Pebble watch mode alarm", R.drawable.ic_alarm_black_24dp, this, acAlarmDisplay, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenAlarm();
                return null;
            }
        });
        // TODO remove ScreenClean!
        this.mAcScreenClean = new Action(deviceManager.getNewId(),"Pebble watch mode clean", R.drawable.ic_cancel_black_24dp, this, acTimeDisplay, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenClean();
                return null;
            }
        });
        // Callable is null, will be overridden in instance with the correct parameters using getNotifyCallable(...)
        this.mAcNotify = new NotificationAction(deviceManager.getNewId(),"Pebble notification",R.drawable.ic_notifications_active_black_24dp,this,acNotify);
        this.events.put(mEvBtnUp.getId(),mEvBtnUp);
        this.events.put(mEvBtnSelect.getId(),mEvBtnSelect);
        this.events.put(mEvBtnDown.getId(),mEvBtnDown);
        this.events.put(mIaEvBtn.getId(),mIaEvBtn);
        this.actions.put(mAcVibrate.getId(),mAcVibrate);
        this.actions.put(mAcScreenTime.getId(),mAcScreenTime);
        this.actions.put(mAcScreenAlarm.getId(),mAcScreenAlarm);
        this.actions.put(mAcScreenClean.getId(),mAcScreenClean);
        this.actions.put(mAcNotify.getId(),mAcNotify);
    }

    // getters

    public ArrayList<InputAction> getInputActions() {
        return inputActions;
    }

    public InputActionEvent getInputActionEvent(InputAction inputAction){
        return inputActionEvents.get(inputAction.getId());
    }

    // These getters only needed for testing!

    public InputActionEvent getIaBtn(){return mIaEvBtn;};

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
        Action simVibrate = new Action(0,"Pebble Vibrate",0, this, null, new Callable<String>() {
            @Override
            public String call() throws Exception {
                simulateActionVibrate();
                return null;
            }
        });
        return simVibrate;
    }

    public Action getSimScreenTime() {
        Action simScreenTime = new Action(0,"Pebble Watch Mode Time",0, this, null, new Callable<String>() {
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
        mIaEvBtnUp.trigger();
    }

    public void evBtnSelect(){
        //Log.d("TRGRD", "Pebble Event Button Select");
        //System.out.println("[Pebble] Button Select!");
        mEvBtnSelect.trigger();
        mIaEvBtnSelect.trigger();
    }

    public void evBtnDown(){
        //Log.d("TRGRD", "Pebble Event Button Down");
        //System.out.println("[Pebble] Button Down!");
        mEvBtnDown.trigger();
        mIaEvBtnDown.trigger();
    }

    public void acVibrate(){
        System.out.println("[Pebble] Vibrates!");
        Intent newIntent = new Intent(PEBBLE_VIBRATE_ACTION);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
        //Toast.makeText(ruleSystemService, "Pebble Vibrate triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    public void acScreenTime(){
        System.out.println("[Pebble] shows time!");
        Intent newIntent = new Intent(PEBBLE_SCREEN_TIME_ACTION);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
        //Toast.makeText(ruleSystemService, "Pebble Screen Time triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    public void acScreenAlarm(){
        // TEST: first vibrate pebble!
        Intent vibrateIntent = new Intent(PEBBLE_VIBRATE_ACTION);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(vibrateIntent);
        System.out.println("[Pebble] shows alarm!");
        Intent newIntent = new Intent(PEBBLE_SCREEN_ALARM_ACTION);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
        //Toast.makeText(ruleSystemService, "Pebble Screen Alarm triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    public void acScreenClean(){
        System.out.println("[Pebble] cleans screen!");
        Intent newIntent = new Intent(PEBBLE_SCREEN_CLEAN_ACTION);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
        //Toast.makeText(ruleSystemService, "Pebble Screen Clean triggered by TRGRD", Toast.LENGTH_SHORT).show();
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
        IntentFilter filter = new IntentFilter(PebbleCommunicationService.PEBBLE_BUTTON_UP_EVENT);
        filter.addAction(PebbleCommunicationService.PEBBLE_BUTTON_SELECT_EVENT);
        filter.addAction(PebbleCommunicationService.PEBBLE_BUTTON_DOWN_EVENT);
        LocalBroadcastManager.getInstance(ruleSystemService).registerReceiver(mReceiver, filter);
        Log.d("TRGRD","Pebble mReceiver registered!");
    }

    public void unRegisterPebbleReceiver(){
        LocalBroadcastManager.getInstance(ruleSystemService).unregisterReceiver(mReceiver);
        Log.d("TRGRD","Pebble mReceiver unRegistered!");
    }

    // Start & Stop Pebble Communication Service

    public void startCommunicationService(){
        Intent pebbleService = new Intent(ruleSystemService, PebbleCommunicationService.class);
        ruleSystemService.startService(pebbleService);
    }

    public void stopCommunicationService(){
        Intent pebbleService = new Intent(ruleSystemService, PebbleCommunicationService.class);
        ruleSystemService.stopService(pebbleService);
    }

    @Override
    public void start(){
        this.startCommunicationService();
        this.registerPebbleReceiver();
        this.started = true;
        //deviceManager.sendRefreshBroadcast();
    }

    @Override
    public void stop(){
        this.unRegisterPebbleReceiver();
        this.stopCommunicationService();
        this.started = false;
        //deviceManager.sendRefreshBroadcast();
    }

    @Override
    public NotificationAction getNotifyAction(String title, String text, NotificationAction.NotificationActionType type) {
        NotificationAction instance = new NotificationAction(deviceManager.getNewId(),mAcNotify,title,text,getNotifyCallable(title,text,type));
        actionInstances.put(instance.getId(),instance);
        return instance;
    }

    @Override
    public void editNotifyAction(NotificationAction notificationAction, String title, String text) {
        notificationAction.setTitle(title);
        notificationAction.setText(text);
        notificationAction.setCallable(getNotifyCallable(title,text,notificationAction.getNotificationActionType()));
    }

    @Override
    public Callable getNotifyCallable(final String title, final String text, final NotificationAction.NotificationActionType type) {
        return new Callable<String>(){
            @Override
            public String call() throws Exception {
                acNotify(title, text, type);
                return null;
            }
        };
    }

    @Override
    public void acNotify(String title, String text, NotificationAction.NotificationActionType type) {
        System.out.println("[Pebble] Vibrates!");
        Intent newIntent = new Intent(PEBBLE_NOTIFICATION_ACTION);
        newIntent.putExtra("title",title);
        newIntent.putExtra("text",text);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
        //Toast.makeText(ruleSystemService, "Pebble Notification triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }
}
