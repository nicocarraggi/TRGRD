package com.example.nicolascarraggi.trgrd.rulesys;

import android.content.Context;

import com.example.nicolascarraggi.trgrd.rulesys.devices.AndroidPhone;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Pebble;

/**
 * Created by nicolascarraggi on 11/04/17.
 */

public class DeviceManager {

    private Context mContext;
    private AndroidPhone mAndroidPhone;
    private Pebble mPebble;

    public DeviceManager(Context mContext) {
        this.mContext = mContext;
        this.mAndroidPhone = new AndroidPhone(mContext);
        this.mPebble = new Pebble(mContext);
    }

    public AndroidPhone getAndroidPhone() {
        return mAndroidPhone;
    }

    public Pebble getPebble() {
        return mPebble;
    }


    // Start & Stop Devices:
    // Start & Stop Services from all devices!!!
    // Register & unRegister Receivers from all devices!!!
    // startDevices must be called in onResume method of Activity OR onCreate of Service
    // stopDevices must be called in onPause method of Activity OR onDestroy of Service

    public void startDevices(){
        // Services
        mPebble.startCommunicationService();
        // Receivers
        mAndroidPhone.registerAndroidPhoneReceiver();
        mPebble.registerPebbleReceiver();
    }

    public void stopDevices(){
        // Services
        mPebble.stopCommunicationService();
        // Receivers
        mAndroidPhone.unRegisterAndroidPhoneReceiver();
        mPebble.unRegisterPebbleReceiver();
    }


}
