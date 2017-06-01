package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.RulesAdapter;
import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.MyTime;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;

public class ShowExampleRulesActivity extends RuleSystemBindingActivity implements MyOnItemClickListener<Rule> {

    public static final int EXAMPLERULE_SELECT_INTENT=1;

    private RulesAdapter rulesAdapter;
    private RecyclerView.LayoutManager mLayoutManagerActions;
    private RecyclerView rvExampleRules;

    // for time logging
    private MyTime start, end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_example_rules);
        start = new MyTime();
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Example rules");
        rvExampleRules = (RecyclerView) findViewById(R.id.rvShowExampleRules);
    }

    @Override
    protected void onBound() {
        super.onBound();
        rulesAdapter = new RulesAdapter(this, ruleSystemService.getExampleRules(), false);
        rvExampleRules.setHasFixedSize(true);
        mLayoutManagerActions = new LinearLayoutManager(this);
        rvExampleRules.setLayoutManager(mLayoutManagerActions);
        rvExampleRules.setAdapter(rulesAdapter);
    }

    @Override
    public void onItemClick(View view, Rule item) {
        switch(view.getId()) {
            case R.id.tvRuleTemplateName:
                DeviceManager deviceManager = ruleSystemService.getDeviceManager();
                //Rule instance = new RuleTemplate(deviceManager.getNewId(),item,deviceManager);
                //ruleSystemService.addRuleTemplateInstance(instance);
                //Intent intent = new Intent(ShowRuleTemplatesActivity.this, CreateRuleFromTemplateActivity.class);
                //intent.putExtra("iscreate",true);
                //intent.putExtra("ruletemplateinstanceid",instance.getId());
                //startActivityForResult(intent,RULETEMPLATE_SELECT_INTENT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == EXAMPLERULE_SELECT_INTENT){
                end = new MyTime();
                MyLogger.timeframeLog("create rule from example rule",start,end);
                finish();
            }
        }
    }
}
