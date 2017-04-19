package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.RulesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;

import java.util.Set;

public class RulesFragment extends TrgrdFragment  implements MyOnItemClickListener<Rule> {

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

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvRules);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (mListener != null) {
            Log.d("TRGRD","RulesFragment test isServiceStarted = "+mListener.getIsServiceStarted());
            if (isServiceBound) showRules();
        }

        return view;
    }

    private void showRules(){
        this.rules = mListener.getRuleSystemService().getRules();
        mAdapter = new RulesAdapter(this,rules);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(Rule rule) {
        Intent intent = new Intent(this.getContext(), RuleDetailsActivity.class);
        intent.putExtra("id",rule.getId());
        startActivity(intent);
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
        if (isServiceBound) showRules();
    }
}
