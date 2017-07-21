package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;

import com.example.nicolascarraggi.trgrd.adapters.EventsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyEventOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.MyStateOnItemClickListener;
import com.example.nicolascarraggi.trgrd.adapters.StatesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;
import com.example.nicolascarraggi.trgrd.rulesys.State;

import java.util.HashSet;
import java.util.Set;

public class AddEventOrStateActivity extends RuleSystemBindingActivity implements TrgrdFragment.OnFragmentInteractionListener {

    private boolean showEvents;

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
        setContentView(R.layout.activity_add_event_or_state);

        this.showEvents = !getIntent().getBooleanExtra("hasevent",false);

    }

    @Override
    protected void onBound() {
        super.onBound();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add trigger");
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
        // TODO needed?
        return false;
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
    public static class EventsOrStatesFragment extends TrgrdFragment implements MyEventOnItemClickListener, MyStateOnItemClickListener, SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SHOW_EVENTS = "show_events";
        private static final String ARG_SECTION_NUMBER = "section_number";

        private EventsAdapter eventsAdapter;
        private StatesAdapter statesAdapter;
        private RecyclerView.LayoutManager mLayoutManagerEvents, mLayoutManagerStates;
        private RecyclerView rvTriggers;
        private LinearLayout llAddEventOrStateEvent;
        private Button bAddEventOrStateEventOk;

        int eventsOrStates;

        public EventsOrStatesFragment() {
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_search,menu);
            MenuItem menuItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
            searchView.setOnQueryTextListener(this);
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static EventsOrStatesFragment newInstance(int sectionNumber, boolean showEvents) {
            EventsOrStatesFragment fragment = new EventsOrStatesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putBoolean(ARG_SHOW_EVENTS, showEvents);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_add_event_or_state, container, false);
            rvTriggers = (RecyclerView) rootView.findViewById(R.id.rvAddEventOrStateTriggers);
            llAddEventOrStateEvent = (LinearLayout) rootView.findViewById(R.id.llAddEventOrStateEvent);
            bAddEventOrStateEventOk = (Button) rootView.findViewById(R.id.bAddEventOrStateEventOk);
            this.eventsOrStates = getArguments().getInt(ARG_SECTION_NUMBER);
            boolean showEvents = getArguments().getBoolean(ARG_SHOW_EVENTS);
            llAddEventOrStateEvent.setVisibility(View.GONE);
            if(eventsOrStates == 1){
                // Show Events
                eventsAdapter = new EventsAdapter(this, mListener.getDeviceManager().getAllEvents(),false,false,false);
                rvTriggers.setHasFixedSize(true);
                mLayoutManagerEvents = new LinearLayoutManager(this.getContext());
                rvTriggers.setLayoutManager(mLayoutManagerEvents);
                rvTriggers.setAdapter(eventsAdapter);
                /*
                Removed because confusing for the user?

                if(!showEvents){
                    llAddEventOrStateEvent.setVisibility(View.VISIBLE);
                    bAddEventOrStateEventOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            llAddEventOrStateEvent.setVisibility(View.GONE);
                        }
                    });
                    //rvTriggers.setVisibility(View.GONE);
                }
                */
            } else if (eventsOrStates == 2){
                // Show States
                statesAdapter = new StatesAdapter(this,mListener.getDeviceManager().getAllStates(),false,false);
                rvTriggers.setHasFixedSize(true);
                // use a linear layout manager
                mLayoutManagerStates = new LinearLayoutManager(this.getContext());
                rvTriggers.setLayoutManager(mLayoutManagerStates);
                rvTriggers.setAdapter(statesAdapter);
            }

            return rootView;
        }

        @Override
        public void onItemClick(View view, Event item) {
            switch(view.getId()) {
                case R.id.tvEventName:
                    returnClickedItemId("event",item.getDevice().getId(),item.getId());
                    break;
            }
        }

        @Override
        public void onItemClick(View view, State item) {
            switch(view.getId()) {
                case R.id.tvStateName:
                    returnClickedItemId("state",item.getDevice().getId(),item.getId());
                    break;
            }
        }

        public void returnClickedItemId(String type, int deviceId, int id){
            Intent intent = new Intent();
            intent.putExtra("type", type);
            intent.putExtra("devid", deviceId);
            intent.putExtra("id", id);
            mListener.finishWithResult(intent);
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            newText = newText.toLowerCase();
            String name;
            if(eventsOrStates == 1){
                Set<Event> newEvents = new HashSet<>();
                for(Event event : mListener.getDeviceManager().getAllEvents()){
                    name = event.getName().toLowerCase();
                    if(name.contains(newText)) newEvents.add(event);
                }
                eventsAdapter.updateData(newEvents);
            } else if (eventsOrStates == 2){
                Set<State> newStates = new HashSet<>();
                for(State state : mListener.getDeviceManager().getAllStates()){
                    name = state.getName().toLowerCase();
                    if(name.contains(newText)) newStates.add(state);
                }
                statesAdapter.updateData(newStates);
            }
            return true;
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
            return EventsOrStatesFragment.newInstance(position + 1, showEvents);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Events ( IF ...)";
                case 1:
                    return "States ( WHILE ... )";
            }
            return null;
        }
    }
}
