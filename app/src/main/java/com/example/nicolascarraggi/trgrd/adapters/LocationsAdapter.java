package com.example.nicolascarraggi.trgrd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

    MyOnItemClickListener<Location> mListener;
    private ArrayList<Location> mDataset;

    public LocationsAdapter(MyOnItemClickListener<Location> listener, HashSet<Location> mDataset) {
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        this.mListener = listener;
    }

    private void sort(){
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Location>() {
            @Override
            public int compare(Location location2, Location location1)
            {
                return  location2.getName().compareTo(location1.getName());
            }
        });
    }

    public void updateData(Set<Location> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        sort();
        notifyDataSetChanged();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        LocationsAdapter.LocationViewHolder vh = new LocationsAdapter.LocationViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        holder.bind(mDataset.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MyOnItemClickListener<Location> myOnItemClickListener;
        private ImageView ivLocation;
        private TextView tvLocationName;

        public LocationViewHolder(View itemView) {
            super(itemView);
            this.ivLocation = (ImageView) itemView.findViewById(R.id.ivLocation);
            this.tvLocationName = (TextView) itemView.findViewById(R.id.tvLocationName);
        }

        public void bind(Location location, MyOnItemClickListener<Location> listener){
            this.myOnItemClickListener = listener;
            this.ivLocation.setImageResource(location.getIconResource());
            this.tvLocationName.setText(location.getName());
            tvLocationName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
