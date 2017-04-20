package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.adapters.EventsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyEventOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyStateOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.StatesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.State;

public class AddTriggerActivity extends RuleSystemBindingActivity
    implements MyEventOnItemClickListener, MyStateOnItemClickListener {

    private boolean showEvents;
    private EventsAdapter eventsAdapter;
    private StatesAdapter statesAdapter;
    private RecyclerView.LayoutManager mLayoutManagerEvents, mLayoutManagerStates;
    private RecyclerView rvEvents, rvStates;
    private TextView tvEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trigger);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Add trigger");

        this.showEvents = !getIntent().getBooleanExtra("hasevent",false);

        tvEvents = (TextView) findViewById(R.id.tvAddTriggerEvents);
        rvEvents = (RecyclerView) findViewById(R.id.rvAddTriggerEvents);
        rvStates = (RecyclerView) findViewById(R.id.rvAddTriggerStates);

        if(!showEvents){
            tvEvents.setText("Events\n\nOne event has already been selected!\n");
            ((ViewGroup) rvEvents.getParent()).removeView(rvEvents);
        }

    }

    @Override
    protected void onBound() {
        super.onBound();

        if(showEvents) {
            eventsAdapter = new EventsAdapter(this, ruleSystemService.getDeviceManager().getAllEvents());
            rvEvents.setHasFixedSize(true);
            mLayoutManagerEvents = new LinearLayoutManager(getApplicationContext());
            rvEvents.setLayoutManager(mLayoutManagerEvents);
            rvEvents.setAdapter(eventsAdapter);
        }

        statesAdapter = new StatesAdapter(this,ruleSystemService.getDeviceManager().getAllStates());

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvStates.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManagerStates = new LinearLayoutManager(getApplicationContext());
        rvStates.setLayoutManager(mLayoutManagerStates);

        rvStates.setAdapter(statesAdapter);

    }

    @Override
    public void onItemClick(Event item) {
        returnClickedItemId("event",item.getDevice().getId(),item.getId());
    }

    @Override
    public void onItemClick(State item) {
        returnClickedItemId("state",item.getDevice().getId(),item.getId());
    }

    public void returnClickedItemId(String type, int devId, int id){
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("devid", devId);
        intent.putExtra("id", id);
        setResult(RESULT_OK, intent);
        finish();
    }
}
