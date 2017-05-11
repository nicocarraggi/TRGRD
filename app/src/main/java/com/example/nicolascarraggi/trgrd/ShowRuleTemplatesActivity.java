package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.RuleTemplatesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.RuleTemplate;

public class ShowRuleTemplatesActivity extends RuleSystemBindingActivity implements MyOnItemClickListener<RuleTemplate> {

    private RuleTemplatesAdapter ruleTemplatesAdapter;
    private RecyclerView.LayoutManager mLayoutManagerActions;
    private RecyclerView rvRuleTemplates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rule_templates);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Rule templates");

        rvRuleTemplates = (RecyclerView) findViewById(R.id.rvShowRuleTemplates);
    }

    @Override
    protected void onBound() {
        super.onBound();

        ruleTemplatesAdapter = new RuleTemplatesAdapter(this, ruleSystemService.getRuleTemplates());
        rvRuleTemplates.setHasFixedSize(true);
        mLayoutManagerActions = new LinearLayoutManager(this);
        rvRuleTemplates.setLayoutManager(mLayoutManagerActions);
        rvRuleTemplates.setAdapter(ruleTemplatesAdapter);

    }

    @Override
    public void onItemClick(View view, RuleTemplate item) {
        switch(view.getId()) {
            case R.id.tvRuleTemplateName:
                Intent intent = new Intent(this, RuleTemplateDetailsActivity.class);
                intent.putExtra("ruletemplateid", item.getId());
                startActivity(intent);
                break;
        }
    }

}
