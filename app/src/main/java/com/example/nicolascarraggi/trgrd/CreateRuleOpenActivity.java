package com.example.nicolascarraggi.trgrd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.adapters.ActionsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.EventsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyActionOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyEventOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyStateOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.StatesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.MyTime;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.TimeEvent;
import com.example.nicolascarraggi.trgrd.rulesys.TimeState;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Clock;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CreateRuleOpenActivity extends RuleSystemBindingActivity
        implements MyEventOnItemClickListener,
        MyStateOnItemClickListener,
        MyActionOnItemClickListener,
        View.OnClickListener{

    private int id;
    private boolean isCreate, isBoundOnce;
    private Rule rule;
    private Set<Event> events;
    private Set<Event> deletedEventInstaces;
    private Set<State> states;
    private Set<State> deletedStateInstaces;
    private Set<Action> actions;
    private Set<Action> deletedActionInstaces;
    private EventsAdapter eventsAdapter;
    private StatesAdapter statesAdapter;
    private ActionsAdapter actionsAdapter;
    private RecyclerView.LayoutManager mLayoutManagerEvents, mLayoutManagerStates, mLayoutManagerActions;
    private RecyclerView rvEvents, rvStates, rvActions;
    private EditText etName;
    private Button bAddTrigger, bAddAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule_open);

        // Get extra int ID! IF real id (>=0), edit rule!!!
        this.isCreate = getIntent().getBooleanExtra("iscreate",true);
        this.id = getIntent().getIntExtra("ruleid",2);

        this.etName = (EditText) findViewById(R.id.etCreateRuleOpenName);
        this.bAddAction = (Button) findViewById(R.id.bCreateRuleOpenAddAction);
        this.bAddTrigger = (Button) findViewById(R.id.bCreateRuleOpenAddTrigger);
        this.rvEvents = (RecyclerView) findViewById(R.id.rvCreateRuleOpenEvents);
        this.rvStates = (RecyclerView) findViewById(R.id.rvCreateRuleOpenStates);
        this.rvActions = (RecyclerView) findViewById(R.id.rvCreateRuleOpenActions);

        this.events = new HashSet<>();
        this.deletedEventInstaces = new HashSet<>();
        this.states = new HashSet<>();
        this.deletedStateInstaces = new HashSet<>();
        this.actions = new HashSet<>();
        this.deletedActionInstaces = new HashSet<>();

        this.isBoundOnce = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rule_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            if(isRuleValid()) {
                // persist delete of instances!
                persistDeleteInstances();
                if(isCreate) {
                    int newId = ruleSystemService.getNewId();
                    this.rule = new Rule(newId, etName.getText().toString(), events, states, actions);
                    ruleSystemService.addRule(rule);
                    Intent intent = new Intent(this, RuleDetailsActivity.class);
                    intent.putExtra("ruleid",rule.getId());
                    startActivity(intent);
                } else {
                    this.rule.setName(etName.getText().toString());
                    this.rule.setEvents(events);
                    this.rule.setStates(states);
                    this.rule.setActions(actions);
                }
                finish();
            } else {
                Toast.makeText(CreateRuleOpenActivity.this, "A rule requires a name, 1 or more triggers and 1 or more actions!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void persistDeleteInstances() {
        for(Event e: deletedEventInstaces){
            e.getDevice().deleteEventInstance(e.getId());
        }
        for(State s: deletedStateInstaces){
            s.getDevice().deleteStateInstance(s.getId());
        }
        for(Action a: deletedActionInstaces){
            a.getDevice().deleteStateInstance(a.getId());
        }
    }

    private void deleteInstance(String type, Event event, State state, Action action) {
        // puts deleted instances in a temporary collection!
        // if rule is saved, persist the deletes to the devices to actually delete the instances!
        // else ( in case the user goes back without saving ) do nothing, because they are still used!
        if(type.equals("event") && (event.getEventValueType() != Event.EventValueType.NONE)){
            deletedEventInstaces.add(event);
        } else if(type.equals("state") && (state.getStateValueType() != State.StateValueType.NONE)){
            deletedStateInstaces.add(state);
        } else if(type.equals("action") && (action.getActionValueType() != Action.ActionValueType.NONE)){
            deletedActionInstaces.add(action);
        }
    }

    @Override
    protected void onBound() {
        super.onBound();

        // In this case, avoid refreshing the adapters when returning from the Add Trigger or Add Action!
        // Because when items were deleted from the rule (but not saved yet) and something is added, these will be shown again!
        if(!isBoundOnce) {
            ActionBar ab = getSupportActionBar();
            if (isCreate) {
                ab.setTitle("Create rule");
            } else {
                ab.setTitle("Edit rule");
                rule = ruleSystemService.getRule(id);
                this.events.addAll(rule.getEvents());
                this.states.addAll(rule.getStates());
                this.actions.addAll(rule.getActions());
                etName.setText(rule.getName());
            }

            eventsAdapter = new EventsAdapter(this, events, true);
            statesAdapter = new StatesAdapter(this, states, true);
            actionsAdapter = new ActionsAdapter(this, actions, true);

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

            bAddTrigger.setOnClickListener(this);
            bAddAction.setOnClickListener(this);

            this.isBoundOnce = true;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // ADD TRIGGER RESULT
            if(resultCode == RESULT_OK) {
                String type = data.getStringExtra("type");
                int devId = data.getIntExtra("devid",0);
                int id = data.getIntExtra("id",0);
                if (type.equals("event")){
                    Event event = ruleSystemService.getDeviceManager().getDevice(devId).getEvent(id);
                    // if instance must be created ...
                    if (event.isTimeEvent()){
                        MyTime d = new MyTime();
                        d.setHours(14);
                        TimeEvent timeEvent = ((Clock) event.getDevice()).getTimeAtInstance((TimeEvent) event,d);
                        event = timeEvent;
                    }
                    events.add(event);
                    eventsAdapter.updateData(events);
                } else if (type.equals("state")){
                    State state = ruleSystemService.getDeviceManager().getDevice(devId).getState(id);// if instance must be created ...
                    if (state.isTimeState()){
                        MyTime dFrom = new MyTime();
                        MyTime dTo = new MyTime();
                        dFrom.setMinutes(0);
                        dTo.setMinutes(0);
                        TimeState timeState = ((Clock) state.getDevice()).getTimeFromToInstance((TimeState) state,dFrom,dTo);
                        state = timeState;
                    }
                    states.add(state);
                    statesAdapter.updateData(states);
                }
            }
        } else if (requestCode == 2){
            // ADD ACTION RESULT
            if(resultCode == RESULT_OK) {
                int devId = data.getIntExtra("devid",0);
                int id = data.getIntExtra("id",0);
                Action action = ruleSystemService.getDeviceManager().getDevice(devId).getAction(id);
                actions.add(action);
                actionsAdapter.updateData(actions);
            }
        }
    }

    // Check if rule name is not empty AND that the rule contains at least 1 trigger and 1 action!
    private boolean isRuleValid(){
        // show explanation message
        return (!etName.getText().toString().isEmpty() && (!actions.isEmpty() && (!events.isEmpty() || !states.isEmpty())));
    }

    // onClick for the buttons in activity
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.bCreateRuleOpenAddTrigger:
                Intent iTriggers = new Intent(CreateRuleOpenActivity.this, AddTriggerActivity.class);
                boolean hasEvent = (!events.isEmpty());
                iTriggers.putExtra("hasevent",hasEvent);
                startActivityForResult(iTriggers, 1);
                break;
            case R.id.bCreateRuleOpenAddAction:
                Intent iActions = new Intent(CreateRuleOpenActivity.this, AddActionActivity.class);
                startActivityForResult(iActions, 2);
                break;
        }
    }

    private void alertDelete(final String type, final Event event, final State state, final Action action){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String name = "";
        if(type.equals("event")){
            name = event.getName();
        } else if (type.equals("state")){
            name = state.getName();
        } else if (type.equals("action")){
            name = action.getName();
        }
        builder.setMessage("Are you sure you want to delete this "+type+"?")
                .setTitle("Delete "+name)
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // delete instance (if an instance exists!)
                        CreateRuleOpenActivity.this.deleteInstance(type,event,state,action);
                        if (type.equals("event") && (event != null)){
                            CreateRuleOpenActivity.this.events.remove(event);
                            CreateRuleOpenActivity.this.eventsAdapter.updateData(CreateRuleOpenActivity.this.events);
                        } else if (type.equals("state") && (state != null)){
                            CreateRuleOpenActivity.this.states.remove(state);
                            CreateRuleOpenActivity.this.statesAdapter.updateData(CreateRuleOpenActivity.this.states);
                        } else if (type.equals("action") && (action != null)){
                            CreateRuleOpenActivity.this.actions.remove(action);
                            CreateRuleOpenActivity.this.actionsAdapter.updateData(CreateRuleOpenActivity.this.actions);
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }


    private void editTime(final Button button, final TimeEvent item) {

        @SuppressLint("ValidFragment")
        class EventTimePickerFragment extends DialogFragment
                implements TimePickerDialog.OnTimeSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                int hour = item.getTime().getHours();
                int minute = item.getTime().getMinutes();

                // Create a new instance of TimePickerDialog and return it
                return new TimePickerDialog(getActivity(), this, hour, minute,true);
            }

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Do something with the time chosen by the user
                MyTime time = item.getTime();
                time.setHours(hourOfDay);
                time.setMinutes(minute);
                button.setText(time.toString());
            }
        }

        DialogFragment newFragment = new EventTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    private void editTime(final Button button, final TimeState item) {

        MyTime tempTime;
        if(button.getId() == R.id.bStateValueOne){
            tempTime = item.getTimeFrom();
        } else {
            tempTime = item.getTimeTo();
        }
        final MyTime time = tempTime;

        @SuppressLint("ValidFragment")
        class StateTimePickerFragment extends DialogFragment
                implements TimePickerDialog.OnTimeSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                int hour = time.getHours();
                int minute = time.getMinutes();

                // Create a new instance of TimePickerDialog and return it
                return new TimePickerDialog(getActivity(), this, hour, minute,true);
            }

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Do something with the time chosen by the user
                time.setHours(hourOfDay);
                time.setMinutes(minute);
                button.setText(time.toString());
            }
        }

        DialogFragment newFragment = new StateTimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");


    }

    // onItemClick for 1 Event item
    @Override
    public void onItemClick(View view, Event item) {
        switch(view.getId()) {
            case R.id.tvEventName:
                // Do something?
                break;
            case R.id.ivEventDelete:
                alertDelete("event", item, null, null);
                break;
            case R.id.bEventValueOne:
                if (item.isTimeEvent()){
                    editTime((Button) view, (TimeEvent) item);
                }
                break;
        }
    }

    // onItemClick for 1 State item
    @Override
    public void onItemClick(View view, State item) {
        switch(view.getId()) {
            case R.id.tvStateName:
                // Do something?
                break;
            case R.id.ivStateDelete:
                alertDelete("state", null, item, null);
                break;
            case R.id.bStateValueOne:
                if (item.isTimeState()){
                    editTime((Button) view, (TimeState) item);
                }
                break;
            case R.id.bStateValueTwo:
                if (item.isTimeState()){
                    editTime((Button) view, (TimeState) item);
                }
                break;
        }
    }

    // onItemClick for 1 Action item
    @Override
    public void onItemClick(View view, Action item) {
        switch(view.getId()) {
            case R.id.tvActionName:
                // Do something?
                break;
            case R.id.ivActionDelete:
                alertDelete("action", null, null, item);
                break;
        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }
}
