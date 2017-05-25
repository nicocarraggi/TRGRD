package com.example.nicolascarraggi.trgrd.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {

    MyOnItemClickListener<Device> mListener;
    private ArrayList<Device> mDataset;

    public DevicesAdapter(MyOnItemClickListener<Device> listener, HashSet<Device> mDataset) {
        this.mListener = listener;
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        sort();
    }

    private void sort(){
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Device>() {
            @Override
            public int compare(Device device2, Device device1)
            {
                return  device2.getName().compareTo(device1.getName());
            }
        });
    }

    public void updateData(Set<Device> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        sort();
        notifyDataSetChanged();
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        DeviceViewHolder vh = new DeviceViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        holder.bind(mDataset.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private MyOnItemClickListener<Device> myOnItemClickListener;
        private ImageView ivDevice;
        private TextView tvDeviceName, tvDeviceStatus;
        private SwitchCompat switchDeviceActive;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            this.ivDevice = (ImageView) itemView.findViewById(R.id.ivDevice);
            this.tvDeviceName = (TextView) itemView.findViewById(R.id.tvDeviceName);
            this.tvDeviceStatus = (TextView) itemView.findViewById(R.id.tvDeviceStatus);
            this.switchDeviceActive = (SwitchCompat) itemView.findViewById(R.id.switchDeviceActive);
        }

        public void bind(Device device, MyOnItemClickListener<Device> listener){
            this.myOnItemClickListener = listener;
            this.ivDevice.setImageResource(device.getIconResource());
            this.tvDeviceName.setText(device.getName());
            this.tvDeviceStatus.setText(device.getStatus());
            tvDeviceName.setOnClickListener(this);
            switchDeviceActive.setChecked(device.isStarted());
            switchDeviceActive.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {
            myOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch(compoundButton.getId()) {
                case R.id.switchDeviceActive:
                    switchDeviceStarted(mDataset.get(getAdapterPosition()), b);
                    break;
            }
        }

        private void switchDeviceStarted(Device device, boolean b) {
            if(device.isStarted()!=b){
                if (b){
                    device.start();
                } else device.stop();
            }
        }
    }
}
