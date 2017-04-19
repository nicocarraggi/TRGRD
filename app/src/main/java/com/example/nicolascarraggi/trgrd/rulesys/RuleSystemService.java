package com.example.nicolascarraggi.trgrd.rulesys;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RuleSystemService extends Service {

    // TODO add rule ID system
    // TODO get and write rules from and to database?

    private final IBinder ruleSystemBinder = new RuleSystemBinder();

    private DeviceManager mDeviceManager;

    private Rule mTestRuleAlarmStartPebble,mTestRuleAlarmDismissPebble,mTestRuleAlarmDonePebble;

    private HashMap<Integer,Rule> hmRules;

    public RuleSystemService() {
        this.mDeviceManager = new DeviceManager(this);
        this.hmRules = new HashMap<>();
        this.mTestRuleAlarmStartPebble = new Rule(0,"Phone alarm alert on Pebble");
        this.mTestRuleAlarmDismissPebble = new Rule(1,"Dismiss phone alarm on Pebble");
        this.mTestRuleAlarmDonePebble = new Rule(2,"Phone alarm done to Pebble");
        this.hmRules.put(mTestRuleAlarmStartPebble.getId(),mTestRuleAlarmStartPebble);
        this.hmRules.put(mTestRuleAlarmDismissPebble.getId(),mTestRuleAlarmDismissPebble);
        this.hmRules.put(mTestRuleAlarmDonePebble.getId(),mTestRuleAlarmDonePebble);
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
        return new HashSet(hmRules.values());
    }

    public Rule getRule(int id){
        return hmRules.get(id);
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
