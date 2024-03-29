package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.RuleTemplatesAdapter;
import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.MyTime;
import com.example.nicolascarraggi.trgrd.rulesys.RuleTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ShowRuleTemplatesActivity extends RuleSystemBindingActivity implements MyOnItemClickListener<RuleTemplate>, SearchView.OnQueryTextListener {

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

        this.ruleTemplatesAdapter = new RuleTemplatesAdapter(this, ruleSystemService.getRuleManager().getRuleTemplates());
        rvRuleTemplates.setHasFixedSize(true);
        this.mLayoutManagerActions = new LinearLayoutManager(this);
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
                ruleSystemService.getRuleManager().addRuleTemplateInstance(instance);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ruletemplates,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search_ruletemplates);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        Set<RuleTemplate> newRuleTemplates = new HashSet<>();
        String name;
        for(RuleTemplate rt : ruleSystemService.getRuleManager().getRuleTemplates()){
            name = rt.getName().toLowerCase();
            if(name.contains(newText)) newRuleTemplates.add(rt);
        }
        ruleTemplatesAdapter.updateData(newRuleTemplates);
        return true;
    }
}
