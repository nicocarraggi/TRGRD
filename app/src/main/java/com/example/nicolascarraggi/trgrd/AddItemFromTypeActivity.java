package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.adapters.ActionsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.EventsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyActionOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyEventOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyStateOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.StatesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.ActionType;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;
import com.example.nicolascarraggi.trgrd.rulesys.Type;

public class AddItemFromTypeActivity extends RuleSystemBindingActivity implements MyActionOnItemClickListener, MyEventOnItemClickListener, MyStateOnItemClickListener {

    private String typeType;
    private int typeInstanceId;
    private Type type;
    private EventType eventType;
    private StateType stateType;
    private ActionType actionType;
    private EventsAdapter eventsAdapter;
    private StatesAdapter statesAdapter;
    private ActionsAdapter actionsAdapter;
    private RecyclerView.LayoutManager mLayoutManagerItems;
    private TextView tvItems;
    private RecyclerView rvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_from_type);

        this.typeType = getIntent().getStringExtra("type");
        this.typeInstanceId = getIntent().getIntExtra("typeinstanceid",0); // TODO replace with default ERROR VALUE ? (-1)

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Add "+typeType);

        tvItems = (TextView) findViewById(R.id.tvAddItemFromTypeItems);
        rvItems = (RecyclerView) findViewById(R.id.rvAddItemFromTypeItems);

    }

    @Override
    protected void onBound() {
        super.onBound();

        rvItems.setHasFixedSize(true);
        mLayoutManagerItems = new LinearLayoutManager(this);
        rvItems.setLayoutManager(mLayoutManagerItems);

        if (typeType.equals("event")){
            eventType = ruleSystemService.getDeviceManager().getEventTypeInstance(typeInstanceId);
            eventsAdapter = new EventsAdapter(this, eventType.getSkeletonEvents(),false);
            rvItems.setAdapter(eventsAdapter);
            tvItems.setText("Events of type: \""+eventType.getName()+"\"");
        } else if(typeType.equals("state")){
            stateType = ruleSystemService.getDeviceManager().getStateTypeInstance(typeInstanceId);
            statesAdapter = new StatesAdapter(this, stateType.getSkeletonStates(),false);
            rvItems.setAdapter(statesAdapter);
            tvItems.setText("States of type: \""+stateType.getName()+"\"");
        } else if(typeType.equals("action")){
            actionType = ruleSystemService.getDeviceManager().getActionTypeInstance(typeInstanceId);
            actionsAdapter = new ActionsAdapter(this, actionType.getSkeletonActions(),false);
            rvItems.setAdapter(actionsAdapter);
            tvItems.setText("Actions of type: \""+actionType.getName()+"\"");
        }

    }

    @Override
    public void onItemClick(View view, Event item) {
        switch(view.getId()) {
            case R.id.tvEventName:
                Intent intent = new Intent();
                intent.putExtra("devid", item.getDevice().getId());
                intent.putExtra("id", item.getId());
                intent.putExtra("typeinstanceid",typeInstanceId);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, State item) {
        switch(view.getId()) {
            case R.id.tvStateName:
                Intent intent = new Intent();
                intent.putExtra("devid", item.getDevice().getId());
                intent.putExtra("id", item.getId());
                intent.putExtra("typeinstanceid",typeInstanceId);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, Action item) {
        switch(view.getId()) {
            case R.id.tvActionName:
                Intent intent = new Intent();
                intent.putExtra("devid", item.getDevice().getId());
                intent.putExtra("id", item.getId());
                intent.putExtra("typeinstanceid",typeInstanceId);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
