package com.example.nicolascarraggi.trgrd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    private ArrayList<Event> mDataset;

    public EventsAdapter(Set<Event> mDataset) {
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Event>() {
            @Override
            public int compare(Event event2, Event event1)
            {
                return  event1.getName().compareTo(event2.getName());
            }
        });
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        EventViewHolder vh = new EventViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        final Event event = mDataset.get(position);
        holder.ivEventDevice.setImageResource(event.getDevice().getIconResource());
        holder.ivEvent.setImageResource(event.getIconResource());
        holder.tvEventName.setText(event.getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivEventDevice, ivEvent;
        public TextView tvEventName;

        public EventViewHolder(View itemView) {
            super(itemView);
            this.ivEventDevice = (ImageView) itemView.findViewById(R.id.ivEventDevice);
            this.ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);
            this.tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
