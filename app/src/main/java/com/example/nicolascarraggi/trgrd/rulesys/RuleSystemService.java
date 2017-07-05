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
    private int newId = 6;
    // TODO get and write rules from and to database?

    private final IBinder ruleSystemBinder = new RuleSystemBinder();

    private DeviceManager mDeviceManager;
    private RuleEngine mRuleEngine;

    // FOR TESTING
    private Rule mTestRuleAlarmStartPebble,mTestRuleAlarmDismissPebble,mTestRuleAlarmDonePebble,
                mTestRulePebbleSportMode, mTestRulePebbleTimeMode, mTestRulePebbleAdd1Left, mTestRulePebbleAdd1Right;
    private Location mTestLocationVub, mTestLocationStadium, mTestLocationHome;

    private HashMap<Integer,Rule> rules;
    private HashMap<Integer,Rule> exampleRules;
    private HashMap<Integer,RuleTemplate> ruleTemplates;
    private HashMap<Integer,RuleTemplate> ruleTemplateInstances;
    private HashMap<String,Location> locations;

    public RuleSystemService() {
        super();
        this.mRuleEngine = new RuleEngine();
        this.rules = new HashMap<>();
        this.exampleRules = new HashMap<>();
        this.ruleTemplates = new HashMap<>();
        this.ruleTemplateInstances = new HashMap<>();
        this.locations = new HashMap<>();
        testCreateLocations();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mDeviceManager = new DeviceManager(this);
        mDeviceManager.startDevices();
        testCreateRulesPebbleAlarm();
        testCreateRulesPebbleWatchmodes();
        testRuleTemplates();
        testExampleRules();
    }

    @Override
    public void onDestroy() {
        Log.d("TRGRD","RuleSystemService onDestroy()");
        mTestRuleAlarmStartPebble.setActive(false);
        mTestRuleAlarmDismissPebble.setActive(false);
        mTestRuleAlarmDonePebble.setActive(false);
        // TODO deactivate all rules?
        mDeviceManager.stopDevices();
        super.onDestroy();
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

    public RuleEngine getRuleEngine() {
        return mRuleEngine;
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

    public Set<Rule> getExampleRules(){
        return new HashSet(exampleRules.values());
    }

    public ExampleRule getExampleRule(int id){
        return (ExampleRule) exampleRules.get(id);
    }

    public void addExampleRule(ExampleRule rule){
        this.exampleRules.put(rule.getId(),rule);
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

    // LOCATIONS TESTS
    private void testCreateLocations(){
        this.mTestLocationVub = new Location("0", "Vrije Universiteit Brussel","Pleinlaan 9", R.drawable.ic_school_black_24dp, new LatLng(50.8218985, 4.3933034));
        this.mTestLocationStadium = new Location("1", "Stadium","Sippelberglaan 1", R.drawable.ic_directions_run_black_24dp, new LatLng(50.8597101, 4.3218491));
        this.mTestLocationHome = new Location("2", "Home","Potaardestraat 161", R.drawable.ic_home_black_24dp, new LatLng(50.862164, 4.282571));
        this.locations.put(mTestLocationVub.getId(),mTestLocationVub);
        this.locations.put(mTestLocationStadium.getId(),mTestLocationStadium);
        this.locations.put(mTestLocationHome.getId(),mTestLocationHome);
    }

    // RULE TESTS
    private void testCreateRulesPebbleAlarm(){
        // create rules
        this.mTestRuleAlarmStartPebble = new Rule(getNewId(),"Display Phone alarm alert on Pebble watch");
        this.mTestRuleAlarmDismissPebble = new Rule(getNewId(),"Dismiss Phone alarm with a Pebble watch button press");
        this.mTestRuleAlarmDonePebble = new Rule(getNewId(),"Clear Pebble watch screen when Phone alarm is done");
        this.rules.put(mTestRuleAlarmStartPebble.getId(),mTestRuleAlarmStartPebble);
        this.rules.put(mTestRuleAlarmDismissPebble.getId(),mTestRuleAlarmDismissPebble);
        this.rules.put(mTestRuleAlarmDonePebble.getId(),mTestRuleAlarmDonePebble);
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

    private void testCreateRulesPebbleWatchmodes(){
        // create rules
        this.mTestRulePebbleSportMode = new Rule(getNewId(),"Set Pebble watch in SPORT mode with a Pebble watch button press");
        this.mTestRulePebbleTimeMode = new Rule(getNewId(),"Set Pebble watch in TIME mode with a Pebble watch button press"); // by shaking a Pebble watch"); //
        this.mTestRulePebbleAdd1Left = new Rule(getNewId(),"Add 1 to Pebble left score with a Pebble watch button press");
        this.mTestRulePebbleAdd1Right = new Rule(getNewId(),"Add 1 to Pebble right score with a Pebble watch button press");
        this.rules.put(mTestRulePebbleSportMode.getId(),mTestRulePebbleSportMode);
        this.rules.put(mTestRulePebbleTimeMode.getId(),mTestRulePebbleTimeMode);
        this.rules.put(mTestRulePebbleAdd1Left.getId(), mTestRulePebbleAdd1Left);
        this.rules.put(mTestRulePebbleAdd1Right.getId(), mTestRulePebbleAdd1Right);
        // Pebble select button WHILE time mode -> Pebble sport mode
        mTestRulePebbleSportMode.addEvent(mDeviceManager.getPebble().getBtnSelect());
        mTestRulePebbleSportMode.addState(mDeviceManager.getPebble().getWatchModeTime());
        mTestRulePebbleSportMode.addAction(mDeviceManager.getPebble().getScreenSport());
        mTestRulePebbleSportMode.setActive(true);
        // Pebble shake WHILE sport mode -> Pebble time mode
        mTestRulePebbleTimeMode.addEvent(mDeviceManager.getPebble().getBtnSelect());
        //mTestRulePebbleTimeMode.addEvent(mDeviceManager.getPebble().getShake());
        mTestRulePebbleTimeMode.addState(mDeviceManager.getPebble().getWatchModeSport());
        mTestRulePebbleTimeMode.addAction(mDeviceManager.getPebble().getScreenTime());
        mTestRulePebbleTimeMode.setActive(true);
        // Pebble shake -> Pebble time mode
        //mTestRulePebbleTimeMode.addEvent(mDeviceManager.getPebble().getShake());
        //mTestRulePebbleTimeMode.addAction(mDeviceManager.getPebble().getScreenTime());
        //mTestRulePebbleTimeMode.setActive(true);
        // Pebble up button -> Pebble left score + 1
        mTestRulePebbleAdd1Left.addEvent(mDeviceManager.getPebble().getBtnUp());
        mTestRulePebbleAdd1Left.addState(mDeviceManager.getPebble().getWatchModeSport());
        mTestRulePebbleAdd1Left.addAction(mDeviceManager.getPebble().getAddScoreOneLeft());
        mTestRulePebbleAdd1Left.setActive(true);
        // Pebble down button -> Pebble right score + 1
        mTestRulePebbleAdd1Right.addEvent(mDeviceManager.getPebble().getBtnDown());
        mTestRulePebbleAdd1Right.addState(mDeviceManager.getPebble().getWatchModeSport());
        mTestRulePebbleAdd1Right.addAction(mDeviceManager.getPebble().getAddScoreOneRight());
        mTestRulePebbleAdd1Right.setActive(true);
    }

    // RULE TEMPLATE TESTS
    private void testRuleTemplates(){
        // When alarm starts, display alarm
        RuleTemplate rtAlarmStart = new RuleTemplate(getNewId(),"Display a starting alarm on a device");
        rtAlarmStart.addTriggerType(mDeviceManager.evAlarmAlert);
        rtAlarmStart.addActionType(mDeviceManager.acAlarmDisplay);
        ruleTemplates.put(rtAlarmStart.getId(),rtAlarmStart);
        // When a button is pressed, notify
        RuleTemplate rtNotifyButton = new RuleTemplate(getNewId(),"Notify on a device that a button is pressed");
        rtNotifyButton.addTriggerType(mDeviceManager.evButtonPress);
        rtNotifyButton.addActionType(mDeviceManager.acNotify);
        ruleTemplates.put(rtNotifyButton.getId(),rtNotifyButton);
        // When at a location and button is pressed, start coffee
        RuleTemplate rtCoffee = new RuleTemplate(getNewId(),"Start making coffee at a location with a button press");
        rtCoffee.addTriggerType(mDeviceManager.evButtonPress);
        rtCoffee.addTriggerType(mDeviceManager.stLocationCurrentlyAt);
        rtCoffee.addActionType(mDeviceManager.acStartCoffee);
        ruleTemplates.put(rtCoffee.getId(),rtCoffee);
        // When button is pressed, while an alarm is going, dismiss it!
        RuleTemplate rtDismissAlarmButton = new RuleTemplate(getNewId(),"Dismiss an alarm on a device with a button press");
        rtDismissAlarmButton.addTriggerType(mDeviceManager.evButtonPress);
        rtDismissAlarmButton.addTriggerType(mDeviceManager.stAlarmGoing);
        rtDismissAlarmButton.addActionType(mDeviceManager.acAlarmDismiss);
        ruleTemplates.put(rtDismissAlarmButton.getId(),rtDismissAlarmButton);
        // Watch mode when at a location
        RuleTemplate rtWatchModeWhileLocation = new RuleTemplate(getNewId(),"Set a watch mode when arriving at a location");
        rtWatchModeWhileLocation.addTriggerType(mDeviceManager.evLocationArrivingAt);
        rtWatchModeWhileLocation.addActionType(mDeviceManager.acWatchMode);
        ruleTemplates.put(rtWatchModeWhileLocation.getId(),rtWatchModeWhileLocation);
        // Set Watch mode to another mode if a certain watch mode is actually used.
        RuleTemplate rwWatchModeSwitch = new RuleTemplate(getNewId(),"Switch to other watch mode with a button press");
        rwWatchModeSwitch.addTriggerType(mDeviceManager.evButtonPress);
        rwWatchModeSwitch.addTriggerType(mDeviceManager.stWatchMode);
        rwWatchModeSwitch.addActionType(mDeviceManager.acWatchMode);
        ruleTemplates.put(rwWatchModeSwitch.getId(),rwWatchModeSwitch);
        // Add or Subtract score with a button press
        RuleTemplate rwScoreButton = new RuleTemplate(getNewId(),"Add or subtract a score with a button press while watch in SPORT mode");
        rwScoreButton.addTriggerType(mDeviceManager.evButtonPress);
        rwScoreButton.addTriggerType(mDeviceManager.stWatchMode);
        rwScoreButton.addActionType(mDeviceManager.acScoreAdjust);
        ruleTemplates.put(rwScoreButton.getId(),rwScoreButton);
    }

    private void testExampleRules(){
        ExampleRule erCoffeeLocation = new ExampleRule(getNewId(),"Start coffee with Pebble watch button when at a location");
        erCoffeeLocation.addEvent(mDeviceManager.getPebble().getBtn());
        erCoffeeLocation.addState(mDeviceManager.getGeofences().getLocationCurrentlyAt());
        erCoffeeLocation.addAction(mDeviceManager.getHomeCoffeeMachine().getStartCoffee());
        exampleRules.put(erCoffeeLocation.getId(),erCoffeeLocation);
        ExampleRule erDisplayAlarm = new ExampleRule(getNewId(),"Display Phone alarm alert on Pebble watch");
        erDisplayAlarm.addEvent(mDeviceManager.getAndroidPhone().getAlarmStart());
        erDisplayAlarm.addAction(mDeviceManager.getPebble().getScreenAlarm());
        exampleRules.put(erDisplayAlarm.getId(),erDisplayAlarm);
        ExampleRule erNotifyButtonPressed = new ExampleRule(getNewId(),"Notify Phone when Pebble watch button is pressed");
        erNotifyButtonPressed.addEvent(mDeviceManager.getPebble().getBtn());
        erNotifyButtonPressed.addAction(mDeviceManager.getAndroidPhone().getNotify());
        exampleRules.put(erNotifyButtonPressed.getId(),erNotifyButtonPressed);
        //
        ExampleRule erDismissPhoneAlarmWithPebbleButton = new ExampleRule(getNewId(),"Dismiss Phone alarm with a Pebble watch button press");
        erDismissPhoneAlarmWithPebbleButton.addEvent(mDeviceManager.getPebble().getBtn());
        erDismissPhoneAlarmWithPebbleButton.addState(mDeviceManager.getAndroidPhone().getAlarmGoing());
        erDismissPhoneAlarmWithPebbleButton.addAction(mDeviceManager.getAndroidPhone().getAcAlarmDismiss());
        exampleRules.put(erDismissPhoneAlarmWithPebbleButton.getId(),erDismissPhoneAlarmWithPebbleButton);
        //
        ExampleRule erWatchmodeSportAtLocation = new ExampleRule(getNewId(),"Set Pebble watch in SPORT mode when arriving at a location");
        erWatchmodeSportAtLocation.addEvent(mDeviceManager.getGeofences().getLocationCurrentlyAt());
        erWatchmodeSportAtLocation.addAction(mDeviceManager.getPebble().getScreenSport());
        exampleRules.put(erWatchmodeSportAtLocation.getId(),erWatchmodeSportAtLocation);
        //
        ExampleRule erWatchmodeTimeAtLocation = new ExampleRule(getNewId(),"Set Pebble watch in TIME mode when arriving at a location");
        erWatchmodeTimeAtLocation.addEvent(mDeviceManager.getGeofences().getLocationCurrentlyAt());
        erWatchmodeTimeAtLocation.addAction(mDeviceManager.getPebble().getScreenTime());
        exampleRules.put(erWatchmodeTimeAtLocation.getId(),erWatchmodeTimeAtLocation);
        //
        ExampleRule erWatchmodeSportBtn = new ExampleRule(getNewId(),"Set Pebble watch in SPORT mode with a Pebble watch button press while Pebble watch in TIME mode");
        erWatchmodeSportBtn.addEvent(mDeviceManager.getPebble().getBtn());
        erWatchmodeSportBtn.addState(mDeviceManager.getPebble().getWatchModeTime());
        erWatchmodeSportBtn.addAction(mDeviceManager.getPebble().getScreenSport());
        exampleRules.put(erWatchmodeSportBtn.getId(),erWatchmodeSportBtn);
        //
        ExampleRule erWatchmodeTimeAtBtn = new ExampleRule(getNewId(),"Set Pebble watch in TIME mode with a Pebble watch button press while Pebble watch in SPORT mode");
        erWatchmodeTimeAtBtn.addEvent(mDeviceManager.getPebble().getBtn());
        erWatchmodeTimeAtBtn.addState(mDeviceManager.getPebble().getWatchModeSport());
        erWatchmodeTimeAtBtn.addAction(mDeviceManager.getPebble().getScreenSport());
        exampleRules.put(erWatchmodeTimeAtBtn.getId(),erWatchmodeTimeAtBtn);
        //
        ExampleRule erScoreButtonAddLeft = new ExampleRule(getNewId(),"Add a LEFT score with a button while Pebble watch in SPORT mode");
        erScoreButtonAddLeft.addEvent(mDeviceManager.getPebble().getBtn());
        erScoreButtonAddLeft.addState(mDeviceManager.getPebble().getWatchModeSport());
        erScoreButtonAddLeft.addAction(mDeviceManager.getPebble().getAddScoreXLeft());
        exampleRules.put(erScoreButtonAddLeft.getId(),erScoreButtonAddLeft);
        //
        ExampleRule erScoreButtonAddRight = new ExampleRule(getNewId(),"Add a RIGHT score with a Pebble watch button press while Pebble watch in SPORT mode");
        erScoreButtonAddRight.addEvent(mDeviceManager.getPebble().getBtn());
        erScoreButtonAddRight.addState(mDeviceManager.getPebble().getWatchModeSport());
        erScoreButtonAddRight.addAction(mDeviceManager.getPebble().getAddScoreXRight());
        exampleRules.put(erScoreButtonAddRight.getId(),erScoreButtonAddRight);
        //
        ExampleRule erScoreButtonSubtractLeft = new ExampleRule(getNewId(),"Subtract a LEFT score with a button while Pebble watch in SPORT mode");
        erScoreButtonSubtractLeft.addEvent(mDeviceManager.getPebble().getBtn());
        erScoreButtonSubtractLeft.addState(mDeviceManager.getPebble().getWatchModeSport());
        erScoreButtonSubtractLeft.addAction(mDeviceManager.getPebble().getSubtractScoreXLeft());
        exampleRules.put(erScoreButtonSubtractLeft.getId(),erScoreButtonSubtractLeft);
        //
        ExampleRule erScoreButtonSubtractRight = new ExampleRule(getNewId(),"Subtract a RIGHT score with a Pebble watch button press while Pebble watch in SPORT mode");
        erScoreButtonSubtractRight.addEvent(mDeviceManager.getPebble().getBtn());
        erScoreButtonSubtractRight.addState(mDeviceManager.getPebble().getWatchModeSport());
        erScoreButtonSubtractRight.addAction(mDeviceManager.getPebble().getSubtractScoreXRight());
        exampleRules.put(erScoreButtonSubtractRight.getId(),erScoreButtonSubtractRight);
    }

}
