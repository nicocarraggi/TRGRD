package com.example.nicolascarraggi.trgrd;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.nicolascarraggi.trgrd.adapters.ActionsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.EventsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyActionOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyEventOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyStateOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.StatesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;
import com.example.nicolascarraggi.trgrd.rulesys.State;

import java.util.Set;

public class RuleDetailsActivity extends RuleSystemBindingActivity implements MyEventOnItemClickListener,
        MyStateOnItemClickListener, MyActionOnItemClickListener {

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

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Rule");
        // Enable the Up button
        //ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.id = intent.getIntExtra("id",0);

        tvRuleDetailsName = (TextView) findViewById(R.id.tvRuleDetailsName);
        rvRuleDetailsEvents = (RecyclerView) findViewById(R.id.rvRuleDetailsEvents);
        rvRuleDetailsStates = (RecyclerView) findViewById(R.id.rvRuleDetailsStates);
        rvRuleDetailsActions = (RecyclerView) findViewById(R.id.rvRuleDetailsActions);
        switchRuleDetailsActive = (SwitchCompat) findViewById(R.id.switchRuleDetailsActive);

    }

    @Override
    protected void onBound(){
        super.onBound();
        rule = ruleSystemService.getRule(id);

        tvRuleDetailsName.setText(rule.getName());
        switchRuleDetailsActive.setChecked(rule.isActive());

        eventsAdapter = new EventsAdapter(this,rule.getEvents());
        statesAdapter = new StatesAdapter(this,rule.getStates());
        actionsAdapter = new ActionsAdapter(this,rule.getActions());

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

    @Override
    public void onItemClick(Event item) {
        Toast.makeText(RuleDetailsActivity.this, "Event Clicked: "+item.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(State item) {
        Toast.makeText(RuleDetailsActivity.this, "State Clicked: "+item.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(Action item) {
        Toast.makeText(RuleDetailsActivity.this, "Action Clicked: "+item.getId(), Toast.LENGTH_SHORT).show();
    }
}
