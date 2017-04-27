package com.example.nicolascarraggi.trgrd.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.TimeEvent;

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
    boolean mEdit;

    public EventsAdapter(MyEventOnItemClickListener listener, Set<Event> mDataset, boolean edit) {
        this.mListener = listener;
        this.mDataset = new ArrayList<>();
        this.mEdit = edit;
        this.mDataset.addAll(mDataset);
        sort();
    }

    private void sort(){
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Event>() {
            @Override
            public int compare(Event event2, Event event1)
            {
                return  event2.getName().compareTo(event1.getName());
            }
        });
    }

    public void updateData(Set<Event> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        sort();
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
        private ImageView ivEventDevice, ivEvent, ivEventDelete;
        private TextView tvEventName, tvEventValueOne;
        private Button bEventValueOne;

        public EventViewHolder(View itemView) {
            super(itemView);
            this.ivEventDevice = (ImageView) itemView.findViewById(R.id.ivEventDevice);
            this.ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);
            this.ivEventDelete = (ImageView) itemView.findViewById(R.id.ivEventDelete);
            this.tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            this.tvEventValueOne = (TextView) itemView.findViewById(R.id.tvEventValueOne);
            this.bEventValueOne = (Button) itemView.findViewById(R.id.bEventValueOne);
            tvEventName.setOnClickListener(this);
            ivEventDelete.setOnClickListener(this);
            bEventValueOne.setOnClickListener(this);
        }

        public void bind(Event event, MyEventOnItemClickListener listener){
            this.myOnItemClickListener = listener;
            this.ivEventDevice.setImageResource(event.getDevice().getIconResource());
            this.ivEvent.setImageResource(event.getIconResource());
            this.tvEventName.setText(event.getName());
            if(!mEdit && ivEventDelete != null){
                ivEventDelete.setVisibility(View.GONE);
            }
            if(event.isSkeleton()){
                // Hide unwanted views!
                if(tvEventValueOne != null) tvEventValueOne.setVisibility(View.GONE);
                if(bEventValueOne != null) bEventValueOne.setVisibility(View.GONE);
            } else if(event.isTimeEvent()){
                TimeEvent timeEvent = (TimeEvent) event;
                // TODO really needed?
                if (tvEventValueOne != null && bEventValueOne != null) {
                    tvEventValueOne.setVisibility(View.VISIBLE);
                    bEventValueOne.setVisibility(View.VISIBLE);
                    tvEventName.setVisibility(View.GONE);
                    tvEventValueOne.setText("At");
                    bEventValueOne.setText(timeEvent.getTime().toString());
                    if(!mEdit) {
                        bEventValueOne.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        }

        @Override
        public void onClick(View view) {
            myOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
