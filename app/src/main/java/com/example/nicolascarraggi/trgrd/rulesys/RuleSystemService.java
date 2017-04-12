package com.example.nicolascarraggi.trgrd.rulesys;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RuleSystemService extends Service {

    private DeviceManager mDeviceManager;

    private Rule mTestRuleAlarmStartPebble,mTestRuleAlarmDismissPebble,mTestRuleAlarmDonePebble;

    public RuleSystemService() {
        mDeviceManager = new DeviceManager(this);
        mTestRuleAlarmStartPebble = new Rule("testRuleAlarmStartPebble");
        mTestRuleAlarmDismissPebble = new Rule("testRuleAlarmDismissPebble");
        mTestRuleAlarmDonePebble = new Rule("testRuleAlarmDonePebble");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDeviceManager.startDevices();
        testAlarmPebble();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTestRuleAlarmStartPebble.setActive(false);
        mTestRuleAlarmDismissPebble.setActive(false);
        mTestRuleAlarmDonePebble.setActive(false);
        mDeviceManager.stopDevices();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // RULE TESTS
    private void testAlarmPebble(){
        // Alarm start! -> Pebble Screen Alarm!
        mTestRuleAlarmStartPebble.addEvent(mDeviceManager.getAndroidPhone().getAlarmStart());
        mTestRuleAlarmStartPebble.addAction(mDeviceManager.getPebble().getScreenAlarm());
        mTestRuleAlarmStartPebble.setActive(true);
        // Alarm Pebble Button Down -> dismiss alarm!
        mTestRuleAlarmDismissPebble.addState(mDeviceManager.getAndroidPhone().getAlarmGoing());
        mTestRuleAlarmDismissPebble.addEvent(mDeviceManager.getPebble().getBtnDown());
        mTestRuleAlarmDismissPebble.addAction(mDeviceManager.getAndroidPhone().getAcAlarmDismiss());
        // Alarm done! -> Pebble Screen Clean!
        mTestRuleAlarmDonePebble.addEvent(mDeviceManager.getAndroidPhone().getAlarmDone());
        mTestRuleAlarmDonePebble.addAction(mDeviceManager.getPebble().getScreenClean());
    }

}
