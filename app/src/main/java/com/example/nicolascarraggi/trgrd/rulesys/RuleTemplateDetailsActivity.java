package com.example.nicolascarraggi.trgrd.rulesys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.RuleSystemBindingActivity;
import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.TypesAdapter;

public class RuleTemplateDetailsActivity extends RuleSystemBindingActivity implements MyOnItemClickListener<Type> {

    private int ruleTemplateId;
    private RuleTemplate ruleTemplate;
    private TypesAdapter triggerTypesAdapter;
    private TypesAdapter actionTypesAdapter;
    private RecyclerView.LayoutManager mLayoutManagerTriggers, mLayoutManagerActions;

    private TextView tvName;
    private RecyclerView rvTriggers, rvActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_template_details);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Rule template");

        Intent intent = getIntent();
        this.ruleTemplateId = intent.getIntExtra("ruletemplateid",0);

        tvName = (TextView) findViewById(R.id.tvRuleTemplateDetailsName);
        rvTriggers = (RecyclerView) findViewById(R.id.rvRuleTemplateDetailsTriggers);
        rvActions = (RecyclerView) findViewById(R.id.rvRuleTemplateDetailsActions);

    }

    @Override
    protected void onBound(){
        super.onBound();
        ruleTemplate = ruleSystemService.getRuleTemplate(ruleTemplateId);

        tvName.setText(ruleTemplate.getName());

        triggerTypesAdapter = new TypesAdapter(this,ruleTemplate.getTriggerTypes(),false);
        actionTypesAdapter = new TypesAdapter(this,ruleTemplate.getActionTypes(),false);

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
    }

    @Override
    public void onItemClick(View view, Type item) {

    }
}
