package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.nicolascarraggi.trgrd.adapters.ActionsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyActionOnItemClickListener;
import com.example.nicolascarraggi.trgrd.rulesys.Action;

import java.util.HashSet;
import java.util.Set;

public class AddActionActivity extends RuleSystemBindingActivity implements MyActionOnItemClickListener, SearchView.OnQueryTextListener {

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

        actionsAdapter = new ActionsAdapter(this, ruleSystemService.getDeviceManager().getAllActions(), false, false);
        rvActions.setHasFixedSize(true);
        mLayoutManagerActions = new LinearLayoutManager(this);
        rvActions.setLayoutManager(mLayoutManagerActions);
        rvActions.setAdapter(actionsAdapter);

    }

    @Override
    public void onItemClick(View view, Action item) {
        switch(view.getId()) {
            case R.id.tvActionName:
                Intent intent = new Intent();
                intent.putExtra("devid", item.getDevice().getId());
                intent.putExtra("id", item.getId());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
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
        Set<Action> newActions = new HashSet<>();
        String name;
        for(Action action : ruleSystemService.getDeviceManager().getAllActions()){
            name = action.getName().toLowerCase();
            if(name.contains(newText)) newActions.add(action);
        }
        actionsAdapter.updateData(newActions);
        return true;
    }
}
