package com.example.nicolascarraggi.trgrd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolascarraggi.trgrd.adapters.MyOnItemClickListener;
import com.example.nicolascarraggi.trgrd.rulesys.Device;
import com.example.nicolascarraggi.trgrd.adapters.DevicesAdapter;
import com.example.nicolascarraggi.trgrd.rulesys.DeviceManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DevicesFragment extends TrgrdFragment implements MyOnItemClickListener<Device>{

    private HashSet<Device> devices;
    private RecyclerView mRecyclerView;
    private DevicesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Refresh Broadcast Receiver

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TRGRD","DevicesFragment mReceiver "+action);

            if(action.equals(DeviceManager.DEVICES_REFRESH_ACTION)){
                showDevices();
            }
        }
    };

    // Register & UnRegister Pebble Broadcast Receiver

    public void registerDevicesReceiver(){
        IntentFilter filter = new IntentFilter(DeviceManager.DEVICES_REFRESH_ACTION);
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(mReceiver, filter);
        Log.d("TRGRD","DevicesFragment mReceiver registered!");
    }

    public void unRegisterDevicesReceiver(){
        LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(mReceiver);
        Log.d("TRGRD","DevicesFragment mReceiver unRegistered!");
    }

    public DevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        registerDevicesReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegisterDevicesReceiver();
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

        this.devices = new HashSet<>();

        if (mListener != null) {
            Log.d("TRGRD","DevicesFragment test isServiceStarted = "+mListener.getIsServiceStarted());
            if(mListener.getIsServiceStarted() && mListener.getIsServiceBound()){
                this.devices = mListener.getRuleSystemService().getDeviceManager().getDevices();
            }
        }

        mAdapter = new DevicesAdapter(this, devices);
        mRecyclerView.setAdapter(mAdapter);

        return view;

    }

    private void showDevices(){
        if (mListener.getRuleSystemService() != null && mAdapter != null && isServiceBound) {
            this.devices = mListener.getRuleSystemService().getDeviceManager().getDevices();
            mAdapter.updateData(devices);
        }
    }

    @Override
    public void notifyIsServiceStartedChanged(boolean isServiceStarted) {
        super.notifyIsServiceStartedChanged(isServiceStarted);
        Log.d("TRGRD","DevicesFragment notify isServiceStarted = " + isServiceStarted);
        this.isServiceStarted = isServiceStarted;
        if(!isServiceStarted){
            this.devices = new HashSet<>();
            this.mAdapter.updateData(devices);
        } else {
            showDevices();
        }
    }

    @Override
    public void notifyIsServiceBoundChanged(boolean isServiceBound) {
        super.notifyIsServiceBoundChanged(isServiceBound);
        this.isServiceBound = isServiceBound;
        Log.d("TRGRD","DevicesFragment notify isServiceBound = " + isServiceBound);
    }

    @Override
    public void onItemClick(View view, Device item) {
        switch(view.getId()) {
            case R.id.tvDeviceName:
                Log.d("TRGRD","DevicesFragment onItemClick tvDeviceName");
                break;
        }
    }
}
