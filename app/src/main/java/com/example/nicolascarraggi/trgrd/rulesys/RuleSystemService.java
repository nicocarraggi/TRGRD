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

    private Location mTestLocationVub, mTestLocationStadium, mTestLocationHome;

    private HashMap<Integer,Rule> rules;
    private HashMap<Integer,RuleTemplate> ruleTemplates;
    private HashMap<Integer,RuleTemplate> ruleTemplateInstances;
    private HashMap<String,Location> locations;

    public RuleSystemService() {
        super();
        this.rules = new HashMap<>();
        this.ruleTemplates = new HashMap<>();
        this.ruleTemplateInstances = new HashMap<>();
        this.locations = new HashMap<>();
        this.mTestRuleAlarmStartPebble = new Rule(0,"Phone alarm alert on Pebble");
        this.mTestRuleAlarmDismissPebble = new Rule(1,"Dismiss phone alarm on Pebble");
        this.mTestRuleAlarmDonePebble = new Rule(2,"Phone alarm done to Pebble");
        this.rules.put(mTestRuleAlarmStartPebble.getId(),mTestRuleAlarmStartPebble);
        this.rules.put(mTestRuleAlarmDismissPebble.getId(),mTestRuleAlarmDismissPebble);
        this.rules.put(mTestRuleAlarmDonePebble.getId(),mTestRuleAlarmDonePebble);
        this.mTestLocationVub = new Location("0", "Vrije Universiteit Brussel","Pleinlaan 9", R.drawable.ic_school_black_24dp, new LatLng(50.8218985, 4.3933034));
        this.mTestLocationStadium = new Location("1", "Stadium","Sippelberglaan 1", R.drawable.ic_directions_run_black_24dp, new LatLng(50.8597101, 4.3218491));
        this.mTestLocationHome = new Location("2", "Home","Potaardestraat 161", R.drawable.ic_home_black_24dp, new LatLng(50.862164, 4.282571));
        this.locations.put(mTestLocationVub.getId(),mTestLocationVub);
        this.locations.put(mTestLocationStadium.getId(),mTestLocationStadium);
        this.locations.put(mTestLocationHome.getId(),mTestLocationHome);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mDeviceManager = new DeviceManager(this);
        mDeviceManager.startDevices();
        testAlarmPebble();
        testRuleTemplates();
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

    public Set<RuleTemplate> getRuleTemplates(){
        return new HashSet(ruleTemplates.values());
    }

    public RuleTemplate getRuleTemplate(int id){
        return ruleTemplates.get(id);
    }

    public void addRuleTemplate(RuleTemplate ruleTemplate){
        this.ruleTemplates.put(ruleTemplate.getId(),ruleTemplate);
    }

    public Set<RuleTemplate> getRuleTemplateInstances(){
        return new HashSet(ruleTemplateInstances.values());
    }

    public RuleTemplate getRuleTemplateInstance(int id){
        return ruleTemplateInstances.get(id);
    }

    public void addRuleTemplateInstance(RuleTemplate ruleTemplate){
        this.ruleTemplateInstances.put(ruleTemplate.getId(),ruleTemplate);
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

    // RULE TEMPLATE TESTS
    private void testRuleTemplates(){
        // When alarm starts, display alarm
        RuleTemplate rtAlarmStart = new RuleTemplate(getNewId(),"Display starting alarm");
        rtAlarmStart.addTriggerType(mDeviceManager.evAlarmAlert);
        rtAlarmStart.addActionType(mDeviceManager.acAlarmDisplay);
        ruleTemplates.put(rtAlarmStart.getId(),rtAlarmStart);
        // When a button is pressed, notify
        RuleTemplate rtNotifyButton = new RuleTemplate(getNewId(),"Notify button pressed");
        rtNotifyButton.addTriggerType(mDeviceManager.evButtonPress);
        rtNotifyButton.addActionType(mDeviceManager.acNotify);
        ruleTemplates.put(rtNotifyButton.getId(),rtNotifyButton);
        // When at a location and button is pressed,
        RuleTemplate rtCoffee = new RuleTemplate(getNewId(),"Coffee at a location");
        rtCoffee.addTriggerType(mDeviceManager.evButtonPress);
        rtCoffee.addTriggerType(mDeviceManager.stLocationCurrentlyAt);
        rtCoffee.addActionType(mDeviceManager.acStartCoffee);
        ruleTemplates.put(rtCoffee.getId(),rtCoffee);
    }

}
