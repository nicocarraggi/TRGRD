package com.example.nicolascarraggi.trgrd.rulesys.devices;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.ActionType;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 17/05/17.
 */

public class VubCoffeeMachine extends Device {

    private Action mAcStartCoffee;

    public VubCoffeeMachine(RuleSystemService ruleSystemService, DeviceManager deviceManager) {
        super(ruleSystemService.getNewId(), "VUB coffee machine", "Unknown", "Unknown", R.drawable.ic_local_cafe_black_24dp, ruleSystemService, deviceManager);
        this.actionTypes.put(deviceManager.getAcStartCoffee().getId(),deviceManager.getAcStartCoffee());
        this.mAcStartCoffee = new Action(deviceManager.getNewId(),"VUB coffee machine start", R.drawable.ic_play_arrow_black_24dp, this, deviceManager.getAcStartCoffee(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                acStartCoffee();
                return null;
            }
        });
        this.actions.put(mAcStartCoffee.getId(),mAcStartCoffee);
    }

    // Action getter

    public Action getStartCoffee() {
        return mAcStartCoffee;
    }

    private void acStartCoffee() {
        // TODO send HTTP request
        deviceManager.getAndroidPhone().acNotify("VUB Coffee","Your coffee is being made!", NotificationAction.NotificationActionType.NONE);
    }

    @Override
    public void start(){
        this.started = true;
    }

    @Override
    public void stop(){
        this.started = false;
    }
}
