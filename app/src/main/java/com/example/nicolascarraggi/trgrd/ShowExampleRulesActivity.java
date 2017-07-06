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
import com.example.nicolascarraggi.trgrd.adapters.RulesAdapter;
import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.MyTime;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;

import java.util.HashSet;
import java.util.Set;

public class ShowExampleRulesActivity extends RuleSystemBindingActivity implements MyOnItemClickListener<Rule>, SearchView.OnQueryTextListener {

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
            case R.id.tvRuleName:
                DeviceManager deviceManager = ruleSystemService.getDeviceManager();
                Intent intent = new Intent(ShowExampleRulesActivity.this, CreateRuleActivity.class);
                intent.putExtra("iscreate",true);
                intent.putExtra("isfromexamplerule",true);
                intent.putExtra("ruleid",item.getId());
                startActivityForResult(intent,EXAMPLERULE_SELECT_INTENT);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_examplerules,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search_examplerules);
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
        Set<Rule> newRules = new HashSet<>();
        String name;
        for(Rule rule : ruleSystemService.getExampleRules()){
            name = rule.getName().toLowerCase();
            if(name.contains(newText)) newRules.add(rule);
        }
        rulesAdapter.updateData(newRules);
        return true;
    }
}
