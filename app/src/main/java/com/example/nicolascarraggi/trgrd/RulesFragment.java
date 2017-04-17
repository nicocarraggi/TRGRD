package com.example.nicolascarraggi.trgrd;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class RulesFragment extends TrgrdFragment {

    private Set<Rule> rules;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public RulesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rules, container, false);

        this.rules = new HashSet<>();

        if (mListener != null) {
            Log.d("TRGRD","RulesFragment test isServiceStarted = "+mListener.getIsServiceStarted());
            if (isServiceBound) this.rules = mListener.getRuleSystemService().getRules();
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewRules);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RulesAdapter(rules);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void notifyIsServiceStartedChanged(boolean isServiceStarted) {
        super.notifyIsServiceStartedChanged(isServiceStarted);
        Log.d("TRGRD","RulesFragment notify isServiceStarted = " + isServiceStarted);
    }

    @Override
    public void notifyIsServiceBoundChanged(boolean isServiceBound) {
        super.notifyIsServiceBoundChanged(isServiceBound);
        Log.d("TRGRD","RulesFragment notify isServiceBound = " + isServiceBound);
        if (isServiceBound) {
            this.rules = mListener.getRuleSystemService().getRules();
            this.mAdapter = new RulesAdapter(rules);
            this.mRecyclerView.setAdapter(mAdapter);
        }
    }
}
