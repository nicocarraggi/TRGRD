package com.example.nicolascarraggi.trgrd.rulesys.devices;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

import java.util.concurrent.Callable;

/**
 * Created by nicolascarraggi on 17/05/17.
 */

public class HomeTv extends Device {

    private Action mAcTurnOnTv, mAcTurnOffTv;

    public HomeTv(RuleSystemService ruleSystemService, DeviceManager deviceManager) {
        super(ruleSystemService.getNewId(), "Home TV", "Unknown", "Unknown", R.drawable.ic_tv_black_24dp, ruleSystemService, deviceManager);
        this.actionTypes.put(deviceManager.acTurnOnOffTv.getId(),deviceManager.acTurnOnOffTv);
        this.mAcTurnOnTv = new Action(deviceManager.getNewId(),"Home TV turn on", R.drawable.ic_play_arrow_black_24dp, this, deviceManager.acTurnOnOffTv, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acTurnOnTv();
                return null;
            }
        });
        this.actions.put(mAcTurnOnTv.getId(),mAcTurnOnTv);
        this.mAcTurnOffTv = new Action(deviceManager.getNewId(),"Home TV turn off", R.drawable.ic_stop_black_24dp, this, deviceManager.acTurnOnOffTv, new Callable<String>() {
            @Override
            public String call() throws Exception {
                acTurnOnTv();
                return null;
            }
        });
        this.actions.put(mAcTurnOffTv.getId(),mAcTurnOffTv);
    }

    // Action getter

    public Action getTurnOnTv() {
        return mAcTurnOnTv;
    }

    private void acTurnOnTv() {
        // TODO send HTTP request
    }

    public Action getTurnOffTv() {
        return mAcTurnOffTv;
    }

    private void acTurnOffTv() {
        // TODO send HTTP request
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
