package com.example.nicolascarraggi.trgrd.rulesys.devices;

/*
 * Copyright (C) 2014 Thalmic Labs Inc.
 * Distributed under the Myo SDK license agreement. See LICENSE.txt for details.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.R;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;

public class MyoCommunicationService extends Service {

    // Constants

    public static final String MYO_GESTURE_FIST_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-fist";
    public static final String MYO_GESTURE_WAVEIN_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-wavein";
    public static final String MYO_GESTURE_WAVEOUT_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-waveout";
    public static final String MYO_GESTURE_DOUBLETAP_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-doubletap";
    public static final String MYO_GESTURE_FINGERSSPREAD_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-fingersspread";

    private static final String TAG = "MyoCommunicationService";
    private Toast mToast;
    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {
        @Override
        public void onConnect(Myo myo, long timestamp) {
            showToast(getString(R.string.connected));
        }
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            showToast(getString(R.string.disconnected));
        }
        // onPose() is called whenever the Myo detects that the person wearing it has changed their pose, for example,
        // making a fist, or not making a fist anymore.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Show the name of the pose in a toast.
            showToast(getString(R.string.pose, pose.toString()));
            String poseName = pose.toString();
            Intent newIntent = null;
            if(poseName.equals("FIST")){
                newIntent = new Intent(MYO_GESTURE_FIST_EVENT);
            } else if(poseName.equals("WAVE_IN")){
                newIntent = new Intent(MYO_GESTURE_WAVEIN_EVENT);
            } else if(poseName.equals("WAVE_OUT")){
                newIntent = new Intent(MYO_GESTURE_WAVEOUT_EVENT);
            } else if(poseName.equals("DOUBLE_TAP")){
                newIntent = new Intent(MYO_GESTURE_DOUBLETAP_EVENT);
            } else if(poseName.equals("FINGERS_SPREAD")){
                newIntent = new Intent(MYO_GESTURE_FINGERSSPREAD_EVENT);
            }
            if(newIntent != null){
                LocalBroadcastManager.getInstance(MyoCommunicationService.this).sendBroadcast(newIntent);
            }
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // First, we initialize the Hub singleton with an application identifier.
        Hub hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            showToast("Couldn't initialize Hub");
            stopSelf();
            return;
        }
        // Disable standard Myo locking policy. All poses will be delivered.
        hub.setLockingPolicy(Hub.LockingPolicy.NONE);
        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);
        // Finally, scan for Myo devices and connect to the first one found that is very near.
        hub.attachToAdjacentMyo();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Service is gone, so unregister the listener.
        Hub.getInstance().removeListener(mListener);
        Hub.getInstance().shutdown();
    }
    private void showToast(String text) {
        //Log.w(TAG, text);
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }
}