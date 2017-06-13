package com.example.nicolascarraggi.trgrd.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.InputActionEvent;
import com.example.nicolascarraggi.trgrd.rulesys.Location;
import com.example.nicolascarraggi.trgrd.rulesys.LocationEvent;
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
    boolean mEdit, mShowDelete, mShowAnd;

    public EventsAdapter(MyEventOnItemClickListener listener, Set<Event> mDataset, boolean edit, boolean showDelete, boolean showAnd) {
        this.mListener = listener;
        this.mDataset = new ArrayList<>();
        this.mEdit = edit;
        this.mShowDelete = showDelete;
        this.mShowAnd = showAnd;
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
        private TextView tvEventTypeIntro, tvEventTypeName, tvEventName, tvEventValueZero,
                            tvEventValueThree, tvEventValueThreeValue;
        private Button bEventValueZero;
        private LinearLayout llEventValueZero, llEventValueThree;

        public EventViewHolder(View itemView) {
            super(itemView);
            this.ivEventDevice = (ImageView) itemView.findViewById(R.id.ivEventDevice);
            this.ivEvent = (ImageView) itemView.findViewById(R.id.ivEvent);
            this.ivEventDelete = (ImageView) itemView.findViewById(R.id.ivEventDelete);
            this.tvEventTypeIntro = (TextView) itemView.findViewById(R.id.tvEventTypeIntro);
            this.tvEventTypeName = (TextView) itemView.findViewById(R.id.tvEventTypeName);
            this.tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            this.tvEventValueZero = (TextView) itemView.findViewById(R.id.tvEventValueZero);
            this.bEventValueZero = (Button) itemView.findViewById(R.id.bEventValueZero);
            this.tvEventValueThree = (TextView) itemView.findViewById(R.id.tvEventValueThree);
            this.tvEventValueThreeValue = (TextView) itemView.findViewById(R.id.tvEventValueThreeValue);
            this.llEventValueZero = (LinearLayout) itemView.findViewById(R.id.llEventValueZero);
            this.llEventValueThree = (LinearLayout) itemView.findViewById(R.id.llEventValueThree);
            tvEventName.setOnClickListener(this);
            ivEventDelete.setOnClickListener(this);
            bEventValueZero.setOnClickListener(this);
        }

        private void showName() {
            tvEventName.setVisibility(View.VISIBLE);
        }

        private void hideName() {
            tvEventName.setVisibility(View.GONE);
        }

        private void showValueZero() {
            llEventValueZero.setVisibility(View.VISIBLE);
        }
        private void hideValueZero(){
            llEventValueZero.setVisibility(View.GONE);
        }

        private void showValueThree() {
            llEventValueThree.setVisibility(View.VISIBLE);
        }
        private void hideValueThree(){
            llEventValueThree.setVisibility(View.GONE);
        }

        public void bind(Event event, MyEventOnItemClickListener listener){
            this.myOnItemClickListener = listener;
            this.ivEventDevice.setImageResource(event.getDevice().getIconResource());
            this.ivEvent.setImageResource(event.getIconResource());
            if(mShowAnd && getAdapterPosition()>0){
                this.tvEventTypeIntro.setText("And");
            }
            this.tvEventTypeName.setText(event.getEventType().getName());
            this.tvEventName.setText(event.getName());
            if(!mShowDelete && ivEventDelete != null){
                ivEventDelete.setVisibility(View.GONE);
            }
            hideName();
            hideValueZero();
            hideValueThree();
            if(event.isSkeleton()){
                showName();
            } else if(event.isTimeEvent()) {
                TimeEvent timeEvent = (TimeEvent) event;
                if (mEdit){
                    showValueZero();
                    tvEventValueZero.setText("At: ");
                    bEventValueZero.setText(timeEvent.getTime().toString());
                } else {
                    showValueThree();
                    tvEventValueThree.setText("At:   "+timeEvent.getTime().toString());
                    tvEventValueThreeValue.setText(timeEvent.getTime().toString());
                }
            } else if(event.isInputActionEvent()){
                InputActionEvent inputActionEvent = (InputActionEvent) event;
                if (mEdit) {
                    showValueZero();
                    tvEventValueZero.setText(inputActionEvent.getInputAction().getDescription());
                    bEventValueZero.setText(inputActionEvent.getInputAction().getName());
                } else {
                    showValueThree();
                    tvEventValueThree.setText(inputActionEvent.getInputAction().getDescription()+"   ");
                    tvEventValueThreeValue.setText(inputActionEvent.getInputAction().getName());
                }
            } else if(event.isLocationEvent()){
                LocationEvent locationEvent = (LocationEvent) event;
                Log.d("TRGRD","EventsAdapter print location event "+locationEvent.getName());String text = "";
                if(locationEvent.getLocationEventType()== LocationEvent.LocationEventType.ARRIVING){
                    text = "Arrived at:   ";
                } else {
                    text = "Left:   ";
                }
                this.ivEvent.setImageResource(locationEvent.getLocation().getIconResource());
                if(mEdit){
                    showValueZero();
                    tvEventValueZero.setText(text);
                    bEventValueZero.setText(locationEvent.getLocation().getName());
                } else {
                    showValueThree();
                    tvEventValueThree.setText(text);
                    tvEventValueThreeValue.setText(locationEvent.getLocation().getName());
                }
            }
        }

        @Override
        public void onClick(View view) {
            myOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
