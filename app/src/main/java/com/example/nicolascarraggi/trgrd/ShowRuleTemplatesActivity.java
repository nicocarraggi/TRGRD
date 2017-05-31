package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.RuleTemplatesAdapter;
import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.MyTime;
import com.example.nicolascarraggi.trgrd.rulesys.RuleTemplate;

public class ShowRuleTemplatesActivity extends RuleSystemBindingActivity implements MyOnItemClickListener<RuleTemplate> {

    public static final int RULETEMPLATE_SELECT_INTENT=1;

    private RuleTemplatesAdapter ruleTemplatesAdapter;
    private RecyclerView.LayoutManager mLayoutManagerActions;
    private RecyclerView rvRuleTemplates;

    // for time logging
    private MyTime start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rule_templates);

        start = new MyTime();

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
/*                // SHOW Template details before edit!
                Intent intent = new Intent(this, RuleTemplateDetailsActivity.class);
                intent.putExtra("ruletemplateid", item.getId());
                startActivityForResult(intent,RULETEMPLATE_SELECT_INTENT);*/
                // create new ruleTemplate instance
                DeviceManager deviceManager = ruleSystemService.getDeviceManager();
                RuleTemplate instance = new RuleTemplate(deviceManager.getNewId(),item,deviceManager);
                ruleSystemService.addRuleTemplateInstance(instance);
                Intent intent = new Intent(ShowRuleTemplatesActivity.this, CreateRuleFromTemplateActivity.class);
                intent.putExtra("iscreate",true);
                intent.putExtra("ruletemplateinstanceid",instance.getId());
                startActivityForResult(intent,RULETEMPLATE_SELECT_INTENT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == RULETEMPLATE_SELECT_INTENT){
                end = new MyTime();
                MyLogger.timeframeLog("create rule from template",start,end);
                finish();
            }
        }
    }

}
