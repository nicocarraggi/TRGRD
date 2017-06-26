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
import com.example.nicolascarraggi.trgrd.rulesys.InputAction;
import com.example.nicolascarraggi.trgrd.rulesys.InputActionEvent;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nicolascarraggi on 23/05/17.
 *
 * Not working correctly: fist, fingers spread
 * TODO: comment out gestures IF they work correctly!
 *
 */

public class MyoDevice extends Wearable implements InputActionDevice {

    public static final String MYO_START_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.start";
    public static final String MYO_STOP_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.stop";

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TRGRD", "Myo mReceiver " + action);

            if(action.equals(MyoCommunicationService.MYO_CONNECTED_EVENT)){
                myoConnected();
            } else if(action.equals(MyoCommunicationService.MYO_DISCONNECTED_EVENT)){
                myoDisconnected();
            } else if(action.equals(MyoCommunicationService.MYO_HUBERROR_EVENT)){
                myoHubError();
            } else if(action.equals(MyoCommunicationService.MYO_GESTURE_FIST_EVENT)){
                //evGestureFist();
            } else if(action.equals(MyoCommunicationService.MYO_GESTURE_WAVEIN_EVENT)){
                evGestureWaveIn();
            } else if(action.equals(MyoCommunicationService.MYO_GESTURE_WAVEOUT_EVENT)){
                evGestureWaveOut();
            } else if(action.equals(MyoCommunicationService.MYO_GESTURE_DOUBLETAP_EVENT)){
                evGestureDoubleTap();
            } else if(action.equals(MyoCommunicationService.MYO_GESTURE_FINGERSSPREAD_EVENT)){
                //evGestureFingersSpread();
            }
        }
    };

    private void myoConnected() {
        this.setStatus("[ connected ]");
        deviceManager.sendRefreshBroadcast();
    }

    private void myoDisconnected() {
        stop();
    }

    private void myoHubError() { stop(); }

    private InputAction mIaGestFist, mIaGestWaveIn, mIaGestWaveOut, mIaGestDoubleTap, mIaGestFingSpread;
    private InputActionEvent mIaEvGest, mIaEvGestFist, mIaEvGestWaveIn, mIaEvGestWaveOut, mIaEvGestDoubleTap, mIaEvGestFingSpread;
    private ArrayList<InputAction> inputActions;
    private HashMap<Integer, InputActionEvent> inputActionEvents;

    //private Event mEvGestureFist, mEvGestureWaveIn, mEvGestureWaveOut, mEvGestureDoubleTap, mEvGestureFingersSpread;

    public MyoDevice(RuleSystemService ruleSystemService, DeviceManager deviceManager, EventType evGesture){
        super(ruleSystemService.getNewId(), "Myo Gesture Armband", "Thalmic Labs", "Myo OS", "Armband", "Arm", R.drawable.ic_pan_tool_black_24dp,ruleSystemService,deviceManager);
        this.inputActions = new ArrayList<>();
        this.inputActionEvents = new HashMap<>();
        this.eventTypes.put(evGesture.getId(),evGesture);
        // INPUT ACTIONS
        //this.mIaGestFist = new InputAction(deviceManager.getNewId(),"MYO gesture:","Fist",R.drawable.ic_gesture_black_24dp);
        this.mIaGestWaveIn = new InputAction(deviceManager.getNewId(),"MYO armband gesture:","Wave in",R.drawable.ic_gesture_black_24dp);
        this.mIaGestWaveOut = new InputAction(deviceManager.getNewId(),"MYO armband gesture:","Wave out",R.drawable.ic_gesture_black_24dp);
        this.mIaGestDoubleTap = new InputAction(deviceManager.getNewId(),"MYO armband gesture:","Double tap",R.drawable.ic_gesture_black_24dp);
        //this.mIaGestFingSpread = new InputAction(deviceManager.getNewId(),"MYO gesture:","Fingers spread",R.drawable.ic_gesture_black_24dp);
        //this.inputActions.add(mIaGestFist);
        this.inputActions.add(mIaGestWaveIn);
        this.inputActions.add(mIaGestWaveOut);
        this.inputActions.add(mIaGestDoubleTap);
        //this.inputActions.add(mIaGestFingSpread);
        // INPUT ACTION EVENTS
        this.mIaEvGest = new InputActionEvent(deviceManager.getNewId(),"MYO armband ... gesture is made","gesture", R.drawable.ic_gesture_black_24dp, this, evGesture);
        //this.mIaEvGestFist = new InputActionEvent(deviceManager.getNewId(),mIaEvGest,mIaGestFist);
        this.mIaEvGestWaveIn = new InputActionEvent(deviceManager.getNewId(),mIaEvGest,mIaGestWaveIn);
        this.mIaEvGestWaveOut = new InputActionEvent(deviceManager.getNewId(),mIaEvGest,mIaGestWaveOut);
        this.mIaEvGestDoubleTap = new InputActionEvent(deviceManager.getNewId(),mIaEvGest,mIaGestDoubleTap);
        //this.mIaEvGestFingSpread = new InputActionEvent(deviceManager.getNewId(),mIaEvGest,mIaGestFingSpread);
        ///this.inputActionEvents.put(mIaEvGestFist.getId(), mIaEvGestFist);
        this.inputActionEvents.put(mIaEvGestWaveIn.getId(), mIaEvGestWaveIn);
        this.inputActionEvents.put(mIaEvGestWaveOut.getId(), mIaEvGestWaveOut);
        this.inputActionEvents.put(mIaEvGestDoubleTap.getId(), mIaEvGestDoubleTap);
        //this.inputActionEvents.put(mIaEvGestFingSpread.getId(), mIaEvGestFingSpread);
        // EVENTS
        this.events.put(mIaEvGest.getId(),mIaEvGest);
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

    public InputActionEvent getGest() {
        return mIaEvGest;
    }

    public InputActionEvent getGestFist() {
        return mIaEvGestFist;
    }

    public InputActionEvent getGestWaveIn() {
        return mIaEvGestWaveIn;
    }

    public InputActionEvent getGestWaveOut() {
        return mIaEvGestWaveOut;
    }

    public InputActionEvent getGestDoubleTap() {
        return mIaEvGestDoubleTap;
    }

    public InputActionEvent getGestFingSpread() {
        return mIaEvGestFingSpread;
    }


    // Event triggers

    private void evGestureFist() {
        //mIaEvGestFist.trigger();
    }

    private void evGestureWaveIn() {
        mIaEvGestWaveIn.trigger();
    }

    private void evGestureWaveOut() {
        mIaEvGestWaveOut.trigger();
    }

    private void evGestureDoubleTap() {
        mIaEvGestDoubleTap.trigger();
    }

    private void evGestureFingersSpread() {
        //mIaEvGestFingSpread.trigger();
    }

    // Register & UnRegister Myo Broadcast Receiver

    public void registerMyoReceiver(){
        IntentFilter filter = new IntentFilter(MyoCommunicationService.MYO_GESTURE_FIST_EVENT);
        filter.addAction(MyoCommunicationService.MYO_GESTURE_WAVEIN_EVENT);
        filter.addAction(MyoCommunicationService.MYO_GESTURE_WAVEOUT_EVENT);
        filter.addAction(MyoCommunicationService.MYO_GESTURE_DOUBLETAP_EVENT);
        filter.addAction(MyoCommunicationService.MYO_GESTURE_FINGERSSPREAD_EVENT);
        filter.addAction(MyoCommunicationService.MYO_CONNECTED_EVENT);
        filter.addAction(MyoCommunicationService.MYO_DISCONNECTED_EVENT);
        filter.addAction(MyoCommunicationService.MYO_HUBERROR_EVENT);
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

    @Override
    public void start(){
        this.startCommunicationService();
        this.registerMyoReceiver();
        this.started = true;
        this.setStatus("[ searching... ]");
        deviceManager.sendRefreshBroadcast();
        // TODO set a limit on searching time ( in which the user has to tap myo to device ) -> 10secs?
    }

    @Override
    public void stop(){
        if (started) {
            this.setStatus("");
            this.unRegisterMyoReceiver();
            this.stopCommunicationService();
            this.started = false;
            deviceManager.sendRefreshBroadcast();
        }
    }
}
