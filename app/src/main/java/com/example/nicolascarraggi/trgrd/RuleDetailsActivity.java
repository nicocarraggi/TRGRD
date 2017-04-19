package com.example.nicolascarraggi.trgrd;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.adapters.ActionsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.EventsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.StatesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

import java.util.Set;

public class RuleDetailsActivity extends AppCompatActivity {

    private RuleSystemService ruleSystemService;
    private boolean isServiceBound = false;
    private int id;
    private Rule rule;
    private EventsAdapter eventsAdapter;
    private StatesAdapter statesAdapter;
    private ActionsAdapter actionsAdapter;
    private RecyclerView.LayoutManager mLayoutManagerEvents, mLayoutManagerStates, mLayoutManagerActions;

    private TextView tvRuleDetailsName;
    private RecyclerView rvRuleDetailsEvents, rvRuleDetailsStates, rvRuleDetailsActions;
    private SwitchCompat switchRuleDetailsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_details);

        Intent intent = getIntent();
        this.id = intent.getIntExtra("id",0);

        tvRuleDetailsName = (TextView) findViewById(R.id.tvRuleDetailsName);
        rvRuleDetailsEvents = (RecyclerView) findViewById(R.id.rvRuleDetailsEvents);
        rvRuleDetailsStates = (RecyclerView) findViewById(R.id.rvRuleDetailsStates);
        rvRuleDetailsActions = (RecyclerView) findViewById(R.id.rvRuleDetailsActions);
        switchRuleDetailsActive = (SwitchCompat) findViewById(R.id.switchRuleDetailsActive);

    }

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

    private void onBound(){
        rule = ruleSystemService.getRule(id);

        tvRuleDetailsName.setText(rule.getName());
        switchRuleDetailsActive.setChecked(rule.isActive());

        eventsAdapter = new EventsAdapter(rule.getEvents());
        statesAdapter = new StatesAdapter(rule.getStates());
        actionsAdapter = new ActionsAdapter(rule.getActions());

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvRuleDetailsEvents.setHasFixedSize(true);
        rvRuleDetailsStates.setHasFixedSize(true);
        rvRuleDetailsActions.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerEvents = new LinearLayoutManager(getApplicationContext());
        mLayoutManagerStates = new LinearLayoutManager(getApplicationContext());
        mLayoutManagerActions = new LinearLayoutManager(getApplicationContext());
        rvRuleDetailsEvents.setLayoutManager(mLayoutManagerEvents);
        rvRuleDetailsStates.setLayoutManager(mLayoutManagerStates);
        rvRuleDetailsActions.setLayoutManager(mLayoutManagerActions);

        rvRuleDetailsEvents.setAdapter(eventsAdapter);
        rvRuleDetailsStates.setAdapter(statesAdapter);
        rvRuleDetailsActions.setAdapter(actionsAdapter);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RuleSystemService.RuleSystemBinder b = (RuleSystemService.RuleSystemBinder) iBinder;
            RuleDetailsActivity.this.ruleSystemService = b.getService();
            //Toast.makeText(RuleDetailsActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            RuleDetailsActivity.this.isServiceBound = true;
            RuleDetailsActivity.this.onBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            RuleDetailsActivity.this.isServiceBound = false;
            RuleDetailsActivity.this.ruleSystemService = null;
            Log.d("TRGRD","RuleDetailsActivity: onServiceDisconnect");
        }
    };

    private void bindWithService(){
        // BIND this activity to the service to communicate with it!
        Intent intent= new Intent(this, RuleSystemService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
}
