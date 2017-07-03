package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.adapters.ActionsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.EventsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyActionOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyEventOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyStateOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.StatesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.devices.Pebble;

import java.util.HashSet;
import java.util.Set;

public class DeviceDetailsActivity extends RuleSystemBindingActivity implements TrgrdFragment.OnFragmentInteractionListener {

    private int deviceId;
    private Device device;

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
        setContentView(R.layout.activity_device_details);

        this.deviceId = getIntent().getIntExtra("deviceid",0);



    }

    @Override
    protected void onBound() {
        super.onBound();

        device = ruleSystemService.getDeviceManager().getDevice(deviceId);
        setTitle(device.getName() + " details");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean getIsServiceStarted() {
        // TODO needed?
        return false;
    }

    @Override
    public boolean getIsServiceBound() {
        return isServiceBound;
    }

    @Override
    public DeviceManager getDeviceManager() {
        return ruleSystemService.getDeviceManager();
    }

    @Override
    public RuleSystemService getRuleSystemService() {
        return ruleSystemService;
    }

    @Override
    public void finishWithResult(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends TrgrdFragment implements MyEventOnItemClickListener, MyStateOnItemClickListener, MyActionOnItemClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_DEVICE_ID = "device_id";

        private int eventsStatesOrActions;
        private int deviceId;
        private Device device;

        private EventsAdapter eventsAdapter;
        private StatesAdapter statesAdapter;
        private ActionsAdapter actionsAdapter;
        private RecyclerView.LayoutManager mLayoutManagerEvents, mLayoutManagerStates;
        private RecyclerView rvDeviceDetails;

        private void fillData(){
            if(mListener.getIsServiceBound()){
                device = mListener.getDeviceManager().getDevice(deviceId);
                if (device != null){
                    if(eventsStatesOrActions == 1){
                        // Show Events
                        Set<Event> events = new HashSet<>();
                        events.addAll(device.getEvents().values());
                        eventsAdapter = new EventsAdapter(this,events,false,false,false);
                        rvDeviceDetails.setHasFixedSize(true);
                        mLayoutManagerEvents = new LinearLayoutManager(this.getContext());
                        rvDeviceDetails.setLayoutManager(mLayoutManagerEvents);
                        rvDeviceDetails.setAdapter(eventsAdapter);
                    } else if (eventsStatesOrActions == 2){
                        // Show States
                        Set<State> states = new HashSet<>();
                        states.addAll(device.getStates().values());
                        statesAdapter = new StatesAdapter(this,states,false,false);
                        rvDeviceDetails.setHasFixedSize(true);
                        // use a linear layout manager
                        mLayoutManagerStates = new LinearLayoutManager(this.getContext());
                        rvDeviceDetails.setLayoutManager(mLayoutManagerStates);
                        rvDeviceDetails.setAdapter(statesAdapter);
                    } else if (eventsStatesOrActions == 3){
                        // Show Actions
                        Set<Action> actions = new HashSet<>();
                        actions.addAll(device.getActions().values());
                        actionsAdapter = new ActionsAdapter(this,actions,false,false);
                        rvDeviceDetails.setHasFixedSize(true);
                        // use a linear layout manager
                        mLayoutManagerStates = new LinearLayoutManager(this.getContext());
                        rvDeviceDetails.setLayoutManager(mLayoutManagerStates);
                        rvDeviceDetails.setAdapter(actionsAdapter);
                    }
                }
            }
        }

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, int deviceId) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(ARG_DEVICE_ID, deviceId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            eventsStatesOrActions = getArguments().getInt(ARG_SECTION_NUMBER);
            deviceId = getArguments().getInt(ARG_DEVICE_ID);
            if(eventsStatesOrActions<4){
                rootView = inflater.inflate(R.layout.fragment_device_details, container, false);
                rvDeviceDetails = (RecyclerView) rootView.findViewById(R.id.rvDeviceDetails);
                fillData();
            } else {
                rootView = inflater.inflate(R.layout.fragment_device_details_settings, container, false);
                device = mListener.getDeviceManager().getDevice(deviceId);
                if (device.getName().equals("Pebble Watch")){
                    rootView = inflater.inflate(R.layout.fragment_pebble_details_settings, container, false);
                    final TextView tvScore = (TextView) rootView.findViewById(R.id.tvPebbleSettingsScore);
                    tvScore.setText(((Pebble) device).getScore().toScoreString());
                    Button bResetScore = (Button) rootView.findViewById(R.id.bPebbleSettingsResetScore);
                    bResetScore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((Pebble) device).setScore(0,0);
                            tvScore.setText(((Pebble) device).getScore().toScoreString());
                        }
                    });
                }
            }
            return rootView;
        }

        @Override
        public void notifyIsServiceBoundChanged(boolean isServiceBound) {
            super.notifyIsServiceBoundChanged(isServiceBound);
            if(isServiceBound) fillData();
        }

        @Override
        public void onItemClick(View view, Event item) {
            switch(view.getId()) {
                case R.id.tvEventName:
                    break;
            }
        }

        @Override
        public void onItemClick(View view, State item) {
            switch(view.getId()) {
                case R.id.tvStateName:
                    break;
            }
        }

        @Override
        public void onItemClick(View view, Action item) {
            switch(view.getId()) {
                case R.id.tvActionName:
                    break;
            }
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, deviceId);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Events\n( IF ...)";
                case 1:
                    return "States\n( WHILE ... )";
                case 2:
                    return "Actions\n( THEN ... )";
                case 3:
                    return "Settings\n            ";
            }
            return null;
        }
    }
}
