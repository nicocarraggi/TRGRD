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
import com.example.nicolascarraggi.trgrd.rulesys.InputActionEvent;
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
                            tvTypeInstanceValueZero, tvTypeInstanceValueOne, tvTypeInstanceValueTwo;
        private ImageView ivTypeInstanceDevice,ivTypeInstance,ivTypeInstanceReplace;
        private Button bTypeInstanceValueZero, bTypeInstanceValueOne, bTypeInstanceValueTwo;
        private LinearLayout llType, llTypeInstance,llTypeInstanceItem, llTypeInstanceType, llTypeInstanceValueZero,
                                llTypeInstanceValueOneTwo;

        public TypeViewHolder(View itemView) {
            super(itemView);
            this.tvTypeName = (TextView) itemView.findViewById(R.id.tvTypeName);
            this.tvTypeIntro = (TextView) itemView.findViewById(R.id.tvTypeIntro);
            this.tvTypeInstanceType = (TextView) itemView.findViewById(R.id.tvTypeInstanceType);
            this.tvTypeInstanceName = (TextView) itemView.findViewById(R.id.tvTypeInstanceName);
            this.tvTypeInstanceValueZero = (TextView) itemView.findViewById(R.id.tvTypeInstanceValueZero);
            this.tvTypeInstanceValueOne = (TextView) itemView.findViewById(R.id.tvTypeInstanceValueOne);
            this.tvTypeInstanceValueTwo = (TextView) itemView.findViewById(R.id.tvTypeInstanceValueTwo);
            this.ivTypeInstanceDevice = (ImageView) itemView.findViewById(R.id.ivTypeInstanceDevice);
            this.ivTypeInstance = (ImageView) itemView.findViewById(R.id.ivTypeInstance);
            this.ivTypeInstanceReplace = (ImageView) itemView.findViewById(R.id.ivTypeInstanceReplace);
            this.bTypeInstanceValueZero = (Button) itemView.findViewById(R.id.bTypeInstanceValueZero);
            this.bTypeInstanceValueOne = (Button) itemView.findViewById(R.id.bTypeInstanceValueOne);
            this.bTypeInstanceValueTwo = (Button) itemView.findViewById(R.id.bTypeInstanceValueTwo);
            this.llType = (LinearLayout) itemView.findViewById(R.id.llType);
            this.llTypeInstance = (LinearLayout) itemView.findViewById(R.id.llTypeInstance);
            this.llTypeInstanceItem = (LinearLayout) itemView.findViewById(R.id.llTypeInstanceItem);
            this.llTypeInstanceType = (LinearLayout) itemView.findViewById(R.id.llTypeInstanceType);
            this.llTypeInstanceValueZero = (LinearLayout) itemView.findViewById(R.id.llTypeInstanceValueZero);
            this.llTypeInstanceValueOneTwo = (LinearLayout) itemView.findViewById(R.id.llTypeInstanceValueOneTwo);
            tvTypeName.setOnClickListener(this);
            tvTypeInstanceName.setOnClickListener(this);
            ivTypeInstanceReplace.setOnClickListener(this);
            bTypeInstanceValueZero.setOnClickListener(this);
            bTypeInstanceValueOne.setOnClickListener(this);
            bTypeInstanceValueTwo.setOnClickListener(this);
        }

        private void showInstanceName(){
            tvTypeInstanceName.setVisibility(View.VISIBLE);
        }
        private void hideInstanceName(){
            tvTypeInstanceName.setVisibility(View.GONE);
        }

        private void showInstanceType(){
            llTypeInstanceType.setVisibility(View.VISIBLE);
        }
        private void hideInstanceType(){
            llTypeInstanceType.setVisibility(View.GONE);
        }

        private void showInstanceValueZero() {
            llTypeInstanceValueZero.setVisibility(View.VISIBLE);
        }
        private void hideInstanceValueZero(){
            llTypeInstanceValueZero.setVisibility(View.GONE);
        }

        private void showInstanceValueOneTwo(){
            llTypeInstanceValueOneTwo.setVisibility(View.VISIBLE);
        }
        private void hideInstanceValueOneTwo(){
            llTypeInstanceValueOneTwo.setVisibility(View.GONE);
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
                tvTypeIntro.setText("If");
                //llType.setBackgroundResource(R.color.colorEvent);
                // Set TypeInstance information
                llTypeInstanceItem.setBackgroundResource(R.color.colorEvent);
                Event event = ((EventType) type).getInstanceEvent();
                if(event == null){
                    tvTypeInstanceName.setText("Add an event of this type!");
                    setViewHasInstance(false);
                    showInstanceName();
                    hideInstanceValueZero();
                    hideInstanceValueOneTwo();
                } else {
                    tvTypeInstanceType.setText("Event");
                    ivTypeInstanceDevice.setImageResource(event.getDevice().getIconResource());
                    ivTypeInstance.setImageResource(event.getIconResource());
                    tvTypeInstanceName.setText(event.getName());
                    setViewHasInstance(true);
                    if(event.isSkeleton()){
                        showInstanceName();
                        hideInstanceValueZero();
                        hideInstanceValueOneTwo();
                    } else if(event.isTimeEvent()) {
                        TimeEvent timeEvent = (TimeEvent) event;
                        hideInstanceValueOneTwo();
                        if (mEdit) {
                            hideInstanceName();
                            showInstanceValueZero();
                            tvTypeInstanceValueZero.setText("At: ");
                            bTypeInstanceValueZero.setText(timeEvent.getTime().toString());
                        } else {
                            showInstanceName();
                            hideInstanceValueZero();
                            tvTypeInstanceValueZero.setText("At:   "+timeEvent.getTime().toString());
                        }
                    } else if(event.isInputActionEvent()){
                        InputActionEvent inputActionEvent = (InputActionEvent) event;
                        hideInstanceName();
                        showInstanceValueZero();
                        hideInstanceValueOneTwo();
                        tvTypeInstanceValueZero.setText(inputActionEvent.getInputAction().getDescription());
                        bTypeInstanceValueZero.setText(inputActionEvent.getInputAction().getName());
                        if(!mEdit) bTypeInstanceValueZero.setBackgroundColor(Color.TRANSPARENT);
                    } else if(event.isLocationEvent()){String text = "";
                        LocationEvent locationEvent = (LocationEvent) event;
                        if(locationEvent.getLocationEventType()== LocationEvent.LocationEventType.ARRIVING){
                            text = "Arrived at:   ";
                        } else if (locationEvent.getLocationEventType()== LocationEvent.LocationEventType.LEAVING){
                            text = "Left:   ";
                        }
                        ivTypeInstance.setImageResource(locationEvent.getLocation().getIconResource());
                        if (mEdit){
                            hideInstanceName();
                            showInstanceValueZero();
                            hideInstanceValueOneTwo();
                            tvTypeInstanceValueZero.setText(text);
                            bTypeInstanceValueZero.setText(locationEvent.getLocation().getName());
                        } else {
                            tvTypeInstanceName.setText(text+ locationEvent.getLocation().getName());
                            showInstanceName();
                            hideInstanceValueZero();
                            hideInstanceValueOneTwo();
                        }
                    }
                }
            } else if(type.isStateType()){
                // Set Type information
                tvTypeIntro.setText("While");
                //llType.setBackgroundResource(R.color.colorState);
                // Set TypeInstance information
                llTypeInstanceItem.setBackgroundResource(R.color.colorState);
                State state = ((StateType) type).getInstanceState();
                if(state == null){
                    setViewHasInstance(false);
                    showInstanceName();
                    hideInstanceValueZero();
                    hideInstanceValueOneTwo();
                    tvTypeInstanceName.setText("Add a state of this type!");
                } else {
                    tvTypeInstanceType.setText("State");
                    ivTypeInstanceDevice.setImageResource(state.getDevice().getIconResource());
                    ivTypeInstance.setImageResource(state.getIconResource());
                    tvTypeInstanceName.setText(state.getName());
                    setViewHasInstance(true);
                    if (state.isSkeleton()) {
                        showInstanceName();
                        hideInstanceValueZero();
                        hideInstanceValueOneTwo();
                    } else if (state.isTimeState()) {
                        TimeState timeState = (TimeState) state;
                        if (mEdit) {
                            hideInstanceName();
                            hideInstanceValueZero();
                            showInstanceValueOneTwo();
                            tvTypeInstanceValueOne.setText("From: ");
                            tvTypeInstanceValueTwo.setText("To: ");
                            bTypeInstanceValueOne.setText(timeState.getTimeFrom().toString());
                            bTypeInstanceValueTwo.setText(timeState.getTimeTo().toString());
                        } else {
                            showInstanceName();
                            hideInstanceValueZero();
                            hideInstanceValueOneTwo();
                            tvTypeInstanceName.setText("From:   " + timeState.getTimeFrom().toString() + "   to:   " + timeState.getTimeTo().toString());
                        }
                    } else if (state.isLocationState()) {
                        LocationState locationState = (LocationState) state;
                        if (mEdit) {
                            hideInstanceName();
                            showInstanceValueZero();
                            hideInstanceValueOneTwo();
                            tvTypeInstanceValueZero.setText("Currently at: ");
                            bTypeInstanceValueZero.setText(locationState.getLocation().getName());
                            ivTypeInstance.setImageResource(locationState.getLocation().getIconResource());
                        } else {
                            showInstanceName();
                            hideInstanceValueZero();
                            hideInstanceValueOneTwo();
                            tvTypeInstanceName.setText("Currently at:   " + locationState.getLocation().getName());
                            ivTypeInstance.setImageResource(locationState.getLocation().getIconResource());
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
                llTypeInstanceItem.setBackgroundResource(R.color.colorAction);
                Action action = ((ActionType) type).getInstanceAction();
                if(action == null){
                    tvTypeInstanceName.setText("Add an action of this type!");
                    setViewHasInstance(false);
                    showInstanceName();
                    hideInstanceValueZero();
                    hideInstanceValueOneTwo();
                } else {
                    tvTypeInstanceType.setText("Action");
                    ivTypeInstanceDevice.setImageResource(action.getDevice().getIconResource());
                    ivTypeInstance.setImageResource(action.getIconResource());
                    tvTypeInstanceName.setText(action.getName());
                    setViewHasInstance(true);
                    if(action.isSkeleton()){
                        showInstanceName();
                        hideInstanceValueZero();
                        hideInstanceValueOneTwo();
                    } else if (action.isNotificationAction()){
                        NotificationAction notificationAction = (NotificationAction) action;
                        hideInstanceValueOneTwo();
                        if (mEdit) {
                            hideInstanceName();
                            showInstanceValueZero();
                            tvTypeInstanceValueZero.setText("Notify: ");
                            bTypeInstanceValueZero.setText(notificationAction.getTitle()+" - "+notificationAction.getText());
                        } else {
                            showInstanceName();
                            hideInstanceValueZero();
                            tvTypeInstanceName.setText("Notify:   "+notificationAction.getTitle()+" - "+notificationAction.getText());
                        }
                    }
                }
            }
        }

        @Override
        public void onClick(View view) {
            Type type = mDataset.get(getAdapterPosition());
            Log.d("TRGRD","TypesAdapter onClick type: id = "+type.getId()+", name = "+type.getName());
            myOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
