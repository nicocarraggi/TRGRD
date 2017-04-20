package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import java.util.HashSet;
import java.util.Set;

public class CreateRuleOpenActivity extends RuleSystemBindingActivity
        implements MyEventOnItemClickListener,
        MyStateOnItemClickListener,
        MyActionOnItemClickListener{

    private Rule rule;
    private Set<Event> events;
    private Set<State> states;
    private Set<Action> actions;
    private EventsAdapter eventsAdapter;
    private StatesAdapter statesAdapter;
    private ActionsAdapter actionsAdapter;
    private RecyclerView.LayoutManager mLayoutManagerEvents, mLayoutManagerStates, mLayoutManagerActions;
    private RecyclerView rvEvents, rvStates, rvActions;
    private EditText etName;
    private Button bAddTrigger, bAddAction, bCreateRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule_open);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Create Rule");

        etName = (EditText) findViewById(R.id.etCreateRuleOpenName);
        bAddAction = (Button) findViewById(R.id.bCreateRuleOpenAddAction);
        bAddTrigger = (Button) findViewById(R.id.bCreateRuleOpenAddTrigger);
        bCreateRule = (Button) findViewById(R.id.bCreateRuleOpen);
        rvEvents = (RecyclerView) findViewById(R.id.rvCreateRuleOpenEvents);
        rvStates = (RecyclerView) findViewById(R.id.rvCreateRuleOpenStates);
        rvActions = (RecyclerView) findViewById(R.id.rvCreateRuleOpenActions);

        events = new HashSet<>();
        states = new HashSet<>();
        actions = new HashSet<>();

    }

    @Override
    protected void onBound() {
        super.onBound();

        eventsAdapter = new EventsAdapter(this,events);
        statesAdapter = new StatesAdapter(this,states);
        actionsAdapter = new ActionsAdapter(this,actions);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvEvents.setHasFixedSize(true);
        rvStates.setHasFixedSize(true);
        rvActions.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerEvents = new LinearLayoutManager(getApplicationContext());
        mLayoutManagerStates = new LinearLayoutManager(getApplicationContext());
        mLayoutManagerActions = new LinearLayoutManager(getApplicationContext());
        rvEvents.setLayoutManager(mLayoutManagerEvents);
        rvStates.setLayoutManager(mLayoutManagerStates);
        rvActions.setLayoutManager(mLayoutManagerActions);

        rvEvents.setAdapter(eventsAdapter);
        rvStates.setAdapter(statesAdapter);
        rvActions.setAdapter(actionsAdapter);

        bAddTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateRuleOpenActivity.this, AddTriggerActivity.class);
                boolean hasEvent = (!events.isEmpty());
                i.putExtra("hasevent",hasEvent);
                startActivityForResult(i, 1);
            }
        });

        bAddAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CreateRuleOpenActivity.this, AddActionActivity.class);
                startActivityForResult(i, 2);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String type = data.getStringExtra("type");
                int devId = data.getIntExtra("devid",0);
                int id = data.getIntExtra("id",0);
                if (type.equals("event")){
                    Event event = ruleSystemService.getDeviceManager().getDevice(devId).getEvent(id);
                    events.add(event);
                    eventsAdapter.updateData(events);
                } else if (type.equals("state")){
                    State state = ruleSystemService.getDeviceManager().getDevice(devId).getState(id);
                    states.add(state);
                    statesAdapter.updateData(states);
                }
            }
        } else if (requestCode == 2){
            if(resultCode == RESULT_OK) {
                int devId = data.getIntExtra("devid",0);
                int id = data.getIntExtra("id",0);
                Action action = ruleSystemService.getDeviceManager().getDevice(devId).getAction(id);
                actions.add(action);
                actionsAdapter.updateData(actions);
            }
        }
    }

    @Override
    public void onItemClick(Event item) {

    }

    @Override
    public void onItemClick(State item) {

    }

    @Override
    public void onItemClick(Action item) {

    }
}
