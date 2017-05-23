package com.example.nicolascarraggi.trgrd.rulesys.devices;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.ActionType;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 17/05/17.
 */

public class CoffeeMachine extends Device {

    private Action mAcStartCoffee;

    public CoffeeMachine(RuleSystemService ruleSystemService, DeviceManager deviceManager, ActionType acStartCoffee) {
        super(ruleSystemService.getNewId(), "Coffee Machine", "Unknown", "Unknown", R.drawable.ic_local_cafe_black_24dp, ruleSystemService, deviceManager);
        this.actionTypes.put(acStartCoffee.getId(),acStartCoffee);
        this.mAcStartCoffee = new Action(deviceManager.getNewId(),"Coffee machine start", R.drawable.ic_play_arrow_black_24dp, this, acStartCoffee, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acStartCoffee();
                return null;
            }
        });
        this.actions.put(mAcStartCoffee.getId(),mAcStartCoffee);
    }

    private void acStartCoffee() {
        // TODO send HTTP request
    }

    public void start(){
        this.started = true;
    }

    public void stop(){
        this.started = false;
    }
}
