package com.example.nicolascarraggi.trgrd;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

import java.util.List;

import android.Manifest;

public class MainActivity extends AppCompatActivity implements TrgrdFragment.OnFragmentInteractionListener{

    private RuleSystemService ruleSystemService;
    private boolean isServiceStarted = false;
    private boolean isServiceBound = false;
    private DeviceManager deviceManager;
    private MenuItem miRulesystemOn, miRulesystemOff;
    private boolean isOptionsMenuCreated = false;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // TODO Temporary FIXED portrait mode!!!
        // must fix rulesystemservice null bug when going landscape mode...
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        askPermission();

        startRuleSystemService();

    }

    public void askPermission(){
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1234);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.isServiceStarted = isMyServiceRunning(RuleSystemService.class);
        if(isServiceStarted){
            bindWithService();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isServiceBound) {
            unbindService(mConnection);
            this.isServiceBound = false;
            notifyFragmentsServiceBoundChange();
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            RuleSystemService.RuleSystemBinder b = (RuleSystemService.RuleSystemBinder) iBinder;
            MainActivity.this.ruleSystemService = b.getService();
            MainActivity.this.deviceManager = b.getService().getDeviceManager();
            //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            MainActivity.this.isServiceBound = true;
            MainActivity.this.notifyFragmentsServiceStartedChange();
            MainActivity.this.notifyFragmentsServiceBoundChange();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            MainActivity.this.isServiceBound = false;
            MainActivity.this.ruleSystemService = null;
            Log.d("TRGRD","MainActivity: onServiceDisconnect");
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean temp = super.onPrepareOptionsMenu(menu);
        this.isOptionsMenuCreated = true;
        if (temp) {
            this.miRulesystemOn = menu.findItem(R.id.rulesystem_on);
            this.miRulesystemOff = menu.findItem(R.id.rulesystem_off);
            showOnOff(this.isServiceStarted);
        }
        return temp;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.rulesystem_on) {
            startRuleSystemService();
        } else if (id == R.id.rulesystem_off) {
            stopRuleSystemService();
        }

        return super.onOptionsItemSelected(item);
    }

    // Fragment communication interface implementation!

    @Override
    public boolean getIsServiceStarted() {
        return isServiceStarted;
    }

    @Override
    public boolean getIsServiceBound() {
        return isServiceBound;
    }

    @Override
    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    @Override
    public RuleSystemService getRuleSystemService() {
        return ruleSystemService;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    RulesFragment rulesFragment = new RulesFragment();
                    return rulesFragment;
                case 1:
                    DevicesFragment devicesFragment = new DevicesFragment();
                    return devicesFragment;
                case 2:
                    LocationsFragment locationsFragment = new LocationsFragment();
                    return locationsFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "RULES";
                case 1:
                    return "DEVICES";
                case 2:
                    return "LOCATIONS";
            }
            return null;
        }
    }

    private void notifyFragmentsServiceStartedChange(){
        FragmentManager manager = getSupportFragmentManager();
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null) {
            TrgrdFragment tempTrgrdFragment;
            for (Fragment f : fragments) {
                tempTrgrdFragment = (TrgrdFragment) f;
                tempTrgrdFragment.notifyIsServiceStartedChanged(isServiceStarted);
            }
        }
    }

    private void notifyFragmentsServiceBoundChange(){
        FragmentManager manager = getSupportFragmentManager();
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null) {
            TrgrdFragment tempTrgrdFragment;
            for (Fragment f:fragments){
                tempTrgrdFragment = (TrgrdFragment) f;
                tempTrgrdFragment.notifyIsServiceBoundChanged(isServiceBound);
            }
        }
    }

    private boolean isAdmin(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isAdminPref = prefs.getBoolean("admin_switch", true);
        return isAdminPref;
    }

    private void showOnOff(boolean isOn){
        if(isOptionsMenuCreated) {
            if (isAdmin()) {
                if (isOn) {
                    miRulesystemOn.setVisible(false);
                    miRulesystemOff.setVisible(true);
                } else {
                    miRulesystemOn.setVisible(true);
                    miRulesystemOff.setVisible(false);
                }
            } else {
                miRulesystemOn.setVisible(false);
                miRulesystemOff.setVisible(false);
            }
        }
    }

    private void startRuleSystemService(){
        Intent ruleSystemService = new Intent(MainActivity.this, RuleSystemService.class);
        startService(ruleSystemService);
        showOnOff(true);
        this.isServiceStarted = true;
        bindWithService();
        notifyFragmentsServiceStartedChange();
    }

    private void stopRuleSystemService(){
        Intent pebbleService = new Intent(MainActivity.this, RuleSystemService.class);
        stopService(pebbleService);
        showOnOff(false);
        this.isServiceStarted = false;
        notifyFragmentsServiceStartedChange();
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
