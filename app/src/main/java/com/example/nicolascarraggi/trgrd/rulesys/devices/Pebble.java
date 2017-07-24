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
import com.example.nicolascarraggi.trgrd.rulesys.ScoreValueAction;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;
import com.example.nicolascarraggi.trgrd.rulesys.ValueAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 5/04/17.
 */

public class Pebble extends Wearable implements NotificationDevice, ScoreDevice, InputActionDevice {

    // Constants

    public static final String PEBBLE_VIBRATE_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.VIBRATE";
    public static final String PEBBLE_NOTIFICATION_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.NOTIFICATION";
    public static final String PEBBLE_SCREEN_TIME_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_TIME";
    public static final String PEBBLE_SCREEN_SPORT_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_SPORT";
    public static final String PEBBLE_SCREEN_ALARM_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_ALARM";
    public static final String PEBBLE_SCREEN_CLEAN_ACTION = "com.example.nicolascarraggi.trgrd.rulesys.devices.SCREEN_CLEAN";

    // TODO keep popup stack? ( if multiple popups show up at the same time )

    // Watchmode enum
    private enum PebbleWatchMode {
        Time, Sport, Popup, AlarmPopup
    }

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
            } else if(action.equals(PebbleCommunicationService.PEBBLE_SHAKE_EVENT)) {
                evShake();
            } else if(action.equals(PebbleCommunicationService.PEBBLE_WAKEUP_EVENT)) {
                evWakeup();
            } else if(action.equals(PebbleCommunicationService.PEBBLE_REST_EVENT)) {
                evRest();
            } else if(action.equals(PebbleCommunicationService.PEBBLE_PHYSICAL_EVENT)) {
                evPhysical();
            }
        }
    };

    private PebbleScore mScore;
    private PebbleWatchMode mWatchMode;
    private PebbleWatchMode mWatchModeBeforePopup;

    private InputAction mIaBtnUp, mIaBtnSelect, mIaBtnDown;
    private InputActionEvent mIaEvBtn, mIaEvBtnUp, mIaEvBtnSelect, mIaEvBtnDown;
    private ArrayList<InputAction> inputActions;
    private HashMap<Integer, InputActionEvent> inputActionEvents;

    //private Event mEvBtnUp, mEvBtnSelect, mEvBtnDown;
    private Event mEvShake, mEvWakesUp, mEvActivityRest, mEvActivityPhysical;
    private State mStWatchModeTime, mStWatchModeSport, mStWatchModePopup;
    private Action mAcVibrate, mAcScreenTime, mAcScreenSport, mAcScreenAlarm, mAcScreenClean,
            mAcScoreAddOneLeft, mAcScoreAddOneRight, mAcScoreSubtractOneLeft, mAcScoreSubtractOneRight;
    private NotificationAction mAcNotify;
    private ScoreValueAction mAcScoreAddXLeft,mAcScoreAddXRight, mAcScoreSubtractXLeft, mAcScoreSubtractXRight;

    public Pebble(RuleSystemService ruleSystemService, DeviceManager deviceManager) {
        super(ruleSystemService.getNewId(), "Pebble Watch", "Pebble", "Pebble OS", "Watch", "Wrist", R.drawable.ic_watch_black_24dp, ruleSystemService, deviceManager);
        this.inputActions = new ArrayList<>();
        this.inputActionEvents = new HashMap<>();
        this.eventTypes.put(deviceManager.getEvButtonPress().getId(),deviceManager.getEvButtonPress());
        this.eventTypes.put(deviceManager.getEvHeartRateReading().getId(),deviceManager.getEvHeartRateReading());
        this.eventTypes.put(deviceManager.getEvGesture().getId(),deviceManager.getEvGesture());
        this.eventTypes.put(deviceManager.getEvWakesUp().getId(),deviceManager.getEvWakesUp());
        this.eventTypes.put(deviceManager.getEvActivityChange().getId(),deviceManager.getEvActivityChange());
        this.stateTypes.put(deviceManager.getStWatchMode().getId(),deviceManager.getStWatchMode());
        this.actionTypes.put(deviceManager.getAcVibrate().getId(),deviceManager.getAcVibrate());
        this.actionTypes.put(deviceManager.getAcAlarmDisplay().getId(),deviceManager.getAcAlarmDisplay());
        this.actionTypes.put(deviceManager.getAcWatchMode().getId(),deviceManager.getAcWatchMode());
        this.actionTypes.put(deviceManager.getAcTimeDisplay().getId(),deviceManager.getAcTimeDisplay());
        this.actionTypes.put(deviceManager.getAcSportDisplay().getId(),deviceManager.getAcSportDisplay());
        this.actionTypes.put(deviceManager.getAcScoreAdjust().getId(),deviceManager.getAcScoreAdjust());
        this.actionTypes.put(deviceManager.getAcNotify().getId(),deviceManager.getAcNotify());
        // INPUT ACTIONS
        this.mIaBtnUp = new InputAction(deviceManager.getNewId(),"Pebble watch button:","UP",R.drawable.ic_keyboard_arrow_up_black_24dp);
        this.mIaBtnSelect = new InputAction(deviceManager.getNewId(),"Pebble watch button:","SELECT",R.drawable.ic_keyboard_arrow_right_black_24dp);
        this.mIaBtnDown = new InputAction(deviceManager.getNewId(),"Pebble watch button:","DOWN",R.drawable.ic_keyboard_arrow_down_black_24dp);
        this.inputActions.add(mIaBtnUp);
        this.inputActions.add(mIaBtnSelect);
        this.inputActions.add(mIaBtnDown);
        // INPUT ACTION EVENTS
        this.mIaEvBtn = new InputActionEvent(deviceManager.getNewId(),"Pebble watch ... button is pressed","button", R.drawable.ic_adjust_black_24dp, this, deviceManager.getEvButtonPress(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mIaEvBtnUp = new InputActionEvent(deviceManager.getNewId(),mIaEvBtn,mIaBtnUp);
        this.mIaEvBtnSelect = new InputActionEvent(deviceManager.getNewId(),mIaEvBtn,mIaBtnSelect);
        this.mIaEvBtnDown = new InputActionEvent(deviceManager.getNewId(),mIaEvBtn,mIaBtnDown);
        this.inputActionEvents.put(mIaBtnUp.getId(), mIaEvBtnUp);
        this.inputActionEvents.put(mIaBtnSelect.getId(), mIaEvBtnSelect);
        this.inputActionEvents.put(mIaBtnDown.getId(), mIaEvBtnDown);
        // EVENTS
        this.mEvShake = new Event(deviceManager.getNewId(),"Pebble shake", R.drawable.ic_gesture_black_24dp, this, deviceManager.getEvGesture(), ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvWakesUp = new Event(deviceManager.getNewId(),"Pebble detects wake up",R.drawable.ic_wb_sunny_black_24dp,this,deviceManager.getEvWakesUp(),ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvActivityRest = new Event(deviceManager.getNewId(),"Pebble detects REST activity",R.drawable.ic_person_black_24dp,this,deviceManager.getEvActivityChange(),ruleSystemService.getRuleManager().getRuleEngine());
        this.mEvActivityPhysical = new Event(deviceManager.getNewId(),"Pebble detects PHYSICAL activity",R.drawable.ic_directions_run_black_24dp,this,deviceManager.getEvActivityChange(),ruleSystemService.getRuleManager().getRuleEngine());
        //this.mEvBtnUp = new Event(deviceManager.getNewId(),"Pebble UP button is pressed", R.drawable.ic_keyboard_arrow_up_black_24dp, this, evButtonPress);
        //this.mEvBtnSelect = new Event(deviceManager.getNewId(),"Pebble SELECT button is pressed", R.drawable.ic_keyboard_arrow_right_black_24dp, this, evButtonPress);
        //this.mEvBtnDown = new Event(deviceManager.getNewId(),"Pebble DOWN button is pressed", R.drawable.ic_keyboard_arrow_down_black_24dp, this, evButtonPress);
        // STATES
        this.mStWatchModeTime = new State(deviceManager.getNewId(),"Pebble watch is currently in mode TIME", R.drawable.ic_access_time_black_24dp,this,deviceManager.getStWatchMode(), true, ruleSystemService.getRuleManager().getRuleEngine());
        this.mStWatchModeSport = new State(deviceManager.getNewId(),"Pebble watch is currently in mode SPORT", R.drawable.ic_directions_run_black_24dp,this,deviceManager.getStWatchMode(), false, ruleSystemService.getRuleManager().getRuleEngine());
        this.mStWatchModePopup = new State(deviceManager.getNewId(),"Pebble watch is currently in mode POPUP", R.drawable.ic_content_copy_black_24dp,this,deviceManager.getStWatchMode(), false, ruleSystemService.getRuleManager().getRuleEngine());
        // ACTIONS
        this.mAcVibrate = new Action(deviceManager.getNewId(),"Pebble watch vibrate", R.drawable.ic_vibration_black_24dp, this, deviceManager.getAcVibrate(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                acVibrate();
                return null;
            }
        });
        // mAcScreenTime had type acTimeDisplay, now acWatchMode -> TODO allow multiple types
        this.mAcScreenTime = new Action(deviceManager.getNewId(),"Set Pebble watch in TIME mode", R.drawable.ic_access_time_black_24dp, this, deviceManager.getAcWatchMode(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenTime();
                return null;
            }
        });
        this.mAcScreenSport = new Action(deviceManager.getNewId(),"Set Pebble watch in SPORT mode", R.drawable.ic_directions_run_black_24dp, this, deviceManager.getAcWatchMode(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenSport();
                return null;
            }
        });
        this.mAcScreenAlarm = new Action(deviceManager.getNewId(),"Display alarm on Pebble watch", R.drawable.ic_alarm_black_24dp, this, deviceManager.getAcAlarmDisplay(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenAlarm();
                return null;
            }
        });
        // TODO remove ScreenClean!
        this.mAcScreenClean = new Action(deviceManager.getNewId(),"Clean Pebble watch display", R.drawable.ic_cancel_black_24dp, this, deviceManager.getAcTimeDisplay(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScreenClean();
                return null;
            }
        });
        // SCORE ADJUST
        /*this.mAcScoreAddOneLeft = new Action(deviceManager.getNewId(),"+ 1 to Pebble watch score LEFT", R.drawable.ic_exposure_plus_1_black_24dp, this, acScoreAdjust, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScoreAddLeft(1);
                return null;
            }
        });
        this.mAcScoreAddOneRight = new Action(deviceManager.getNewId(),"+ 1 to Pebble watch score RIGHT", R.drawable.ic_exposure_plus_1_black_24dp, this, acScoreAdjust, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScoreAddRight(1);
                return null;
            }
        });
        this.mAcScoreSubtractOneLeft = new Action(deviceManager.getNewId(),"- 1 to Pebble watch score LEFT", R.drawable.ic_exposure_neg_1_black_24dp, this, acScoreAdjust, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScoreSubtractLeft(1);
                return null;
            }
        });
        this.mAcScoreSubtractOneRight = new Action(deviceManager.getNewId(),"- 1 to Pebble watch score RIGHT", R.drawable.ic_exposure_neg_1_black_24dp, this, acScoreAdjust, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acScoreSubtractRight(1);
                return null;
            }
        });
        */
        // Callable is null, will be overridden in instance with the correct parameters using getNotifyCallable(...)
        this.mAcNotify = new NotificationAction(deviceManager.getNewId(),"Pebble watch notification",R.drawable.ic_notifications_active_black_24dp,this,deviceManager.getAcNotify());
        // Callable is null, will be overridden in instance with the correct parameters using getValueActionCallable(...)
        this.mAcScoreAddXLeft = new ScoreValueAction(deviceManager.getNewId(),"+ ... to Pebble watch score LEFT", R.drawable.ic_plus_x_black, this, deviceManager.getAcScoreAdjust()
                , ScoreValueAction.ScoreSide.LEFT, ScoreValueAction.ScoreValueActionType.ADD);
        this.mAcScoreAddXRight = new ScoreValueAction(deviceManager.getNewId(),"+ ... to Pebble watch score RIGHT", R.drawable.ic_plus_x_black, this, deviceManager.getAcScoreAdjust()
                , ScoreValueAction.ScoreSide.RIGHT, ScoreValueAction.ScoreValueActionType.ADD);
        this.mAcScoreSubtractXLeft = new ScoreValueAction(deviceManager.getNewId(),"- ... to Pebble watch score LEFT", R.drawable.ic_minus_x_black, this, deviceManager.getAcScoreAdjust()
                , ScoreValueAction.ScoreSide.LEFT, ScoreValueAction.ScoreValueActionType.SUBTRACT);
        this.mAcScoreSubtractXRight = new ScoreValueAction(deviceManager.getNewId(),"- ... to Pebble watch score RIGHT", R.drawable.ic_minus_x_black, this, deviceManager.getAcScoreAdjust()
                , ScoreValueAction.ScoreSide.RIGHT, ScoreValueAction.ScoreValueActionType.SUBTRACT);

        // EVENTS PUT
        //this.events.put(mEvBtnUp.getId(),mEvBtnUp);
        //this.events.put(mEvBtnSelect.getId(),mEvBtnSelect);
        //this.events.put(mEvBtnDown.getId(),mEvBtnDown);
        this.events.put(mIaEvBtn.getId(),mIaEvBtn);
        this.events.put(mEvShake.getId(),mEvShake);
        this.events.put(mEvWakesUp.getId(),mEvWakesUp);
        this.events.put(mEvActivityRest.getId(),mEvActivityRest);
        this.events.put(mEvActivityPhysical.getId(),mEvActivityPhysical);
        // STATES PUT
        this.states.put(mStWatchModeTime.getId(),mStWatchModeTime);
        this.states.put(mStWatchModeSport.getId(),mStWatchModeSport);
        this.states.put(mStWatchModePopup.getId(),mStWatchModePopup);
        // ACTIONS PUT
        this.actions.put(mAcVibrate.getId(),mAcVibrate);
        this.actions.put(mAcScreenTime.getId(),mAcScreenTime);
        this.actions.put(mAcScreenSport.getId(),mAcScreenSport);
        this.actions.put(mAcScreenAlarm.getId(),mAcScreenAlarm);
        //this.actions.put(mAcScreenClean.getId(),mAcScreenClean); TEMPORARY DISABLED!!
        this.actions.put(mAcNotify.getId(),mAcNotify);
        //this.actions.put(mAcScoreAddOneLeft.getId(), mAcScoreAddOneLeft);
        //this.actions.put(mAcScoreAddOneRight.getId(), mAcScoreAddOneRight);
        //this.actions.put(mAcScoreSubtractOneLeft.getId(), mAcScoreSubtractOneLeft);
        //this.actions.put(mAcScoreSubtractOneRight.getId(), mAcScoreSubtractOneRight);
        this.actions.put(mAcScoreAddXLeft.getId(), mAcScoreAddXLeft);
        this.actions.put(mAcScoreAddXRight.getId(), mAcScoreAddXRight);
        this.actions.put(mAcScoreSubtractXLeft.getId(), mAcScoreSubtractXLeft);
        this.actions.put(mAcScoreSubtractXRight.getId(), mAcScoreSubtractXRight);
        // initiate score: 0  -  0
        this.mScore = new PebbleScore(0,0);
        // initiate watch mode: time
        this.mWatchMode = PebbleWatchMode.Time;
        this.mWatchModeBeforePopup = PebbleWatchMode.Time;
    }

    // InputActionDevice getters

    @Override
    public ArrayList<InputAction> getInputActions() {
        return inputActions;
    }

    @Override
    public InputActionEvent getInputActionEvent(InputAction inputAction){
        return inputActionEvents.get(inputAction.getId());
    }

    // Event getters

    public Event getBtn(){return mIaEvBtn;};

    public Event getBtnUp() {
        return mIaEvBtnUp;
    }

    public Event getBtnSelect() {
        return mIaEvBtnSelect;
    }

    public Event getBtnDown() {
        return mIaEvBtnDown;
    }

    public Event getShake() {
        return mEvShake;
    }

    public Event getWakesUp() {
        return mEvWakesUp;
    }

    public Event getRest() {
        return mEvActivityRest;
    }

    public Event getPhysical() {
        return mEvActivityPhysical;
    }
    // State getters

    public State getWatchModeTime() {
        return mStWatchModeTime;
    }

    public State getWatchModeSport() {
        return mStWatchModeSport;
    }

    public State getWatchModePopup() {
        return mStWatchModePopup;
    }

    // Action getters

    public Action getVibrate() {
        return mAcVibrate;
    }

    public Action getScreenTime() {
        return mAcScreenTime;
    }

    public Action getScreenSport() {
        return mAcScreenSport;
    }

    public Action getScreenAlarm() {
        return mAcScreenAlarm;
    }

    public Action getScreenClean() {
        return mAcScreenClean;
    }

    public Action getAddScoreOneLeft() {
        return mAcScoreAddOneLeft;
    }

    public Action getAddScoreOneRight() {
        return mAcScoreAddOneRight;
    }

    public Action getSubtractScoreOneLeft() {
        return mAcScoreSubtractOneLeft;
    }

    public Action getSubtractScoreOneRight() {
        return mAcScoreSubtractOneRight;
    }

    public Action getAddScoreXLeft() {
        return mAcScoreAddXLeft;
    }

    public Action getAddScoreXRight() {
        return mAcScoreAddXRight;
    }

    public Action getSubtractScoreXLeft() {
        return mAcScoreSubtractXLeft;
    }

    public Action getSubtractScoreXRight() {
        return mAcScoreSubtractXRight;
    }

    // simulate ...
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

    // Event triggers

    public void evBtnUp(){
        mIaEvBtnUp.trigger();
    }

    public void evBtnSelect(){
        mIaEvBtnSelect.trigger();
    }

    public void evBtnDown(){
        mIaEvBtnDown.trigger();
    }

    public void evShake(){
        mEvShake.trigger();
    }

    public void evWakeup(){
        mEvWakesUp.trigger();
    }

    public void evRest() {
        mEvActivityRest.trigger();
    }

    public void evPhysical() {
        mEvActivityPhysical.trigger();
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
        setWatchModeTime();
        //Toast.makeText(ruleSystemService, "Pebble Screen Time triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    public void acScreenSport(){
        System.out.println("[Pebble] shows sport info!");
        Intent newIntent = new Intent(PEBBLE_SCREEN_SPORT_ACTION);
        newIntent.putExtra("score",mScore.toScoreString());
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
        setWatchModeSport();
    }

    public void acScreenAlarm(){
        // TEST: first vibrate pebble!
        Intent vibrateIntent = new Intent(PEBBLE_VIBRATE_ACTION);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(vibrateIntent);
        System.out.println("[Pebble] shows alarm!");
        Intent newIntent = new Intent(PEBBLE_SCREEN_ALARM_ACTION);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
        setWatchmodeAlarmPopup();
        //Toast.makeText(ruleSystemService, "Pebble Screen Alarm triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    public void acScreenClean(){
        System.out.println("[Pebble] cleans screen!");
        Intent newIntent = new Intent(PEBBLE_SCREEN_CLEAN_ACTION);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
        //Toast.makeText(ruleSystemService, "Pebble Screen Clean triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    // Watchmodes

    private void setWatchModeTimeStates(){
        mStWatchModeTime.setState(true);
        mStWatchModeSport.setState(false);
        mStWatchModePopup.setState(false);
    }

    private void setWatchModeSportStates(){
        mStWatchModeTime.setState(false);
        mStWatchModeSport.setState(true);
        mStWatchModePopup.setState(false);
    }

    private void setWatchModePopupStates(){
        mStWatchModeTime.setState(false);
        mStWatchModeSport.setState(false);
        mStWatchModePopup.setState(true);
    }

    private void setWatchModeTime(){
        this.mWatchMode = PebbleWatchMode.Time;
        setWatchModeTimeStates();
    }

    private void setWatchModeSport(){
        this.mWatchMode = PebbleWatchMode.Sport;
        setWatchModeSportStates();
    }

    private void setWatchmodePopup(){
        if(mWatchMode != PebbleWatchMode.Popup &&
                mWatchMode != PebbleWatchMode.AlarmPopup){
            // save which mode it was before the popup!
            this.mWatchModeBeforePopup = mWatchMode;
            this.mWatchMode = PebbleWatchMode.Popup;
            setWatchModePopupStates();
        }
    }

    private void setWatchmodeAlarmPopup(){
        if(mWatchMode != PebbleWatchMode.AlarmPopup &&
                mWatchMode != PebbleWatchMode.Popup){
            // save which mode it was before the popup!
            this.mWatchModeBeforePopup = mWatchMode;
            this.mWatchMode = PebbleWatchMode.AlarmPopup;
            setWatchModePopupStates();
        }
    }

    // Score adjust

    private void acScoreAddLeft(int x){
        mScore.addLeft(x);
        // refresh IF actually in sport mode
        if(mWatchMode == PebbleWatchMode.Sport){
            acScreenSport();
        }
    }

    private void acScoreAddRight(int x){
        mScore.addRight(x);
        // refresh IF actually in sport mode
        if(mWatchMode == PebbleWatchMode.Sport){
            acScreenSport();
        }
    }

    private void acScoreSubtractLeft(int x){
        mScore.subtractLeft(x);
        // refresh IF actually in sport mode
        if(mWatchMode == PebbleWatchMode.Sport){
            acScreenSport();
        }
    }

    private void acScoreSubtractRight(int x){
        mScore.subtractRight(x);
        // refresh IF actually in sport mode
        if(mWatchMode == PebbleWatchMode.Sport){
            acScreenSport();
        }
    }

    private void acScoreSetLeft(int x){
        mScore.setLeft(x);
        // refresh IF actually in sport mode
        if(mWatchMode == PebbleWatchMode.Sport){
            acScreenSport();
        }
    }

    private void acScoreSetRight(int x){
        mScore.setRight(x);
        // refresh IF actually in sport mode
        if(mWatchMode == PebbleWatchMode.Sport){
            acScreenSport();
        }
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
        filter.addAction(PebbleCommunicationService.PEBBLE_SHAKE_EVENT);
        filter.addAction(PebbleCommunicationService.PEBBLE_WAKEUP_EVENT);
        filter.addAction(PebbleCommunicationService.PEBBLE_REST_EVENT);
        filter.addAction(PebbleCommunicationService.PEBBLE_PHYSICAL_EVENT);
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

    // NOTIFICATION ACTION

    // creates a new instance
    @Override
    public NotificationAction getNotifyAction(String title, String text, NotificationAction.NotificationActionType type) {
        NotificationAction instance = new NotificationAction(deviceManager.getNewId(),mAcNotify,title,text,getNotifyCallable(title,text,type));
        actionInstances.put(instance.getId(),instance);
        return instance;
    }

    // edits an existing instance
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
        //System.out.println("[Pebble] Vibrates!");
        Intent newIntent = new Intent(PEBBLE_NOTIFICATION_ACTION);
        newIntent.putExtra("title",title);
        newIntent.putExtra("text",text);
        LocalBroadcastManager.getInstance(ruleSystemService).sendBroadcast(newIntent);
        //Toast.makeText(ruleSystemService, "Pebble Notification triggered by TRGRD", Toast.LENGTH_SHORT).show();
    }

    // VALUE ACTION

    // creates a new instance
    @Override
    public ScoreValueAction getScoreValueAction(ScoreValueAction scoreValueActionSkeleton, int value) {
        ScoreValueAction instance = new ScoreValueAction(deviceManager.getNewId(),scoreValueActionSkeleton,value,
                getScoreValueActionCallable(value,scoreValueActionSkeleton.getScoreValueActionType(),scoreValueActionSkeleton.getScoreSide()));
        actionInstances.put(instance.getId(),instance);
        return instance;
    }

    @Override
    public Callable getScoreValueActionCallable(final int value, final ScoreValueAction.ScoreValueActionType type, final ScoreValueAction.ScoreSide side) {
        return new Callable<String>(){
            @Override
            public String call() throws Exception {
                acScore(value, type, side);
                return null;
            }
        };
    }

    private void acScoreAdd(int value, ScoreValueAction.ScoreSide side){
        switch (side){
            case LEFT:
                acScoreAddLeft(value);
                break;
            case RIGHT:
                acScoreAddRight(value);
                break;
            default:
                break;
        }
    }

    private void acScoreSubtract(int value, ScoreValueAction.ScoreSide side){
        switch (side){
            case LEFT:
                acScoreSubtractLeft(value);
                break;
            case RIGHT:
                acScoreSubtractRight(value);
                break;
            default:
                break;
        }
    }

    private void acScoreSet(int value, ScoreValueAction.ScoreSide side){
        switch (side){
            case LEFT:
                acScoreSetLeft(value);
                break;
            case RIGHT:
                acScoreSetRight(value);
                break;
            default:
                break;
        }
    }

    @Override
    public void acScore(int value, ScoreValueAction.ScoreValueActionType scoreValueActionType, ScoreValueAction.ScoreSide side){
        switch (scoreValueActionType) {
            case ADD:
                acScoreAdd(value, side);
                break;
            case SUBTRACT:
                acScoreSubtract(value, side);
                break;
            case SET:
                acScoreSet(value,side);
                break;
            default:
                break;
        }
    }

    @Override
    public void editScoreValueAction(ScoreValueAction scoreValueAction, int value) {
        scoreValueAction.setValue(value);
        scoreValueAction.setCallable(getScoreValueActionCallable(value,scoreValueAction.getScoreValueActionType(),scoreValueAction.getScoreSide()));
    }

    public PebbleScore getScore() {
        return mScore;
    }

    public void setScore(int left, int right){
        acScoreSetLeft(left);
        acScoreSetRight(right);
    }
}
