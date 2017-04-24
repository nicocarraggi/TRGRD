package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.adapters.ActionsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.EventsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyActionOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyEventOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyStateOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.StatesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.State;

public class RuleDetailsActivity extends RuleSystemBindingActivity implements MyEventOnItemClickListener,
        MyStateOnItemClickListener, MyActionOnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private int ruleId;
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

        Intent intent = getIntent();
        this.ruleId = intent.getIntExtra("ruleid",0);

        tvRuleDetailsName = (TextView) findViewById(R.id.tvRuleDetailsName);
        rvRuleDetailsEvents = (RecyclerView) findViewById(R.id.rvRuleDetailsEvents);
        rvRuleDetailsStates = (RecyclerView) findViewById(R.id.rvRuleDetailsStates);
        rvRuleDetailsActions = (RecyclerView) findViewById(R.id.rvRuleDetailsActions);
        switchRuleDetailsActive = (SwitchCompat) findViewById(R.id.switchRuleDetailsActive);

        switchRuleDetailsActive.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onBound(){
        super.onBound();
        rule = ruleSystemService.getRule(ruleId);

        tvRuleDetailsName.setText(rule.getName());
        switchRuleDetailsActive.setChecked(rule.isActive());

        eventsAdapter = new EventsAdapter(this,rule.getEvents(),false);
        statesAdapter = new StatesAdapter(this,rule.getStates(),false);
        actionsAdapter = new ActionsAdapter(this,rule.getActions(),false);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rule_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = null;
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RuleDetailsActivity.this);
            String rulesMethodology = prefs.getString("rule_methodology_list", "2");
            if (rulesMethodology.equals("2")){
                intent = new Intent(RuleDetailsActivity.this, CreateRuleOpenActivity.class);
                intent.putExtra("iscreate",false);
                intent.putExtra("ruleid",ruleId);
            }
            if(intent != null) startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Event item) {
        Toast.makeText(RuleDetailsActivity.this, "Event Clicked: "+item.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDeleteClick(Event item) {

    }

    @Override
    public void onItemClick(State item) {
        Toast.makeText(RuleDetailsActivity.this, "State Clicked: "+item.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDeleteClick(State item) {

    }

    @Override
    public void onItemClick(Action item) {
        Toast.makeText(RuleDetailsActivity.this, "Action Clicked: "+item.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemDeleteClick(Action item) {

    }

    private void switchRuleActive(boolean b) {
        if(isServiceBound){
            if(rule.isActive()!=b){
                rule.setActive(b);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.switchRuleDetailsActive:
                switchRuleActive(b);
                break;
        }
    }
}
