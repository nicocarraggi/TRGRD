package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.nicolascarraggi.trgrd.adapters.ActionsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyActionOnItemClickListener;
import com.example.nicolascarraggi.trgrd.rulesys.Action;

public class AddActionActivity extends RuleSystemBindingActivity implements MyActionOnItemClickListener {

    private ActionsAdapter actionsAdapter;
    private RecyclerView.LayoutManager mLayoutManagerActions;
    private RecyclerView rvActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_action);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Add action");

        rvActions = (RecyclerView) findViewById(R.id.rvAddActionActions);

    }

    @Override
    protected void onBound() {
        super.onBound();

        actionsAdapter = new ActionsAdapter(this, ruleSystemService.getDeviceManager().getAllActions());
        rvActions.setHasFixedSize(true);
        mLayoutManagerActions = new LinearLayoutManager(this);
        rvActions.setLayoutManager(mLayoutManagerActions);
        rvActions.setAdapter(actionsAdapter);

    }

    @Override
    public void onItemClick(Action item) {
        Intent intent = new Intent();
        intent.putExtra("devid", item.getDevice().getId());
        intent.putExtra("id", item.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
