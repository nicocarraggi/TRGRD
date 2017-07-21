package com.example.nicolascarraggi.trgrd.rulesys;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.devices.ScoreDevice;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RuleSystemService extends Service {

    // TODO add database for rule system data
    private int newId = 0;

    private final IBinder ruleSystemBinder = new RuleSystemBinder();

    private RuleManager mRuleManager;
    private DeviceManager mDeviceManager;
    private LocationManager mLocationManager;

    // FOR TESTING
    private Rule mTestRuleAlarmStartPebble,mTestRuleAlarmDismissPebble,mTestRuleAlarmDonePebble,
                mTestRulePebbleSportMode, mTestRulePebbleTimeMode, mTestRulePebbleAdd1Left,
                mTestRulePebbleAdd1Right, mTestRulePebbleRest, mTestRulePebblePhysical,
                mTestRulePebbleCoffee;
    private Location mTestLocationVub, mTestLocationStadium, mTestLocationHome;

    public RuleSystemService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mRuleManager = new RuleManager(this);
        this.mDeviceManager = new DeviceManager(this);
        this.mLocationManager = new LocationManager(this);
        testCreateLocations();
        mDeviceManager.startDevices();
        testCreateRulesPebbleAlarm();
        testCreateRulesPebbleWatchmodes();
        testCreateRulesPebbleActivity();
        testCreateRulesPebbleWakeCoffee();
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

    public RuleManager getRuleManager() {
        return mRuleManager;
    }

    public DeviceManager getDeviceManager() {
        return mDeviceManager;
    }

    public LocationManager getLocationManager() {
        return mLocationManager;
    }

    public int getNewId(){
        return this.newId++;
    }


    // ------------------------------------
    //
    //   DUMMY DATA FOR TESTING PROTOTYPE
    //
    // ------------------------------------

    // LOCATIONS TESTS
    private void testCreateLocations(){
        this.mTestLocationVub = new Location(Integer.toString(getNewId()), "Vrije Universiteit Brussel","Pleinlaan 9", R.drawable.ic_school_black_24dp, new LatLng(50.8218985, 4.3933034));
        this.mTestLocationStadium = new Location(Integer.toString(getNewId()), "Stadium","Sippelberglaan 1", R.drawable.ic_directions_run_black_24dp, new LatLng(50.8597101, 4.3218491));
        this.mTestLocationHome = new Location(Integer.toString(getNewId()), "Home","Potaardestraat 161", R.drawable.ic_home_black_24dp, new LatLng(50.862164, 4.282571));
        mLocationManager.addLocation(mTestLocationVub);
        mLocationManager.addLocation(mTestLocationStadium);
        mLocationManager.addLocation(mTestLocationHome);
    }

    // RULE TESTS
    private void testCreateRulesPebbleAlarm(){
        // create rules
        this.mTestRuleAlarmStartPebble = new Rule(getNewId(),"Display Phone alarm alert on Pebble watch");
        this.mTestRuleAlarmDismissPebble = new Rule(getNewId(),"Dismiss Phone alarm with a Pebble watch button press");
        this.mTestRuleAlarmDonePebble = new Rule(getNewId(),"Clear Pebble watch screen when Phone alarm is done");
        mRuleManager.addRule(mTestRuleAlarmStartPebble);
        mRuleManager.addRule(mTestRuleAlarmDismissPebble);
        mRuleManager.addRule(mTestRuleAlarmDonePebble);
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
        mRuleManager.addRule(mTestRulePebbleSportMode);
        mRuleManager.addRule(mTestRulePebbleTimeMode);
        mRuleManager.addRule(mTestRulePebbleAdd1Left);
        mRuleManager.addRule(mTestRulePebbleAdd1Right);
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
        ScoreDevice scoreDevice = (ScoreDevice) mDeviceManager.getPebble();
        ScoreValueAction addXLeft = (ScoreValueAction) mDeviceManager.getPebble().getAddScoreXLeft();
        Action add1Left = scoreDevice.getScoreValueAction(addXLeft,1);
        mTestRulePebbleAdd1Left.addAction(add1Left);
        mTestRulePebbleAdd1Left.setActive(true);
        // Pebble down button -> Pebble right score + 1
        mTestRulePebbleAdd1Right.addEvent(mDeviceManager.getPebble().getBtnDown());
        mTestRulePebbleAdd1Right.addState(mDeviceManager.getPebble().getWatchModeSport());
        ScoreValueAction addXRight = (ScoreValueAction) mDeviceManager.getPebble().getAddScoreXRight();
        Action add1Right = scoreDevice.getScoreValueAction(addXRight,1);
        mTestRulePebbleAdd1Right.addAction(add1Right);
        mTestRulePebbleAdd1Right.setActive(true);
    }

    private void testCreateRulesPebbleActivity(){
        // Pebble REST activity event -> Pebble TIME mode
        this.mTestRulePebbleRest = new Rule(getNewId(),"Switch to Pebble watch TIME mode when Pebble detects REST activity.");
        mTestRulePebbleRest.addEvent(mDeviceManager.getPebble().getRest());
        mTestRulePebbleRest.addAction(mDeviceManager.getPebble().getScreenTime());
        mTestRulePebbleRest.setActive(true);
        // Pebble PHYSICAL activity event -> Pebble SPORT mode
        this.mTestRulePebblePhysical = new Rule(getNewId(),"Switch to Pebble watch SPORT mode when Pebble detects PHYSICAL activity.");
        mTestRulePebblePhysical.addEvent(mDeviceManager.getPebble().getPhysical());
        mTestRulePebblePhysical.addAction(mDeviceManager.getPebble().getScreenSport());
        mTestRulePebblePhysical.setActive(true);
        mRuleManager.addRule(mTestRulePebbleRest);
        mRuleManager.addRule(mTestRulePebblePhysical);
    }

    private void testCreateRulesPebbleWakeCoffee(){
        // Pebble Wake Up detect event -> Home coffee start
        this.mTestRulePebbleCoffee = new Rule(getNewId(),"Start home coffee machine when I wake up!");
        mTestRulePebbleCoffee.addEvent(mDeviceManager.getPebble().getWakesUp());
        mTestRulePebbleCoffee.addAction(mDeviceManager.getHomeCoffeeMachine().getStartCoffee());
        mTestRulePebbleCoffee.setActive(true);
        mRuleManager.addRule(mTestRulePebbleCoffee);
    }

    // RULE TEMPLATE TESTS
    private void testRuleTemplates(){
        // When alarm starts, display alarm
        RuleTemplate rtAlarmStart = new RuleTemplate(getNewId(),"Display a starting alarm on a device");
        rtAlarmStart.addTriggerType(mDeviceManager.evAlarmAlert);
        rtAlarmStart.addActionType(mDeviceManager.acAlarmDisplay);
        mRuleManager.addRuleTemplate(rtAlarmStart);
        // When a button is pressed, notify
        RuleTemplate rtNotifyButton = new RuleTemplate(getNewId(),"Notify on a device that a button is pressed");
        rtNotifyButton.addTriggerType(mDeviceManager.evButtonPress);
        rtNotifyButton.addActionType(mDeviceManager.acNotify);
        mRuleManager.addRuleTemplate(rtNotifyButton);
        // When at a location and button is pressed, start coffee
        RuleTemplate rtCoffee = new RuleTemplate(getNewId(),"Start making coffee at a location with a button press");
        rtCoffee.addTriggerType(mDeviceManager.evButtonPress);
        rtCoffee.addTriggerType(mDeviceManager.stLocationCurrentlyAt);
        rtCoffee.addActionType(mDeviceManager.acStartCoffee);
        mRuleManager.addRuleTemplate(rtCoffee);
        // When button is pressed, while an alarm is going, dismiss it!
        RuleTemplate rtDismissAlarmButton = new RuleTemplate(getNewId(),"Dismiss an alarm on a device with a button press");
        rtDismissAlarmButton.addTriggerType(mDeviceManager.evButtonPress);
        rtDismissAlarmButton.addTriggerType(mDeviceManager.stAlarmGoing);
        rtDismissAlarmButton.addActionType(mDeviceManager.acAlarmDismiss);
        mRuleManager.addRuleTemplate(rtDismissAlarmButton);
        // Watch mode when at a location
        RuleTemplate rtWatchModeWhileLocation = new RuleTemplate(getNewId(),"Set a watch mode when arriving at a location");
        rtWatchModeWhileLocation.addTriggerType(mDeviceManager.evLocationArrivingAt);
        rtWatchModeWhileLocation.addActionType(mDeviceManager.acWatchMode);
        mRuleManager.addRuleTemplate(rtWatchModeWhileLocation);
        // Set Watch mode to another mode if a certain watch mode is actually used.
        RuleTemplate rwWatchModeSwitch = new RuleTemplate(getNewId(),"Switch to other watch mode with a button press");
        rwWatchModeSwitch.addTriggerType(mDeviceManager.evButtonPress);
        rwWatchModeSwitch.addTriggerType(mDeviceManager.stWatchMode);
        rwWatchModeSwitch.addActionType(mDeviceManager.acWatchMode);
        mRuleManager.addRuleTemplate(rwWatchModeSwitch);
        // Add or Subtract score with a button press
        RuleTemplate rwScoreButton = new RuleTemplate(getNewId(),"Add or subtract a score with a button press while watch in SPORT mode");
        rwScoreButton.addTriggerType(mDeviceManager.evButtonPress);
        rwScoreButton.addTriggerType(mDeviceManager.stWatchMode);
        rwScoreButton.addActionType(mDeviceManager.acScoreAdjust);
        mRuleManager.addRuleTemplate(rwScoreButton);
        // Switch watch mode with activity change
        RuleTemplate rwWatchModeActivitySwitch = new RuleTemplate(getNewId(),"Switch to other watch mode with a change in physical activity");
        rwWatchModeActivitySwitch.addTriggerType(mDeviceManager.evActivityChange);
        rwWatchModeActivitySwitch.addActionType(mDeviceManager.getAcWatchMode());
        mRuleManager.addRuleTemplate(rwWatchModeActivitySwitch);
        // Start coffee with waking up
        RuleTemplate rwCoffeWakeup = new RuleTemplate(getNewId(), "Start making coffee when I wake up");
        rwCoffeWakeup.addTriggerType(mDeviceManager.evWakesUp);
        rwCoffeWakeup.addActionType(mDeviceManager.acStartCoffee);
        mRuleManager.addRuleTemplate(rwCoffeWakeup);
    }

    private void testExampleRules(){
        ExampleRule erCoffeeLocation = new ExampleRule(getNewId(),"Start coffee with Pebble watch button when at a location");
        erCoffeeLocation.addEvent(mDeviceManager.getPebble().getBtn());
        erCoffeeLocation.addState(mDeviceManager.getGeofences().getLocationCurrentlyAt());
        erCoffeeLocation.addAction(mDeviceManager.getHomeCoffeeMachine().getStartCoffee());
        mRuleManager.addExampleRule(erCoffeeLocation);
        ExampleRule erDisplayAlarm = new ExampleRule(getNewId(),"Display Phone alarm alert on Pebble watch");
        erDisplayAlarm.addEvent(mDeviceManager.getAndroidPhone().getAlarmStart());
        erDisplayAlarm.addAction(mDeviceManager.getPebble().getScreenAlarm());
        mRuleManager.addExampleRule(erDisplayAlarm);
        ExampleRule erNotifyButtonPressed = new ExampleRule(getNewId(),"Notify Phone when Pebble watch button is pressed");
        erNotifyButtonPressed.addEvent(mDeviceManager.getPebble().getBtn());
        erNotifyButtonPressed.addAction(mDeviceManager.getAndroidPhone().getNotify());
        mRuleManager.addExampleRule(erNotifyButtonPressed);
        //
        ExampleRule erDismissPhoneAlarmWithPebbleButton = new ExampleRule(getNewId(),"Dismiss Phone alarm with a Pebble watch button press");
        erDismissPhoneAlarmWithPebbleButton.addEvent(mDeviceManager.getPebble().getBtn());
        erDismissPhoneAlarmWithPebbleButton.addState(mDeviceManager.getAndroidPhone().getAlarmGoing());
        erDismissPhoneAlarmWithPebbleButton.addAction(mDeviceManager.getAndroidPhone().getAcAlarmDismiss());
        mRuleManager.addExampleRule(erDismissPhoneAlarmWithPebbleButton);
        //
        ExampleRule erWatchmodeSportAtLocation = new ExampleRule(getNewId(),"Set Pebble watch in SPORT mode when arriving at a location");
        erWatchmodeSportAtLocation.addEvent(mDeviceManager.getGeofences().getLocationArrivingAt());
        erWatchmodeSportAtLocation.addAction(mDeviceManager.getPebble().getScreenSport());
        mRuleManager.addExampleRule(erWatchmodeSportAtLocation);
        //
        ExampleRule erWatchmodeTimeAtLocation = new ExampleRule(getNewId(),"Set Pebble watch in TIME mode when arriving at a location");
        erWatchmodeTimeAtLocation.addEvent(mDeviceManager.getGeofences().getLocationArrivingAt());
        erWatchmodeTimeAtLocation.addAction(mDeviceManager.getPebble().getScreenTime());
        mRuleManager.addExampleRule(erWatchmodeTimeAtLocation);
        //
        ExampleRule erWatchmodeSportBtn = new ExampleRule(getNewId(),"Set Pebble watch in SPORT mode with a Pebble watch button press while Pebble watch in TIME mode");
        erWatchmodeSportBtn.addEvent(mDeviceManager.getPebble().getBtn());
        erWatchmodeSportBtn.addState(mDeviceManager.getPebble().getWatchModeTime());
        erWatchmodeSportBtn.addAction(mDeviceManager.getPebble().getScreenSport());
        mRuleManager.addExampleRule(erWatchmodeSportBtn);
        //
        ExampleRule erWatchmodeTimeAtBtn = new ExampleRule(getNewId(),"Set Pebble watch in TIME mode with a Pebble watch button press while Pebble watch in SPORT mode");
        erWatchmodeTimeAtBtn.addEvent(mDeviceManager.getPebble().getBtn());
        erWatchmodeTimeAtBtn.addState(mDeviceManager.getPebble().getWatchModeSport());
        erWatchmodeTimeAtBtn.addAction(mDeviceManager.getPebble().getScreenSport());
        mRuleManager.addExampleRule(erWatchmodeTimeAtBtn);
        //
        ExampleRule erScoreButtonAddLeft = new ExampleRule(getNewId(),"Add a LEFT score with a button while Pebble watch in SPORT mode");
        erScoreButtonAddLeft.addEvent(mDeviceManager.getPebble().getBtn());
        erScoreButtonAddLeft.addState(mDeviceManager.getPebble().getWatchModeSport());
        erScoreButtonAddLeft.addAction(mDeviceManager.getPebble().getAddScoreXLeft());
        mRuleManager.addExampleRule(erScoreButtonAddLeft);
        //
        ExampleRule erScoreButtonAddRight = new ExampleRule(getNewId(),"Add a RIGHT score with a Pebble watch button press while Pebble watch in SPORT mode");
        erScoreButtonAddRight.addEvent(mDeviceManager.getPebble().getBtn());
        erScoreButtonAddRight.addState(mDeviceManager.getPebble().getWatchModeSport());
        erScoreButtonAddRight.addAction(mDeviceManager.getPebble().getAddScoreXRight());
        mRuleManager.addExampleRule(erScoreButtonAddRight);
        //
        ExampleRule erScoreButtonSubtractLeft = new ExampleRule(getNewId(),"Subtract a LEFT score with a button while Pebble watch in SPORT mode");
        erScoreButtonSubtractLeft.addEvent(mDeviceManager.getPebble().getBtn());
        erScoreButtonSubtractLeft.addState(mDeviceManager.getPebble().getWatchModeSport());
        erScoreButtonSubtractLeft.addAction(mDeviceManager.getPebble().getSubtractScoreXLeft());
        mRuleManager.addExampleRule(erScoreButtonSubtractLeft);
        //
        ExampleRule erScoreButtonSubtractRight = new ExampleRule(getNewId(),"Subtract a RIGHT score with a Pebble watch button press while Pebble watch in SPORT mode");
        erScoreButtonSubtractRight.addEvent(mDeviceManager.getPebble().getBtn());
        erScoreButtonSubtractRight.addState(mDeviceManager.getPebble().getWatchModeSport());
        erScoreButtonSubtractRight.addAction(mDeviceManager.getPebble().getSubtractScoreXRight());
        mRuleManager.addExampleRule(erScoreButtonSubtractRight);
        // Switch watch mode with activity change
        ExampleRule erRestTime = new ExampleRule(getNewId(),"Set Pebble watch in TIME mode when a Pebble detects REST activity");
        erRestTime.addEvent(mDeviceManager.getPebble().getRest());
        erRestTime.addAction(mDeviceManager.getPebble().getScreenTime());
        mRuleManager.addExampleRule(erRestTime);
        ExampleRule erPhysicalSport = new ExampleRule(getNewId(),"Set Pebble watch in SPORT mode when a Pebble detects PHYSICAL activity");
        erPhysicalSport.addEvent(mDeviceManager.getPebble().getPhysical());
        erPhysicalSport.addAction(mDeviceManager.getPebble().getScreenSport());
        mRuleManager.addExampleRule(erPhysicalSport);
        // Start coffee with waking up
        ExampleRule erCoffeeWakeVub = new ExampleRule(getNewId(),"Start VUB coffee machine when I wake up");
        erCoffeeWakeVub.addEvent(mDeviceManager.getPebble().getWakesUp());
        erCoffeeWakeVub.addAction(mDeviceManager.getVubCoffeeMachine().getStartCoffee());
        mRuleManager.addExampleRule(erCoffeeWakeVub);
        ExampleRule erCoffeeWakeHome = new ExampleRule(getNewId(),"Start Home coffee machine when I wake up");
        erCoffeeWakeHome.addEvent(mDeviceManager.getPebble().getWakesUp());
        erCoffeeWakeHome.addAction(mDeviceManager.getHomeCoffeeMachine().getStartCoffee());
        mRuleManager.addExampleRule(erCoffeeWakeHome);
    }

}
