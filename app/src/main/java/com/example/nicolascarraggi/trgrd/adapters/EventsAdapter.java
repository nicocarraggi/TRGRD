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
    private MyEventOnItemClickListener mListener;

    public EventsAdapter(MyEventOnItemClickListener listener, Set<Event> mDataset) {
        this.mListener = listener;
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

    public void updateData(Set<Event> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        notifyDataSetChanged();
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
        holder.bind(mDataset.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MyEventOnItemClickListener myOnItemClickListener;
        private ImageView ivEventDevice, ivEvent;
        private TextView tvEventName;

        public EventViewHolder(View itemView) {
            super(itemView);
            this.ivEventDevice = (ImageView) itemView.findViewById(R.id.ivEventDevice);
            this.ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);
            this.tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvEventName.setOnClickListener(this);
        }

        public void bind(Event event, MyEventOnItemClickListener listener){
            this.myOnItemClickListener = listener;
            this.ivEventDevice.setImageResource(event.getDevice().getIconResource());
            this.ivEvent.setImageResource(event.getIconResource());
            this.tvEventName.setText(event.getName());
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.tvEventName:
                    myOnItemClickListener.onItemClick(mDataset.get(getAdapterPosition()));
                    break;
            }
        }
    }
}
