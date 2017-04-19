package com.example.nicolascarraggi.trgrd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolascarraggi.trgrd.rulesys.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {


    private ArrayList<Location> mDataset;

    public LocationsAdapter(Set<Location> mDataset) {
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Location>() {
            @Override
            public int compare(Location location2, Location location1)
            {
                return  location1.getName().compareTo(location2.getName());
            }
        });
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO implement!!
        return null;
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        // TODO implement!!
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public LocationViewHolder(View itemView) {
            // TODO implement!!
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            // TODO implement!!
        }
    }
}
