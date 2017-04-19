package com.example.nicolascarraggi.trgrd.rulesys;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder> {


    private ArrayList<Device> mDataset;

    public DevicesAdapter(Set<Device> mDataset) {
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Device>() {
            @Override
            public int compare(Device device2, Device device1)
            {
                return  device1.getName().compareTo(device2.getName());
            }
        });
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
        final Device device = mDataset.get(position);
        holder.ivDevice.setImageResource(device.getIconResource());
        holder.tvDeviceName.setText(device.getName());
        //holder.switchDeviceActive.setChecked(device.isActive());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivDevice;
        public TextView tvDeviceName;
        public SwitchCompat switchDeviceActive;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            this.ivDevice = (ImageView) itemView.findViewById(R.id.ivDevice);
            this.tvDeviceName = (TextView) itemView.findViewById(R.id.tvDeviceName);
            this.switchDeviceActive = (SwitchCompat) itemView.findViewById(R.id.switchDeviceActive);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
