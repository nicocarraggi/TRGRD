package com.example.nicolascarraggi.trgrd.rulesys;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.HashSet;
import java.util.Set;

public class RuleSystemService extends Service {

    private final IBinder ruleSystemBinder = new RuleSystemBinder();

    private DeviceManager mDeviceManager;

    private Rule mTestRuleAlarmStartPebble,mTestRuleAlarmDismissPebble,mTestRuleAlarmDonePebble;

    private Set<Rule> rules;

    public RuleSystemService() {
        this.mDeviceManager = new DeviceManager(this);
        this.rules = new HashSet<>();
        this.mTestRuleAlarmStartPebble = new Rule("testRuleAlarmStartPebble");
        this.mTestRuleAlarmDismissPebble = new Rule("testRuleAlarmDismissPebble");
        this.mTestRuleAlarmDonePebble = new Rule("testRuleAlarmDonePebble");
        this.rules.add(mTestRuleAlarmStartPebble);
        this.rules.add(mTestRuleAlarmDismissPebble);
        this.rules.add(mTestRuleAlarmDonePebble);
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
        return ruleSystemBinder;
    }

    public class RuleSystemBinder extends Binder {
        public RuleSystemService getService(){
            return RuleSystemService.this;
        }
    }

    public DeviceManager getDeviceManager() {
        return mDeviceManager;
    }

    public Set<Rule> getRules(){
        return this.rules;
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
        mTestRuleAlarmDismissPebble.setActive(true);
        // Alarm done! -> Pebble Screen Clean!
        mTestRuleAlarmDonePebble.addEvent(mDeviceManager.getAndroidPhone().getAlarmDone());
        mTestRuleAlarmDonePebble.addAction(mDeviceManager.getPebble().getScreenClean());
        mTestRuleAlarmDonePebble.setActive(true);
    }

}
