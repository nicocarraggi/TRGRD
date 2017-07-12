package com.example.nicolascarraggi.trgrd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.adapters.ActionsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.EventsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyActionOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyEventOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyStateOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.StatesAdapter;
import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.ExampleRule;
import com.example.nicolascarraggi.trgrd.rulesys.InputAction;
import com.example.nicolascarraggi.trgrd.rulesys.InputActionEvent;
import com.example.nicolascarraggi.trgrd.rulesys.Location;
import com.example.nicolascarraggi.trgrd.rulesys.LocationEvent;
import com.example.nicolascarraggi.trgrd.rulesys.LocationState;
import com.example.nicolascarraggi.trgrd.rulesys.MyTime;
import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.ScoreValueAction;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.TimeEvent;
import com.example.nicolascarraggi.trgrd.rulesys.TimeState;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Clock;
import com.example.nicolascarraggi.trgrd.rulesys.devices.InputActionDevice;
import com.example.nicolascarraggi.trgrd.rulesys.devices.NotificationDevice;
import com.example.nicolascarraggi.trgrd.rulesys.devices.ScoreDevice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CreateRuleActivity extends RuleSystemBindingActivity
        implements MyEventOnItemClickListener,
        MyStateOnItemClickListener,
        MyActionOnItemClickListener,
        View.OnClickListener {

    private final int REQUEST_CODE_TRIGGER = 1;
    private final int REQUEST_CODE_ACTION = 2;

    private final int ASK_LOCATION_ARRIVING = 1;
    private final int ASK_LOCATION_LEAVING = 2;
    private final int ASK_LOCATION_CURRENTLY = 3;

    private int id;
    private boolean isCreate, isFromExampleRule, isBoundOnce;
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
    private LinearLayout llCreateRuleTriggers, llCreateRuleActions;
    private EditText etName;
    private Button bAddTrigger, bAddAction;

    // for time logging
    private MyTime start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule);

        // Get extra int ID! IF real id (>=0), edit rule!!!
        this.isCreate = getIntent().getBooleanExtra("iscreate",true);
        this.isFromExampleRule = getIntent().getBooleanExtra("isfromexamplerule",false);
        this.id = getIntent().getIntExtra("ruleid",2); // TODO replace with default ERROR VALUE ? (-1)

        start = new MyTime();

        this.etName = (EditText) findViewById(R.id.etCreateRuleOpenName);
        this.llCreateRuleTriggers = (LinearLayout) findViewById(R.id.llCreateRuleTriggers);
        this.llCreateRuleActions = (LinearLayout) findViewById(R.id.llCreateRuleActions);
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

        if(isFromExampleRule){
            llCreateRuleTriggers.setVisibility(View.GONE);
            llCreateRuleActions.setVisibility(View.GONE);
        }

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
        if (id == R.id.action_rule_save) {
            if(isRuleValid()) {
                if(isCreate) {
                    int newId = ruleSystemService.getNewId();
                    this.rule = new Rule(newId, etName.getText().toString(), events, states, actions);
                    ruleSystemService.addRule(rule);
                    this.rule.setActive(true);
                    Intent intent = new Intent(this, RuleDetailsActivity.class);
                    intent.putExtra("ruleid",rule.getId());
                    startActivity(intent);
                    if (isFromExampleRule) {
                        Intent backIntent = new Intent();
                        setResult(RESULT_OK, backIntent);
                    }
                } else {
                    this.rule.setName(etName.getText().toString());
                    this.rule.reset(events,states,actions);
                }
                // persist delete of instances!
                persistDeleteInstances();
                end = new MyTime();
                MyLogger.timeframeLog("create rule open",start,end);
                finish();
            } else {
                Toast.makeText(CreateRuleActivity.this, "A rule requires a name, 1 or more triggers and 1 or more actions!", Toast.LENGTH_LONG).show();
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
            a.getDevice().deleteActionInstance(a.getId());
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

            eventsAdapter = new EventsAdapter(this, events, true, !isFromExampleRule, true);
            statesAdapter = new StatesAdapter(this, states, true, !isFromExampleRule);
            actionsAdapter = new ActionsAdapter(this, actions, true, !isFromExampleRule);

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

            if(isCreate && isFromExampleRule){
                ExampleRule exampleRule = ruleSystemService.getExampleRule(id);
                etName.setText(exampleRule.getName());
                for (Event e: exampleRule.getEvents()){
                    addEvent(e);
                }
                for (State s: exampleRule.getStates()){
                    addState(s);
                }
                for (Action a: exampleRule.getActions()){
                    addAction(a);
                }
            }
        }
    }

    private void addEvent(Event event){
        // if instance must be created ...
        if (event.isTimeEvent()) {
            MyTime d = new MyTime();
            d.setMinutes(0);
            TimeEvent timeEvent = ((Clock) event.getDevice()).getTimeAtInstance((TimeEvent) event, d);
            event = timeEvent;
        } else if (event.isInputActionEvent()){
            InputActionEvent inputActionEvent = (InputActionEvent) event;
            askInputAction(inputActionEvent, null);
            // Return because event will be added later!
            return;
        } else if (event.isLocationEvent()){
            LocationEvent locationEvent = (LocationEvent) event;
            if (locationEvent.getLocationEventType() == LocationEvent.LocationEventType.ARRIVING){
                askLocation(ASK_LOCATION_ARRIVING,event, null);
            } else if (locationEvent.getLocationEventType() == LocationEvent.LocationEventType.LEAVING){
                askLocation(ASK_LOCATION_LEAVING,event, null);
            }
            // Return because event will be added later!
            return;
        }
        events.add(event);
        eventsAdapter.updateData(events);
    }

    private void addState(State state){
        if (state.isTimeState()){
            MyTime dFrom = new MyTime();
            MyTime dTo = new MyTime();
            dFrom.setMinutes(0);
            dTo.setMinutes(0);
            TimeState timeState = ((Clock) state.getDevice()).getTimeFromToInstance((TimeState) state,dFrom,dTo);
            state = timeState;
        } else if (state.isLocationState()){
            askLocation(ASK_LOCATION_CURRENTLY,state, null);
            // Return because state will be added later!
            return;
        }
        states.add(state);
        statesAdapter.updateData(states);
    }

    private void addAction(Action action){
        if (action.isNotificationAction()){
            askNotification((NotificationAction) action,null);
            // Return because action will be added later!
            return;
        } else if (action.isScoreValueAction()){
            askScoreValue((ScoreValueAction) action,null);
            // Return because action will be added later!
            return;
        }
        actions.add(action);
        actionsAdapter.updateData(actions);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TRIGGER) {
            // ADD TRIGGER RESULT
            if(resultCode == RESULT_OK) {
                String type = data.getStringExtra("type");
                int devId = data.getIntExtra("devid",0);
                int id = data.getIntExtra("id",0);
                if (type.equals("event")){
                    Event event = ruleSystemService.getDeviceManager().getDevice(devId).getEvent(id);
                    addEvent(event);
                } else if (type.equals("state")){
                    State state = ruleSystemService.getDeviceManager().getDevice(devId).getState(id);// if instance must be created ...
                    addState(state);
                }
            }
        } else if (requestCode == REQUEST_CODE_ACTION){
            // ADD ACTION RESULT
            if(resultCode == RESULT_OK) {
                int devId = data.getIntExtra("devid",0);
                int id = data.getIntExtra("id",0);
                Action action = ruleSystemService.getDeviceManager().getDevice(devId).getAction(id);
                addAction(action);
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
                Intent iTriggers = new Intent(CreateRuleActivity.this, AddEventOrStateActivity.class);
                boolean hasEvent = (!events.isEmpty());
                iTriggers.putExtra("hasevent",hasEvent);
                startActivityForResult(iTriggers, REQUEST_CODE_TRIGGER);
                break;
            case R.id.bCreateRuleOpenAddAction:
                Intent iActions = new Intent(CreateRuleActivity.this, AddActionActivity.class);
                startActivityForResult(iActions, REQUEST_CODE_ACTION);
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
                        CreateRuleActivity.this.deleteInstance(type,event,state,action);
                        if (type.equals("event") && (event != null)){
                            CreateRuleActivity.this.events.remove(event);
                            CreateRuleActivity.this.eventsAdapter.updateData(CreateRuleActivity.this.events);
                        } else if (type.equals("state") && (state != null)){
                            CreateRuleActivity.this.states.remove(state);
                            CreateRuleActivity.this.statesAdapter.updateData(CreateRuleActivity.this.states);
                        } else if (type.equals("action") && (action != null)){
                            CreateRuleActivity.this.actions.remove(action);
                            CreateRuleActivity.this.actionsAdapter.updateData(CreateRuleActivity.this.actions);
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
            case R.id.bEventValueZero:
                if (item.isTimeEvent()){
                    editTime((Button) view, (TimeEvent) item);
                } else if(item.isInputActionEvent()){
                    InputActionEvent inputActionEvent = (InputActionEvent) item;
                    askInputAction(inputActionEvent, inputActionEvent.getInputAction());
                } else if(item.isLocationEvent()){
                    LocationEvent locationEvent = (LocationEvent) item;
                    if(locationEvent.getLocationEventType() == LocationEvent.LocationEventType.ARRIVING){
                        askLocation(ASK_LOCATION_ARRIVING,item, locationEvent.getLocation());
                    } else if(locationEvent.getLocationEventType() == LocationEvent.LocationEventType.LEAVING){
                        askLocation(ASK_LOCATION_LEAVING,item, locationEvent.getLocation());
                    }
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
            case R.id.bStateValueZero:
            case R.id.bStateValueOne:
                if (item.isTimeState()){
                    editTime((Button) view, (TimeState) item);
                } else if (item.isLocationState()){
                    LocationState locationState = (LocationState) item;
                    askLocation(ASK_LOCATION_CURRENTLY,item,locationState.getLocation());
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
            case R.id.bActionValueZero:
                if (item.isNotificationAction()){
                    askNotification((NotificationAction) item, (NotificationAction) item);
                } else if (item.isScoreValueAction()){
                    askScoreValue((ScoreValueAction) item, (ScoreValueAction) item);
                }
                break;
        }
    }

    // ------------------------
    // INPUT ACTION ASKING CODE
    // ------------------------

    public void askInputAction(final InputActionEvent inputActionEvent, final InputAction oldInputAction){
        final ArrayList<InputAction> inputActions = ((InputActionDevice) inputActionEvent.getDevice()).getInputActions();
        CharSequence inputActionNames[] = new CharSequence[inputActions.size()];
        for(int i=0; i<inputActions.size(); i++){
            inputActionNames[i] = inputActions.get(i).getDescription()+" "+inputActions.get(i).getName();
        }

        // TODO if no input actions ( size = 0 ) throw error?

        if (oldInputAction == null && isFromExampleRule){
            // IF oldInputAction is null AND is from example rule, just select first option to avoid showing multiple
            //    dialogs at the start! User can change the value afterwards.
            CreateRuleActivity.this.onInputActionClick(inputActions.get(0));
            return;
        }

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateRuleActivity.this);
        builder.setTitle("Pick a(n) "+inputActionEvent.getInputActionType()+":");
        builder.setItems(inputActionNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputAction selectedInputAction = inputActions.get(which);
                // Only change event or state IF there was previously no location OR the selected location is different from the old one.
                if(oldInputAction == null || (selectedInputAction.getId() != oldInputAction.getId())) {
                    Log.d("TRGRD","CreateRuleOpenActivity askInputAction UPDATE");
                    if (oldInputAction != null) CreateRuleActivity.this.events.remove(inputActionEvent);
                    CreateRuleActivity.this.onInputActionClick(selectedInputAction);
                }
            }
        });
        builder.show();
    }

    private void onInputActionClick(InputAction selectedInputAction) {
        events.add(selectedInputAction.getInputActionEvent());
        eventsAdapter.updateData(events);
    }

    // --------------------
    // LOCATION ASKING CODE
    // --------------------

    public void onLocationArrivingAtClick(Location location) {
        LocationEvent locationEvent = location.getArrivingAt();
        events.add(locationEvent);
        eventsAdapter.updateData(events);
    }

    public void onLocationLeavingClick(Location location) {
        LocationEvent locationEvent = location.getLeaving();
        events.add(locationEvent);
        eventsAdapter.updateData(events);
    }

    public void onLocationCurrentlyAtClick(Location location) {
        LocationState locationState = location.getCurrentlyAt();
        states.add(locationState);
        statesAdapter.updateData(states);
    }

    public void askLocation(final int type, final Event eventOrState, final Location oldLocation){
        Log.d("TRGRD","CreateRuleOpenActivity askLocation "+type+", "+eventOrState.getName()+", "+oldLocation);
        final ArrayList<Location> locations = new ArrayList<>();
        locations.addAll(ruleSystemService.getLocations());
        CharSequence locationNames[] = new CharSequence[locations.size()];
        for(int i=0; i<locations.size(); i++){
            locationNames[i] = locations.get(i).getName();
        }
        // TODO if no locations ( size = 0 ) throw error?

        if (oldLocation == null && isFromExampleRule){
            // IF oldLocation is null AND is from example rule, just select first option to avoid showing multiple
            //    dialogs at the start! User can change the value afterwards.
            switch (type) {
                case ASK_LOCATION_ARRIVING:
                    CreateRuleActivity.this.onLocationArrivingAtClick(locations.get(0));
                    break;
                case ASK_LOCATION_LEAVING:
                    CreateRuleActivity.this.onLocationLeavingClick(locations.get(0));
                    break;
                case ASK_LOCATION_CURRENTLY:
                    CreateRuleActivity.this.onLocationCurrentlyAtClick(locations.get(0));
                    break;
            }
            return;
        }

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateRuleActivity.this);
        builder.setTitle("Pick a location:");
        builder.setItems(locationNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Location selectedLocation = locations.get(which);
                // Only change event or state IF there was previously no location OR the selected location is different from the old one.
                if(oldLocation == null || !selectedLocation.getId().equals(oldLocation.getId())) {
                    Log.d("TRGRD","CreateRuleOpenActivity askLocation UPDATE");
                    switch (type) {
                        case ASK_LOCATION_ARRIVING:
                            if (oldLocation != null) CreateRuleActivity.this.events.remove(eventOrState);
                            CreateRuleActivity.this.onLocationArrivingAtClick(selectedLocation);
                            break;
                        case ASK_LOCATION_LEAVING:
                            if (oldLocation != null) CreateRuleActivity.this.events.remove(eventOrState);
                            CreateRuleActivity.this.onLocationLeavingClick(selectedLocation);
                            break;
                        case ASK_LOCATION_CURRENTLY:
                            if (oldLocation != null) CreateRuleActivity.this.states.remove(eventOrState);
                            CreateRuleActivity.this.onLocationCurrentlyAtClick(selectedLocation);
                            break;
                    }
                }
            }
        });
        builder.show();
    }

    // ------------------------
    // NOTIFICATION ASKING CODE
    // ------------------------

    public void onNoficationClick(NotificationAction action){
        actions.add(action);
        actionsAdapter.updateData(actions);
    }

    public void askNotification(final NotificationAction action, final NotificationAction oldAction){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateRuleActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = CreateRuleActivity.this.getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_notification, null);

        final EditText etTitle, etText;
        etTitle = (EditText) view.findViewById(R.id.etCreateRuleOpenNotificationTitle);
        etText = (EditText) view.findViewById(R.id.etCreateRuleOpenNotificationText);

        if(oldAction != null){
            etTitle.setText(oldAction.getTitle());
            etText.setText(oldAction.getText());
        } else if(isFromExampleRule) {
            // IF oldAction is null AND from example rule, just select first option to avoid showing multiple
            //    dialogs at the start! User can change the value afterwards.
            NotificationAction noAc = action;
            noAc = ((NotificationDevice) action.getDevice()).getNotifyAction("...",
                    "...", noAc.getNotificationActionType());
            onNoficationClick(noAc);
            return;
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle("Notification")
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText etTitle, etText;
                        etTitle = (EditText) view.findViewById(R.id.etCreateRuleOpenNotificationTitle);
                        etText = (EditText) view.findViewById(R.id.etCreateRuleOpenNotificationText);
                        String title, text;
                        title = etTitle.getText().toString();
                        text = etText.getText().toString();
                        NotificationAction noAc = action;
                        if(oldAction == null){
                            noAc = ((NotificationDevice) action.getDevice()).getNotifyAction(title,
                                    text, noAc.getNotificationActionType());
                        } else {
                            ((NotificationDevice) action.getDevice()).editNotifyAction(noAc,title,text);
                        }
                        onNoficationClick(noAc);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // DO nothing
                    }
                });
        builder.show();
    };

    // ----------------------
    // SCOREVALUE ASKING CODE
    // ----------------------

    public void onScoreValueClick(ScoreValueAction action){
        actions.add(action);
        actionsAdapter.updateData(actions);
    }

    public void askScoreValue(final ScoreValueAction action, final ScoreValueAction oldAction){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateRuleActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = CreateRuleActivity.this.getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_scorevalue, null);

        final NumberPicker np = (NumberPicker) view.findViewById(R.id.npScoreValue);
        np.setMaxValue(100);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);

        if(oldAction != null){
            np.setValue(oldAction.getValue());
        } else if(isFromExampleRule) {
            // IF oldAction is null AND from example rule, just select 1 to avoid showing multiple
            //    dialogs at the start! User can change the value afterwards.
            ScoreDevice scoreDevice = (ScoreDevice) action.getDevice();
            ScoreValueAction scoreValueAction = scoreDevice.getScoreValueAction(action,1);
            onScoreValueClick(scoreValueAction);
            return;
        }

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle("Score value")
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        NumberPicker np = (NumberPicker) view.findViewById(R.id.npScoreValue);
                        ScoreValueAction scoreValueAction = action;
                        ScoreDevice scoreDevice = (ScoreDevice) action.getDevice();
                        if(oldAction == null){
                            scoreValueAction = scoreDevice.getScoreValueAction(action,np.getValue());
                        } else {
                            scoreDevice.editScoreValueAction(action,np.getValue());
                        }
                        onScoreValueClick(scoreValueAction);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // DO nothing
                    }
                });
        builder.show();
    }

}
