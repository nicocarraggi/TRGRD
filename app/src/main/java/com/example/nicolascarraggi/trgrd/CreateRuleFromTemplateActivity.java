package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.TypesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.RuleTemplate;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.Type;

import java.util.HashSet;
import java.util.Set;

public class CreateRuleFromTemplateActivity extends RuleSystemBindingActivity implements MyOnItemClickListener<Type> {

    private final int ASK_LOCATION_ARRIVING = 1;
    private final int ASK_LOCATION_LEAVING = 2;
    private final int ASK_LOCATION_CURRENTLY = 3;

    private int id;
    private boolean isCreate, isBoundOnce;
    private RuleTemplate ruleTemplateInstance;
    private TypesAdapter triggerTypesAdapter;
    private TypesAdapter actionTypesAdapter;
    private RecyclerView.LayoutManager mLayoutManagerTriggers, mLayoutManagerActions;

    private Set<Event> deletedEventInstaces;
    private Set<State> deletedStateInstaces;
    private Set<Action> deletedActionInstaces;

    private EditText etName;
    private RecyclerView rvTriggers, rvActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rule_from_template);

        // Get extra int ID! IF real id (>=0), edit rule!!!
        this.isCreate = getIntent().getBooleanExtra("iscreate",true);
        this.id = getIntent().getIntExtra("ruletemplateinstanceid",2); // TODO replace with default ERROR VALUE ? (-1)

        this.etName = (EditText) findViewById(R.id.etCreateRuleFromTemplateName);
        this.rvTriggers = (RecyclerView) findViewById(R.id.rvCreateRuleFromTemplateTriggers);
        this.rvActions = (RecyclerView) findViewById(R.id.rvCreateRuleFromTemplateActions);

        this.deletedEventInstaces = new HashSet<>();
        this.deletedStateInstaces = new HashSet<>();
        this.deletedActionInstaces = new HashSet<>();

        this.isBoundOnce = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rule_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rule_save) {
            if(isRuleValid()) {
                if(isCreate) {
                    /*int newId = ruleSystemService.getNewId();
                    this.rule = new Rule(newId, etName.getText().toString(), events, states, actions);
                    ruleSystemService.addRule(rule);
                    this.rule.setActive(true);
                    Intent intent = new Intent(this, RuleDetailsOpenActivity.class);
                    intent.putExtra("ruleid",rule.getId());
                    startActivity(intent);*/
                } else {
                    //this.rule.setName(etName.getText().toString());
                    //this.rule.reset(events,states,actions);
                }
                // persist delete of instances!
                //persistDeleteInstances();
                //finish();
            } else {
                Toast.makeText(CreateRuleFromTemplateActivity.this, "A rule requires a name, 1 or more triggers and 1 or more actions!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Check if rule name is not empty AND that the rule contains at least 1 trigger and 1 action!
    private boolean isRuleValid(){
        // show explanation message
        return (!etName.getText().toString().isEmpty() && ruleTemplateInstance.isInstanceValid());
    }

    @Override
    protected void onBound() {
        super.onBound();

        // In this case, avoid refreshing the adapters when returning from the Add Trigger or Add Action!
        // Because when items were deleted from the rule (but not saved yet) and something is added, these will be shown again!
        if(!isBoundOnce) {
            ActionBar ab = getSupportActionBar();
            if (isCreate) {
                ab.setTitle("Create rule");
                ruleTemplateInstance = ruleSystemService.getRuleTemplateInstance(id);
                etName.setText(ruleTemplateInstance.getName());
            } else {
                ab.setTitle("Edit rule");
                ruleTemplateInstance = ruleSystemService.getRuleTemplateInstance(id);
                etName.setText(ruleTemplateInstance.getName());
            }

            triggerTypesAdapter = new TypesAdapter(this, ruleTemplateInstance.getTriggerTypes(), true, true);
            actionTypesAdapter = new TypesAdapter(this, ruleTemplateInstance.getActionTypes(), true, true);

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

            this.isBoundOnce = true;
        } else {
            // TODO refresh adapters!
        }

    }

    @Override
    public void onItemClick(View view, Type item) {
        // TODO
    }
}
