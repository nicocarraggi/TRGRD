package com.example.nicolascarraggi.trgrd;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

import java.util.Set;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RuleSystemService ruleSystemService;
    boolean isServiceStarted = false;
    boolean isServiceBound = false;
    DeviceManager deviceManager;

    Button bStart, bStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.bStart = (Button) findViewById(R.id.bStart);
        this.bStop = (Button) findViewById(R.id.bStop);

        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isServiceStarted){
                    startRuleSystemService();
                }
            }
        });

        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServiceStarted){
                    stopRuleSystemService();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.isServiceStarted = isMyServiceRunning(RuleSystemService.class);


        // disable corresponding service button
        if(isServiceStarted){
            this.bStart.setEnabled(false);
            this.bStop.setEnabled(true);
            bindWithService();

        } else {
            this.bStart.setEnabled(true);
            this.bStop.setEnabled(false);
            //this.isServiceBound = false;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isServiceBound) {
            unbindService(mConnection);
            this.isServiceBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RuleSystemService.RuleSystemBinder b = (RuleSystemService.RuleSystemBinder) iBinder;
            HomeActivity.this.ruleSystemService = b.getService();
            Toast.makeText(HomeActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            HomeActivity.this.isServiceBound = true;
            HomeActivity.this.deviceManager = b.getService().getDeviceManager();
            Set<Rule> rules = b.getService().getRules();
            Log.d("TRGRD","HomeActivity: rules from service size = "+rules.size());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            HomeActivity.this.isServiceBound = false;
            HomeActivity.this.ruleSystemService = null;
            Log.d("TRGRD","HomeActivity: onServiceDisconnect");
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rules) {
            // Handle the camera action
        } else if (id == R.id.nav_devices) {

        } else if (id == R.id.nav_locations) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startRuleSystemService(){
        Intent pebbleService = new Intent(HomeActivity.this, RuleSystemService.class);
        startService(pebbleService);
        this.bStart.setEnabled(false);
        this.bStop.setEnabled(true);
        this.isServiceStarted = true;
        bindWithService();
    }

    private void stopRuleSystemService(){
        Intent pebbleService = new Intent(HomeActivity.this, RuleSystemService.class);
        stopService(pebbleService);
        this.bStart.setEnabled(true);
        this.bStop.setEnabled(false);
        this.isServiceStarted = false;
    }

    private void bindWithService(){
        // BIND this activity to the service to communicate with it!
        Intent intent= new Intent(this, RuleSystemService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    // Solution from StackOverflow (http://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android)
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d("TRGRD", "isMyServiceRunning TRUE");
                return true;
            }
        }
        Log.d("TRGRD", "isMyServiceRunning FALSE");
        return false;
    }

}
