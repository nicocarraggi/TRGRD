package com.example.nicolascarraggi.trgrd;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

/**
 * Created by nicolascarraggi on 19/04/17.
 */

public class RuleSystemBindingActivity extends AppCompatActivity {

    protected RuleSystemService ruleSystemService;
    protected boolean isServiceBound = false;

    @Override
    protected void onResume() {
        super.onResume();
        bindWithService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isServiceBound) {
            unbindService(mConnection);
            this.isServiceBound = false;
        }
    }

    // Override this if something needs to happen!!!
    protected void onBound(){}

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RuleSystemService.RuleSystemBinder b = (RuleSystemService.RuleSystemBinder) iBinder;
            RuleSystemBindingActivity.this.ruleSystemService = b.getService();
            //Toast.makeText(RuleDetailsActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            RuleSystemBindingActivity.this.isServiceBound = true;
            RuleSystemBindingActivity.this.onBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            RuleSystemBindingActivity.this.isServiceBound = false;
            RuleSystemBindingActivity.this.ruleSystemService = null;
            Log.d("TRGRD","RuleSystemBindingActivity: onServiceDisconnect");
        }
    };

    private void bindWithService(){
        // BIND this activity to the service to communicate with it!
        Intent intent= new Intent(this, RuleSystemService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

}
