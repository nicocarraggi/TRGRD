package com.example.nicolascarraggi.trgrd.rulesys;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.nicolascarraggi.trgrd.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RuleSystemService extends Service {

    // TODO add rule ID system
    private int newId = 3;
    // TODO get and write rules from and to database?

    private final IBinder ruleSystemBinder = new RuleSystemBinder();

    private DeviceManager mDeviceManager;

    private Rule mTestRuleAlarmStartPebble,mTestRuleAlarmDismissPebble,mTestRuleAlarmDonePebble;

    private Location mTestLocationVub, mTestLocationStadium;

    private HashMap<Integer,Rule> rules;
    private HashMap<String,Location> locations;

    public RuleSystemService() {
        this.mDeviceManager = new DeviceManager(this);
        this.rules = new HashMap<>();
        this.locations = new HashMap<>();
        this.mTestRuleAlarmStartPebble = new Rule(0,"Phone alarm alert on Pebble");
        this.mTestRuleAlarmDismissPebble = new Rule(1,"Dismiss phone alarm on Pebble");
        this.mTestRuleAlarmDonePebble = new Rule(2,"Phone alarm done to Pebble");
        this.rules.put(mTestRuleAlarmStartPebble.getId(),mTestRuleAlarmStartPebble);
        this.rules.put(mTestRuleAlarmDismissPebble.getId(),mTestRuleAlarmDismissPebble);
        this.rules.put(mTestRuleAlarmDonePebble.getId(),mTestRuleAlarmDonePebble);
        this.mTestLocationVub = new Location("0", "Vrije Universiteit Brussel","Pleinlaan 9", R.drawable.ic_school_black_24dp, new LatLng(50.8218985, 4.3933034));
        this.mTestLocationStadium = new Location("1", "Stadium","Sippelberglaan 1", R.drawable.ic_directions_run_black_24dp, new LatLng(50.8597101, 4.3218491));
        this.locations.put(mTestLocationVub.getId(),mTestLocationVub);
        this.locations.put(mTestLocationStadium.getId(),mTestLocationStadium);
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
        return new HashSet(rules.values());
    }

    public Rule getRule(int id){
        return rules.get(id);
    }

    public void addRule(Rule rule){
        this.rules.put(rule.getId(),rule);
    }

    public HashSet<Location> getLocations(){
        return new HashSet(locations.values());
    }

    public Location getLocation(String id){
        return locations.get(id);
    }

    public void addLocation(Location location){
        this.locations.put(location.getId(),location);
    }

    public int getNewId(){
        return this.newId++;
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
