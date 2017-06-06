package com.example.nicolascarraggi.trgrd.rulesys.devices;

/*
 * Copyright (C) 2014 Thalmic Labs Inc.
 * Distributed under the Myo SDK license agreement. See LICENSE.txt for details.
 */

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.thalmic.myo.scanner.Scanner;

public class MyoCommunicationService extends Service {

    // Constants

    public static final String MYO_CONNECTED_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.connected";
    public static final String MYO_DISCONNECTED_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.disconnected";
    public static final String MYO_HUBERROR_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.huberror";
    public static final String MYO_GESTURE_FIST_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-fist"; // NEVER TRIGGERED
    public static final String MYO_GESTURE_WAVEIN_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-wavein"; // OK
    public static final String MYO_GESTURE_WAVEOUT_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-waveout"; // TOO SENSITIVE
    public static final String MYO_GESTURE_DOUBLETAP_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-doubletap"; // OK
    public static final String MYO_GESTURE_FINGERSSPREAD_EVENT = "com.example.nicolascarraggi.trgrd.rulesys.devices.myo.gesture-fingersspread"; // NEVER TRIGGERED

    private static final String TAG = "MyoCommunicationService";
    private Toast mToast;
    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {
        @Override
        public void onConnect(Myo myo, long timestamp) {
            Intent intent = new Intent(MYO_CONNECTED_EVENT);
            LocalBroadcastManager.getInstance(MyoCommunicationService.this).sendBroadcast(intent);
            showToast(getString(R.string.myo_connected));
        }
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            Intent intent = new Intent(MYO_DISCONNECTED_EVENT);
            LocalBroadcastManager.getInstance(MyoCommunicationService.this).sendBroadcast(intent);
            showToast(getString(R.string.myo_disconnected));
        }
        // onPose() is called whenever the Myo detects that the person wearing it has changed their pose, for example,
        // making a fist, or not making a fist anymore.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Show the name of the pose in a toast.
            // showToast(getString(R.string.myo_pose, pose.toString()));
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
            Intent intent = new Intent(MYO_HUBERROR_EVENT);
            LocalBroadcastManager.getInstance(MyoCommunicationService.this).sendBroadcast(intent);
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
        Scanner scanner = Hub.getInstance().getScanner();
        if(scanner != null) Hub.getInstance().getScanner().stopScanning();
        Log.d("TRGRD","MyoCommunicationService onDestroy()");
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