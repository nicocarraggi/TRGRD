package com.example.nicolascarraggi.trgrd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.TypesAdapter;
import com.example.nicolascarraggi.trgrd.errors.NullActionException;
import com.example.nicolascarraggi.trgrd.errors.NullEventException;
import com.example.nicolascarraggi.trgrd.errors.NullStateException;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.ActionType;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.InputAction;
import com.example.nicolascarraggi.trgrd.rulesys.InputActionEvent;
import com.example.nicolascarraggi.trgrd.rulesys.Location;
import com.example.nicolascarraggi.trgrd.rulesys.LocationEvent;
import com.example.nicolascarraggi.trgrd.rulesys.LocationState;
import com.example.nicolascarraggi.trgrd.rulesys.MyTime;
import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.RuleTemplate;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;
import com.example.nicolascarraggi.trgrd.rulesys.TimeEvent;
import com.example.nicolascarraggi.trgrd.rulesys.TimeState;
import com.example.nicolascarraggi.trgrd.rulesys.Type;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Clock;
import com.example.nicolascarraggi.trgrd.rulesys.devices.InputActionDevice;
import com.example.nicolascarraggi.trgrd.rulesys.devices.NotificationDevice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CreateRuleFromTemplateActivity extends RuleSystemBindingActivity implements MyOnItemClickListener<Type> {

    private final int REQUEST_CODE_EVENT_ADD = 1;
    private final int REQUEST_CODE_EVENT_RENEW = 2;
    private final int REQUEST_CODE_STATE_ADD = 3;
    private final int REQUEST_CODE_STATE_RENEW = 4;
    private final int REQUEST_CODE_ACTION_ADD = 5;
    private final int REQUEST_CODE_ACTION_RENEW = 6;

    private final int ASK_LOCATION_ARRIVING = 1;
    private final int ASK_LOCATION_LEAVING = 2;
    private final int ASK_LOCATION_CURRENTLY = 3;

    private int id;
    private boolean isCreate, isBoundOnce;
    private RuleTemplate ruleTemplateInstance;
    private TypesAdapter triggerTypesAdapter;
    private TypesAdapter actionTypesAdapter;
    private RecyclerView.LayoutManager mLayoutManagerTriggers, mLayoutManagerActions;

    private Set<Event> deletedEventInstaces;
    private Set<State> deletedStateInstaces;
    private Set<Action> deletedActionInstaces;

    private EditText etName;
    private RecyclerView rvTriggers, rvActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule_from_template);

        // Get extra int ID! IF real id (>=0), edit rule!!!
        this.isCreate = getIntent().getBooleanExtra("iscreate",true);
        this.id = getIntent().getIntExtra("ruletemplateinstanceid",2); // TODO replace with default ERROR VALUE ? (-1)

        this.etName = (EditText) findViewById(R.id.etCreateRuleFromTemplateName);
        this.rvTriggers = (RecyclerView) findViewById(R.id.rvCreateRuleFromTemplateTriggers);
        this.rvActions = (RecyclerView) findViewById(R.id.rvCreateRuleFromTemplateActions);

        this.deletedEventInstaces = new HashSet<>();
        this.deletedStateInstaces = new HashSet<>();
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
        if (id == R.id.action_rule_save) {
            if(isRuleValid()) {
                if(isCreate) {
                    int newId = ruleSystemService.getNewId();
                    try {
                        ruleTemplateInstance.setName(etName.getText().toString());
                        Rule rule = new Rule(newId,ruleTemplateInstance);
                        ruleSystemService.getRuleManager().addRule(rule);
                        rule.setActive(true);
                        Intent intent = new Intent(this, RuleDetailsActivity.class);
                        intent.putExtra("ruleid",rule.getId());
                        startActivity(intent);
                        Intent backIntent = new Intent();
                        setResult(RESULT_OK, backIntent);
                        finish();
                    } catch (NullActionException e) {
                        e.printStackTrace();
                    } catch (NullEventException e) {
                        e.printStackTrace();
                    } catch (NullStateException e) {
                        e.printStackTrace();
                    }
                } else {
                    Rule rule = ruleTemplateInstance.getRule();
                    if(rule != null) {
                        ruleTemplateInstance.setName(etName.getText().toString());
                        ruleTemplateInstance.getRule().resetFromRuleTemplateInstance(ruleTemplateInstance);
                        finish();
                    }
                }
            } else {
                Toast.makeText(CreateRuleFromTemplateActivity.this, "The template should be filled in completely!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Check if rule name is not empty AND that the rule contains at least 1 trigger and 1 action!
    private boolean isRuleValid(){
        // show explanation message
        return (!etName.getText().toString().isEmpty() && ruleTemplateInstance.isInstanceValid());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isCreate) {
            // TODO delete ruleTemplateInstance!
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
                ruleTemplateInstance = ruleSystemService.getRuleManager().getRuleTemplateInstance(id);
                etName.setText(ruleTemplateInstance.getName());
            } else {
                ab.setTitle("Edit rule");
                ruleTemplateInstance = ruleSystemService.getRuleManager().getRuleTemplateInstance(id);
                etName.setText(ruleTemplateInstance.getName());
            }

            triggerTypesAdapter = new TypesAdapter(this, ruleTemplateInstance.getTriggerTypes(), true, true);
            actionTypesAdapter = new TypesAdapter(this, ruleTemplateInstance.getActionTypes(), true, true);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            rvTriggers.setHasFixedSize(true);
            rvActions.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManagerTriggers = new LinearLayoutManager(getApplicationContext());
            mLayoutManagerActions = new LinearLayoutManager(getApplicationContext());
            rvTriggers.setLayoutManager(mLayoutManagerTriggers);
            rvActions.setLayoutManager(mLayoutManagerActions);

            rvTriggers.setAdapter(triggerTypesAdapter);
            rvActions.setAdapter(actionTypesAdapter);

            this.isBoundOnce = true;
        } else {
            // TODO refresh adapters!
        }

    }

    @Override
    public void onItemClick(View view, Type item) {
        switch(view.getId()) {
            case R.id.ivTypeInstanceReplace:
                if(item.isEventType()){
                    Intent iEvents = new Intent(CreateRuleFromTemplateActivity.this, AddItemFromTypeActivity.class);
                    iEvents.putExtra("type","event");
                    iEvents.putExtra("typeinstanceid",item.getId());
                    if (item.isHasInstanceItem()){
                        startActivityForResult(iEvents, REQUEST_CODE_EVENT_RENEW);
                    } else {
                        startActivityForResult(iEvents, REQUEST_CODE_EVENT_ADD);
                    }
                } else if(item.isStateType()){
                    Intent iStates = new Intent(CreateRuleFromTemplateActivity.this, AddItemFromTypeActivity.class);
                    iStates.putExtra("type","state");
                    iStates.putExtra("typeinstanceid",item.getId());
                    if (item.isHasInstanceItem()){
                        startActivityForResult(iStates, REQUEST_CODE_STATE_RENEW);
                    } else {
                        startActivityForResult(iStates, REQUEST_CODE_STATE_ADD);
                    }
                } else if(item.isActionType()){
                    Intent iActions = new Intent(CreateRuleFromTemplateActivity.this, AddItemFromTypeActivity.class);
                    iActions.putExtra("type","action");
                    iActions.putExtra("typeinstanceid",item.getId());
                    if (item.isHasInstanceItem()){
                        startActivityForResult(iActions, REQUEST_CODE_ACTION_RENEW);
                    } else {
                        startActivityForResult(iActions, REQUEST_CODE_ACTION_ADD);
                    }
                }
                break;
            case R.id.bTypeInstanceValueZero:
            case R.id.bTypeInstanceValueOne:
                if(item.isEventType()){
                    Event event = ((EventType) item).getInstanceEvent();
                    if (event.isTimeEvent()) {
                        editTime((Button) view, (TimeEvent) event);
                    } else if(event.isInputActionEvent()){
                        InputActionEvent inputActionEvent = (InputActionEvent) event;
                        askInputAction(inputActionEvent,item,inputActionEvent.getInputAction());
                    } else if(event.isLocationEvent()){
                        LocationEvent locationEvent = (LocationEvent) event;
                        if(locationEvent.getLocationEventType() == LocationEvent.LocationEventType.ARRIVING){
                            askLocation(ASK_LOCATION_ARRIVING,item,locationEvent.getLocation());
                        } else if(locationEvent.getLocationEventType() == LocationEvent.LocationEventType.LEAVING){
                            askLocation(ASK_LOCATION_LEAVING,item,locationEvent.getLocation());
                        }
                    }
                } else if(item.isStateType()){
                    State state = ((StateType) item).getInstanceState();
                    if (state.isTimeState()){
                        editTime((Button) view, (TimeState) state);
                    } else if (state.isLocationState()){
                        LocationState locationState = (LocationState) state;
                        askLocation(ASK_LOCATION_CURRENTLY,item,locationState.getLocation());
                    }
                } else if(item.isActionType()){
                    Action action = ((ActionType) item).getInstanceAction();
                    if (action.isNotificationAction()){
                        askNotification((NotificationAction) action,(ActionType)item, (NotificationAction) action);
                    }
                }
                break;
            case R.id.bTypeInstanceValueTwo:
                if(item.isStateType()){
                    State state = ((StateType) item).getInstanceState();
                    if (state.isTimeState()){
                        editTime((Button) view, (TimeState) state);
                    }
                }
                break;
        }
    }

    private void addEvent(int itemId, int itemDevId, int itemTypeInstanceId){
        EventType eventTypeInstance = ruleSystemService.getDeviceManager().getEventTypeInstance(itemTypeInstanceId);
        Event event = ruleSystemService.getDeviceManager().getDevice(itemDevId).getEvent(itemId);
        // if instance must be created ...
        if (event.isTimeEvent()) {
            MyTime d = new MyTime();
            d.setMinutes(0);
            TimeEvent timeEvent = ((Clock) event.getDevice()).getTimeAtInstance((TimeEvent) event, d);
            event = timeEvent;
        } else if (event.isInputActionEvent()){
            InputActionEvent inputActionEvent = (InputActionEvent) event;
            askInputAction(inputActionEvent,eventTypeInstance,null);
            // Return because event will be added later!
            return;
        } else if (event.isLocationEvent()){
            LocationEvent locationEvent = (LocationEvent) event;
            if (locationEvent.getLocationEventType() == LocationEvent.LocationEventType.ARRIVING){
                askLocation(ASK_LOCATION_ARRIVING,eventTypeInstance, null);
            } else if (locationEvent.getLocationEventType() == LocationEvent.LocationEventType.LEAVING){
                askLocation(ASK_LOCATION_LEAVING,eventTypeInstance, null);
            }
            // Return because event will be added later!
            return;
        }
        eventTypeInstance.setInstanceEvent(event);
        triggerTypesAdapter.notifyDataSetChanged();
    }

    private void addState(int itemId, int itemDevId, int itemTypeInstanceId){
        StateType stateTypeInstance = ruleSystemService.getDeviceManager().getStateTypeInstance(itemTypeInstanceId);
        State state = ruleSystemService.getDeviceManager().getDevice(itemDevId).getState(itemId);
        Log.d("TRGRD","CreateRuleFromTemplateActivity addState state = "+state);
        if (state.isTimeState()){
            MyTime dFrom = new MyTime();
            MyTime dTo = new MyTime();
            dFrom.setMinutes(0);
            dTo.setMinutes(0);
            TimeState timeState = ((Clock) state.getDevice()).getTimeFromToInstance((TimeState) state,dFrom,dTo);
            state = timeState;
        } else if (state.isLocationState()){
            LocationState locationState = (LocationState) state;
            askLocation(ASK_LOCATION_CURRENTLY, stateTypeInstance, null);
            // Return because state will be added later!
            return;
        }
        stateTypeInstance.setInstanceState(state);
        triggerTypesAdapter.notifyDataSetChanged();
    }

    private void addAction(int itemId, int itemDevId, int itemTypeInstanceId){
        ActionType actionTypeInstance = ruleSystemService.getDeviceManager().getActionTypeInstance(itemTypeInstanceId);
        Action action = ruleSystemService.getDeviceManager().getDevice(itemDevId).getAction(itemId);
        if (action.isNotificationAction()){
            askNotification((NotificationAction) action, actionTypeInstance, null);
            // Return because action will be added later!
            return;
        }
        actionTypeInstance.setInstanceAction(action);
        actionTypesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int itemId, itemDevId, itemTypeInstanceId;
        if(resultCode == RESULT_OK){
            itemId = data.getIntExtra("id",0);
            itemDevId = data.getIntExtra("devid",0);
            itemTypeInstanceId = data.getIntExtra("typeinstanceid",0);
            switch (requestCode) {
                case REQUEST_CODE_EVENT_ADD:
                    addEvent(itemId,itemDevId,itemTypeInstanceId);
                    break;
                case REQUEST_CODE_EVENT_RENEW:
                    // TODO delete previous instance?
                    addEvent(itemId,itemDevId,itemTypeInstanceId);
                    break;
                case REQUEST_CODE_STATE_ADD:
                    addState(itemId,itemDevId,itemTypeInstanceId);
                    break;
                case REQUEST_CODE_STATE_RENEW:
                    // TODO delete previous instance?
                    addState(itemId,itemDevId,itemTypeInstanceId);
                    break;
                case REQUEST_CODE_ACTION_ADD:
                    addAction(itemId,itemDevId,itemTypeInstanceId);
                    break;
                case REQUEST_CODE_ACTION_RENEW:
                    // TODO delete previous instance?
                    addAction(itemId,itemDevId,itemTypeInstanceId);
                    break;
                default:
                    // ERROR: bad request code?
                    break;
            }

        }
    }

    // ----------------
    // TIME ASKING CODE
    // ----------------


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
        if(button.getId() == R.id.bTypeInstanceValueOne){
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

    // ------------------------
    // INPUT ACTION ASKING CODE
    // ------------------------

    public void askInputAction(final InputActionEvent inputActionEvent, final Type eventTypeInstance, final InputAction oldInputAction){
        final ArrayList<InputAction> inputActions = ((InputActionDevice) inputActionEvent.getDevice()).getInputActions();
        CharSequence inputActionNames[] = new CharSequence[inputActions.size()];
        for(int i=0; i<inputActions.size(); i++){
            inputActionNames[i] = inputActions.get(i).getDescription()+" "+inputActions.get(i).getName();
        }
        // TODO if no input actions ( size = 0 ) throw error?
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateRuleFromTemplateActivity.this);
        builder.setTitle("Pick a(n) "+inputActionEvent.getInputActionType()+":");
        builder.setItems(inputActionNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputAction selectedInputAction = inputActions.get(which);
                // Only change event or state IF there was previously no location OR the selected location is different from the old one.
                if(oldInputAction == null || (selectedInputAction.getId() != oldInputAction.getId())) {
                    Log.d("TRGRD","CreateRuleFromTemplateActivity askInputAction UPDATE");
                    CreateRuleFromTemplateActivity.this.onInputActionClick(selectedInputAction, eventTypeInstance);
                }
            }
        });
        builder.show();
    }

    private void onInputActionClick(InputAction selectedInputAction, Type typeInstance) {
        InputActionEvent inputActionEvent = selectedInputAction.getInputActionEvent();
        EventType eventTypeInstance = (EventType) typeInstance;
        eventTypeInstance.setInstanceEvent(inputActionEvent);
        triggerTypesAdapter.notifyDataSetChanged();
    }

    // --------------------
    // LOCATION ASKING CODE
    // --------------------

    public void onLocationArrivingAtClick(Location location, Type typeInstance) {
        LocationEvent locationEvent = location.getArrivingAt();
        EventType eventTypeInstance = (EventType) typeInstance;
        eventTypeInstance.setInstanceEvent(locationEvent);
        triggerTypesAdapter.notifyDataSetChanged();
    }

    public void onLocationLeavingClick(Location location, Type typeInstance) {
        LocationEvent locationEvent = location.getLeaving();
        EventType eventTypeInstance = (EventType) typeInstance;
        eventTypeInstance.setInstanceEvent(locationEvent);
        triggerTypesAdapter.notifyDataSetChanged();
    }

    public void onLocationCurrentlyAtClick(Location location, Type typeInstance) {
        LocationState locationState = location.getCurrentlyAt();
        StateType stateTypeInstance = (StateType) typeInstance;
        stateTypeInstance.setInstanceState(locationState);
        triggerTypesAdapter.notifyDataSetChanged();
    }

    public void askLocation(final int type, final Type eventOrStateTypeInstance, final Location oldLocation){
        //Log.d("TRGRD","CreateRuleFromTemplateActivity askLocation "+type+", "+eventOrState.getName()+", "+oldLocation);
        final ArrayList<Location> locations = new ArrayList<>();
        locations.addAll(ruleSystemService.getLocationManager().getLocations());
        CharSequence locationNames[] = new CharSequence[locations.size()];
        for(int i=0; i<locations.size(); i++){
            locationNames[i] = locations.get(i).getName();
        }
        // TODO if no locations ( size = 0 ) throw error?
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateRuleFromTemplateActivity.this);
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
                            CreateRuleFromTemplateActivity.this.onLocationArrivingAtClick(selectedLocation,eventOrStateTypeInstance);
                            break;
                        case ASK_LOCATION_LEAVING:
                            CreateRuleFromTemplateActivity.this.onLocationLeavingClick(selectedLocation,eventOrStateTypeInstance);
                            break;
                        case ASK_LOCATION_CURRENTLY:
                            CreateRuleFromTemplateActivity.this.onLocationCurrentlyAtClick(selectedLocation,eventOrStateTypeInstance);
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

    public void onNoficationClick(NotificationAction action, ActionType actionTypeInstance){
        actionTypeInstance.setInstanceAction(action);
        actionTypesAdapter.notifyDataSetChanged();
    }

    public void askNotification(final NotificationAction action, final ActionType actionTypeInstance, final NotificationAction oldAction){
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateRuleFromTemplateActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = CreateRuleFromTemplateActivity.this.getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_notification, null);

        final EditText etTitle, etText;
        etTitle = (EditText) view.findViewById(R.id.etCreateRuleOpenNotificationTitle);
        etText = (EditText) view.findViewById(R.id.etCreateRuleOpenNotificationText);

        if(oldAction != null){
            etTitle.setText(oldAction.getTitle());
            etText.setText(oldAction.getText());
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
                        onNoficationClick(noAc, actionTypeInstance);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // DO nothing
                    }
                });
        builder.show();
    };

}
