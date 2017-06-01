package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.RulesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.getInteger;

public class RulesFragment extends TrgrdFragment  implements MyOnItemClickListener<Rule> {

    public static final String RULE_METHODOLOGY_CONFIG = "0";
    public static final String RULE_METHODOLOGY_TEMPLATE = "1";
    public static final String RULE_METHODOLOGY_OPEN = "2";

    private Set<Rule> rules;
    private RecyclerView mRecyclerView;
    private RulesAdapter mAdapter;
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

        this.rules = new HashSet<>();

        if (mListener != null) {
            Log.d("TRGRD","RulesFragment test isServiceStarted = "+mListener.getIsServiceStarted() + " & isServiceBound = "+isServiceBound);
            if (mListener.getIsServiceStarted() && mListener.getIsServiceBound()){
                this.rules = mListener.getRuleSystemService().getRules();
            }
        }

        this.mAdapter = new RulesAdapter(this,rules,true);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabRules);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RulesFragment.this.getContext());
                String rulesMethodology = prefs.getString("rule_methodology_list", "2");
                Log.d("TRGRD","RulesFragment rulesMethodology = "+rulesMethodology);
                if (rulesMethodology.equals(RULE_METHODOLOGY_CONFIG)){
                    intent = new Intent(RulesFragment.this.getContext(), ShowExampleRulesActivity.class);
                } else if (rulesMethodology.equals(RULE_METHODOLOGY_TEMPLATE)){
                    intent = new Intent(RulesFragment.this.getContext(), ShowRuleTemplatesActivity.class);
                } else if (rulesMethodology.equals(RULE_METHODOLOGY_OPEN)){
                    intent = new Intent(RulesFragment.this.getContext(), CreateRuleOpenActivity.class);
                }
                if(intent != null) startActivity(intent);
            }
        });

        return view;
    }

    private void showRules(){
        if (mListener.getRuleSystemService() != null && mAdapter != null) {
            this.rules = mListener.getRuleSystemService().getRules();
            mAdapter.updateData(rules);
        }
    }

    @Override
    public void onItemClick(View view, Rule rule) {
        switch(view.getId()) {
            case R.id.tvRuleName:
            case R.id.llRuleIcons:
                Intent intent = new Intent(this.getContext(), RuleDetailsActivity.class);
                intent.putExtra("ruleid",rule.getId());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void notifyIsServiceStartedChanged(boolean isServiceStarted) {
        super.notifyIsServiceStartedChanged(isServiceStarted);
        Log.d("TRGRD","RulesFragment notify isServiceStarted = " + isServiceStarted);
        if(!isServiceStarted){
            this.rules = new HashSet<>();
            this.mAdapter.updateData(rules);
        } else {
            showRules();
        }
    }

    @Override
    public void notifyIsServiceBoundChanged(boolean isServiceBound) {
        super.notifyIsServiceBoundChanged(isServiceBound);
        Log.d("TRGRD","RulesFragment notify isServiceBound = " + isServiceBound);
    }
}
