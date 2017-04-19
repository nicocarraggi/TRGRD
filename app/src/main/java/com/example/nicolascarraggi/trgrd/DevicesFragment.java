package com.example.nicolascarraggi.trgrd;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.adapters.DevicesAdapter;

import java.util.HashMap;
import java.util.Set;

public class DevicesFragment extends TrgrdFragment implements MyOnItemClickListener<Device>{

    private HashMap<Integer,Device> devices;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public DevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_devices, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvDevices);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (mListener != null) {
            Log.d("TRGRD","DevicesFragment test isServiceStarted = "+mListener.getIsServiceStarted());
            if (isServiceBound) showDevices();
        }

        return view;

    }

    private void showDevices(){
        this.devices = mListener.getDeviceManager().getDevices();
        mAdapter = new DevicesAdapter(this, devices);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(Device item) {

    }

    @Override
    public void notifyIsServiceStartedChanged(boolean isServiceStarted) {
        super.notifyIsServiceStartedChanged(isServiceStarted);
        Log.d("TRGRD","DevicesFragment notify isServiceStarted = " + isServiceStarted);
    }

    @Override
    public void notifyIsServiceBoundChanged(boolean isServiceBound) {
        super.notifyIsServiceBoundChanged(isServiceBound);
        Log.d("TRGRD","DevicesFragment notify isServiceBound = " + isServiceBound);
        if (isServiceBound) showDevices();
    }
}
