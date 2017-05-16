package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.TypesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.RuleTemplate;
import com.example.nicolascarraggi.trgrd.rulesys.Type;

public class RuleTemplateDetailsActivity extends RuleSystemBindingActivity implements MyOnItemClickListener<Type> {

    public static final int RULETEMPLATE_DETAILS_INTENT=1;

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
        this.ruleTemplateId = intent.getIntExtra("ruletemplateid",0); // TODO replace with default ERROR VALUE ? (-1)

        tvName = (TextView) findViewById(R.id.tvRuleTemplateDetailsName);
        rvTriggers = (RecyclerView) findViewById(R.id.rvRuleTemplateDetailsTriggers);
        rvActions = (RecyclerView) findViewById(R.id.rvRuleTemplateDetailsActions);

    }

    @Override
    protected void onBound(){
        super.onBound();
        ruleTemplate = ruleSystemService.getRuleTemplate(ruleTemplateId);

        tvName.setText(ruleTemplate.getName());

        triggerTypesAdapter = new TypesAdapter(this,ruleTemplate.getTriggerTypes(),false,false);
        actionTypesAdapter = new TypesAdapter(this,ruleTemplate.getActionTypes(),false,false);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ruletemplate_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ruletemplate_select) {
            // create new ruleTemplate instance
            DeviceManager deviceManager = ruleSystemService.getDeviceManager();
            RuleTemplate instance = new RuleTemplate(deviceManager.getNewId(),ruleTemplate,deviceManager);
            ruleSystemService.addRuleTemplateInstance(instance);
            Intent intent = new Intent(RuleTemplateDetailsActivity.this, CreateRuleFromTemplateActivity.class);
            intent.putExtra("iscreate",true);
            intent.putExtra("ruletemplateinstanceid",instance.getId());
            startActivityForResult(intent,RULETEMPLATE_DETAILS_INTENT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == RULETEMPLATE_DETAILS_INTENT){
                Intent backIntent = new Intent();
                setResult(RESULT_OK, backIntent);
                finish();
            }
        }
    }

    @Override
    public void onItemClick(View view, Type item) {

    }
}
