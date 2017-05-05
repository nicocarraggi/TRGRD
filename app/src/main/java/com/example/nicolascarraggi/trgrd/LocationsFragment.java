package com.example.nicolascarraggi.trgrd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolascarraggi.trgrd.adapters.LocationsAdapter;
import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;
import com.example.nicolascarraggi.trgrd.rulesys.Location;
import com.example.nicolascarraggi.trgrd.rulesys.RuleSystemService;

import java.util.HashSet;

public class LocationsFragment extends TrgrdFragment implements MyOnItemClickListener<Location> {

    private HashSet<Location> locations;
    private RecyclerView mRecyclerView;
    private LocationsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public LocationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_locations, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvLocations);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        this.locations = new HashSet<>();

        if (mListener != null) {
            Log.d("TRGRD","LocationsFragment test isServiceStarted = "+mListener.getIsServiceStarted());
            if(mListener.getIsServiceStarted() && mListener.getIsServiceBound()){
                this.locations = mListener.getRuleSystemService().getLocations();
            }
        }

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabLocations);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationsFragment.this.getContext(), CreateLocationActivity.class);
                startActivity(intent);
            }
        });

        mAdapter = new LocationsAdapter(this, locations);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void showLocations(){
        if (mListener.getRuleSystemService() != null && mAdapter != null) {
            this.locations = mListener.getRuleSystemService().getLocations();
            mAdapter.updateData(locations);
        }
    }

    @Override
    public void notifyIsServiceStartedChanged(boolean isServiceStarted) {
        super.notifyIsServiceStartedChanged(isServiceStarted);
        Log.d("TRGRD","LocationsFragment notify isServiceStarted = " + isServiceStarted);
        if(!isServiceStarted){
            this.locations = new HashSet<>();
            this.mAdapter.updateData(locations);
        } else {
            // TODO also check if service is bound?
            showLocations();
        }
    }

    @Override
    public void notifyIsServiceBoundChanged(boolean isServiceBound) {
        super.notifyIsServiceBoundChanged(isServiceBound);
        Log.d("TRGRD","LocationsFragment notify isServiceBound = " + isServiceBound);
    }

    @Override
    public void onItemClick(View view, Location item) {
        switch(view.getId()) {
            case R.id.tvLocationName:
                Log.d("TRGRD","LocationsFragment onItemClick tvLocationName");
                Intent intent = new Intent(this.getContext(), LocationDetailsActivity.class);
                intent.putExtra("locationid",item.getId());
                startActivity(intent);
                break;
        }
    }
}
