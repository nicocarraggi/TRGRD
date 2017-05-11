package com.example.nicolascarraggi.trgrd.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
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
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.ActionType;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.EventType;
import com.example.nicolascarraggi.trgrd.rulesys.LocationEvent;
import com.example.nicolascarraggi.trgrd.rulesys.LocationState;
import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.StateType;
import com.example.nicolascarraggi.trgrd.rulesys.TimeEvent;
import com.example.nicolascarraggi.trgrd.rulesys.TimeState;
import com.example.nicolascarraggi.trgrd.rulesys.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.TypeViewHolder> {

    private ArrayList<Type> mDataset;
    private MyOnItemClickListener mListener;
    boolean mEdit, mShowInstance;

    public TypesAdapter(MyOnItemClickListener listener, ArrayList<Type> mDataset, boolean edit, boolean showInstance) {
        this.mListener = listener;
        this.mDataset = mDataset;
        this.mEdit = edit;
        this.mShowInstance = showInstance;
        sort();
    }

    private void sort(){
        // TODO put Events above States
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Type>() {
            @Override
            public int compare(Type type2, Type type1)
            {
                return  type2.getName().compareTo(type1.getName());
            }
        });
    }

    public void updateData(Set<Type> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        sort();
        notifyDataSetChanged();
    }

    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.type_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        TypeViewHolder vh = new TypeViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TypeViewHolder holder, int position) {
        holder.bind(mDataset.get(position), mListener, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class TypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MyOnItemClickListener<Type> myOnItemClickListener;
        private TextView tvTypeName, tvTypeIntro, tvTypeInstanceType, tvTypeInstanceName,
                            tvTypeInstanceValueOne, tvTypeInstanceValueTwo;
        private ImageView ivTypeInstanceDevice,ivTypeInstance,ivTypeInstanceReplace;
        private Button bTypeInstanceValueOne, bTypeInstanceValueTwo;
        private LinearLayout llType, llTypeInstance, llTypeInstanceType;

        public TypeViewHolder(View itemView) {
            super(itemView);
            this.tvTypeName = (TextView) itemView.findViewById(R.id.tvTypeName);
            this.tvTypeIntro = (TextView) itemView.findViewById(R.id.tvTypeIntro);
            this.tvTypeInstanceType = (TextView) itemView.findViewById(R.id.tvTypeInstanceType);
            this.tvTypeInstanceName = (TextView) itemView.findViewById(R.id.tvTypeInstanceName);
            this.tvTypeInstanceValueOne = (TextView) itemView.findViewById(R.id.tvTypeInstanceValueOne);
            this.tvTypeInstanceValueTwo = (TextView) itemView.findViewById(R.id.tvTypeInstanceValueTwo);
            this.ivTypeInstanceDevice = (ImageView) itemView.findViewById(R.id.ivTypeInstanceDevice);
            this.ivTypeInstance = (ImageView) itemView.findViewById(R.id.ivTypeInstance);
            this.ivTypeInstanceReplace = (ImageView) itemView.findViewById(R.id.ivTypeInstanceReplace);
            this.bTypeInstanceValueOne = (Button) itemView.findViewById(R.id.bTypeInstanceValueOne);
            this.bTypeInstanceValueTwo = (Button) itemView.findViewById(R.id.bTypeInstanceValueTwo);
            this.llType = (LinearLayout) itemView.findViewById(R.id.llType);
            this.llTypeInstance = (LinearLayout) itemView.findViewById(R.id.llTypeInstance);
            this.llTypeInstanceType = (LinearLayout) itemView.findViewById(R.id.llTypeInstanceType);
            tvTypeName.setOnClickListener(this);
            tvTypeInstanceName.setOnClickListener(this);
            ivTypeInstanceReplace.setOnClickListener(this);
            bTypeInstanceValueOne.setOnClickListener(this);
            bTypeInstanceValueTwo.setOnClickListener(this);
        }

        private void hideInstanceName(){
            tvTypeInstanceName.setVisibility(View.GONE);
        }

        private void hideInstanceValueOne(){
            tvTypeInstanceValueOne.setVisibility(View.GONE);
            bTypeInstanceValueOne.setVisibility(View.GONE);
        }

        private void hideInstanceValueTwo(){
            tvTypeInstanceValueTwo.setVisibility(View.GONE);
            bTypeInstanceValueTwo.setVisibility(View.GONE);
        }

        private void hideInstanceType(){
            llTypeInstanceType.setVisibility(View.GONE);
        }

        private void setViewHasInstance(boolean hasInstance){
            if(hasInstance){
                tvTypeInstanceName.setTypeface(null, Typeface.NORMAL);
                ivTypeInstanceReplace.setImageResource(R.drawable.ic_autorenew_black_24dp);
                llTypeInstanceType.setVisibility(View.VISIBLE); // TODO throws null pointer if visibity was GONE?
            } else {
                tvTypeInstanceName.setTypeface(null, Typeface.ITALIC);
                ivTypeInstanceReplace.setImageResource(R.drawable.ic_add_black_24dp);
                llTypeInstanceType.setVisibility(View.GONE);
            }
        }

        public void bind(Type type, MyOnItemClickListener listener, int position){
            this.myOnItemClickListener = listener;
            this.tvTypeName.setText(type.getName());
            if(!mShowInstance){
                llTypeInstance.setVisibility(View.GONE);
            }
            if(!mEdit){
                ivTypeInstanceReplace.setVisibility(View.GONE);
            }
            if(type.isEventType()){
                // Set Type information
                tvTypeIntro.setText("When");
                //llType.setBackgroundResource(R.color.colorEvent);
                // Set TypeInstance information
                llTypeInstance.setBackgroundResource(R.color.colorEvent);
                Event event = ((EventType) type).getInstanceEvent();
                if(event == null){
                    tvTypeInstanceName.setText("Add an event of this type!");
                    setViewHasInstance(false);
                    hideInstanceValueOne();
                    hideInstanceValueTwo();
                } else {
                    ivTypeInstanceDevice.setImageResource(event.getDevice().getIconResource());
                    ivTypeInstance.setImageResource(event.getIconResource());
                    tvTypeInstanceName.setText(event.getName());
                    setViewHasInstance(true);
                    if(event.isSkeleton()){
                        hideInstanceValueOne();
                        hideInstanceValueTwo();
                    } else if(event.isTimeEvent()) {
                        TimeEvent timeEvent = (TimeEvent) event;
                        hideInstanceName();
                        hideInstanceValueTwo();
                        tvTypeInstanceValueOne.setText("At");
                        bTypeInstanceValueOne.setText(timeEvent.getTime().toString());
                        if (!mEdit) {
                            bTypeInstanceValueOne.setBackgroundColor(Color.TRANSPARENT);
                        }
                    } else if(event.isLocationEvent()){
                        LocationEvent locationEvent = (LocationEvent) event;
                        hideInstanceName();
                        hideInstanceValueTwo();
                        String text = "";
                        if(locationEvent.getLocationEventType()== LocationEvent.LocationEventType.ARRIVING){
                            text = "Arriving at: ";
                        } else {
                            text = "Leaving: ";
                        }
                        tvTypeInstanceValueOne.setText(text);
                        bTypeInstanceValueOne.setText(locationEvent.getLocation().getName());
                        if(!mEdit) bTypeInstanceValueOne.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            } else if(type.isStateType()){
                // Set Type information
                tvTypeIntro.setText("While");
                //llType.setBackgroundResource(R.color.colorState);
                // Set TypeInstance information
                llTypeInstance.setBackgroundResource(R.color.colorState);
                State state = ((StateType) type).getInstanceState();
                if(state == null){
                    tvTypeInstanceName.setText("Add a state of this type!");
                    setViewHasInstance(false);
                    hideInstanceValueOne();
                    hideInstanceValueTwo();
                } else {
                    ivTypeInstanceDevice.setImageResource(state.getDevice().getIconResource());
                    ivTypeInstance.setImageResource(state.getIconResource());
                    tvTypeInstanceName.setText(state.getName());
                    setViewHasInstance(true);
                    if (state.isSkeleton()) {
                        hideInstanceValueOne();
                        hideInstanceValueTwo();
                    } else if (state.isTimeState()) {
                        TimeState timeState = (TimeState) state;
                        if (mEdit) {
                            hideInstanceName();
                            tvTypeInstanceValueOne.setText("From");
                            tvTypeInstanceValueTwo.setText("To");
                            bTypeInstanceValueOne.setText(timeState.getTimeFrom().toString());
                            bTypeInstanceValueTwo.setText(timeState.getTimeTo().toString());
                        } else {
                            tvTypeInstanceName.setText("From   " + timeState.getTimeFrom().toString() + "   to   " + timeState.getTimeTo().toString());
                            hideInstanceValueOne();
                            hideInstanceValueTwo();
                        }
                    } else if (state.isLocationState()) {
                        LocationState locationState = (LocationState) state;
                        if (mEdit) {
                            hideInstanceName();
                            hideInstanceValueTwo();
                            tvTypeInstanceValueOne.setText("Currently at: ");
                            bTypeInstanceValueOne.setText(locationState.getLocation().getName());
                        } else {
                            tvTypeInstanceName.setText("Currently at:   " + locationState.getLocation().getName());
                            hideInstanceValueOne();
                            hideInstanceValueTwo();
                        }
                    }
                }
            } else if (type.isActionType()){
                // Set Type information
                // First item show "Then"
                // TODO Other items show "And"
                tvTypeIntro.setText("Then");
                //llType.setBackgroundResource(R.color.colorAction);
                // Set TypeInstance information
                llTypeInstance.setBackgroundResource(R.color.colorAction);
                Action action = ((ActionType) type).getInstanceAction();
                if(action == null){
                    tvTypeInstanceName.setText("Add an action of this type!");
                    setViewHasInstance(false);
                    hideInstanceValueOne();
                    hideInstanceValueTwo();
                } else {
                    ivTypeInstanceDevice.setImageResource(action.getDevice().getIconResource());
                    ivTypeInstance.setImageResource(action.getIconResource());
                    tvTypeInstanceName.setText(action.getName());
                    setViewHasInstance(true);
                    if(action.isSkeleton()){
                        hideInstanceValueOne();
                        hideInstanceValueTwo();
                    } else if (action.isNotificationAction()){
                        NotificationAction notificationAction = (NotificationAction) action;
                        hideInstanceName();
                        hideInstanceValueTwo();
                        tvTypeInstanceValueOne.setText("Notify:");
                        bTypeInstanceValueOne.setText(notificationAction.getTitle());
                        if(!mEdit) bTypeInstanceValueOne.setBackgroundColor(Color.TRANSPARENT);
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
