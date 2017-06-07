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
import com.example.nicolascarraggi.trgrd.rulesys.LocationState;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.TimeState;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.StateViewHolder> {
    
    private ArrayList<State> mDataset;
    private MyStateOnItemClickListener mListener;
    private boolean mEdit, mShowDelete;

    public StatesAdapter(MyStateOnItemClickListener listener, Set<State> mDataset, boolean edit, boolean showDelete) {
        this.mListener = listener;
        this.mDataset = new ArrayList<>();
        this.mEdit = edit;
        this.mShowDelete = showDelete;
        this.mDataset.addAll(mDataset);
        sort();
    }

    private void sort(){
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<State>() {
            @Override
            public int compare(State state2, State state1)
            {
                return  state2.getName().compareTo(state1.getName());
            }
        });
    }

    public void updateData(Set<State> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        sort();
        notifyDataSetChanged();
    }

    @Override
    public StateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.state_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        StateViewHolder vh = new StateViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(StateViewHolder holder, int position) {
        holder.bind(mDataset.get(position),mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MyStateOnItemClickListener myStateOnItemClickListener;
        private ImageView ivStateDevice, ivState, ivStateDelete;
        private TextView tvStateTypeName, tvStateName, tvStateValueZero, tvStateValueOne, tvStateValueTwo;
        private Button bStateValueZero, bStateValueOne, bStateValueTwo;
        private LinearLayout llStateValueZero, llStateValueOneTwo;

        public StateViewHolder(View itemView) {
            super(itemView);
            this.ivStateDevice = (ImageView) itemView.findViewById(R.id.ivStateDevice);
            this.ivState = (ImageView) itemView.findViewById(R.id.ivState);
            this.ivStateDelete = (ImageView) itemView.findViewById(R.id.ivStateDelete);
            this.tvStateTypeName = (TextView) itemView.findViewById(R.id.tvStateTypeName);
            this.tvStateName = (TextView) itemView.findViewById(R.id.tvStateName);
            this.llStateValueZero = (LinearLayout) itemView.findViewById(R.id.llStateValueZero);
            this.llStateValueOneTwo = (LinearLayout) itemView.findViewById(R.id.llStateValueOneTwo);
            this.tvStateValueZero = (TextView) itemView.findViewById(R.id.tvStateValueZero);
            this.tvStateValueOne = (TextView) itemView.findViewById(R.id.tvStateValueOne);
            this.tvStateValueTwo = (TextView) itemView.findViewById(R.id.tvStateValueTwo);
            this.bStateValueZero = (Button) itemView.findViewById(R.id.bStateValueZero);
            this.bStateValueOne = (Button) itemView.findViewById(R.id.bStateValueOne);
            this.bStateValueTwo = (Button) itemView.findViewById(R.id.bStateValueTwo);
            tvStateName.setOnClickListener(this);
            ivStateDelete.setOnClickListener(this);
            bStateValueZero.setOnClickListener(this);
            bStateValueOne.setOnClickListener(this);
            bStateValueTwo.setOnClickListener(this);
        }

        private void showName() {
            tvStateName.setVisibility(View.VISIBLE);
        }

        private void hideName() {
            tvStateName.setVisibility(View.GONE);
        }

        private void showValueZero() {
            llStateValueZero.setVisibility(View.VISIBLE);
        }
        private void hideValueZero(){
            llStateValueZero.setVisibility(View.GONE);
        }

        private void showValueOneTwo(){
            llStateValueOneTwo.setVisibility(View.VISIBLE);
        }
        private void hideValueOneTwo(){
            llStateValueOneTwo.setVisibility(View.GONE);
        }

        public void bind(State state, MyStateOnItemClickListener listener){
            this.myStateOnItemClickListener = listener;
            this.ivStateDevice.setImageResource(state.getDevice().getIconResource());
            this.ivState.setImageResource(state.getIconResource());
            this.tvStateTypeName.setText(state.getStateType().getName());
            this.tvStateName.setText(state.getName());
            if(!mShowDelete && ivStateDelete != null){
                ivStateDelete.setVisibility(View.GONE);
            }
            Log.d("TRGRD","StatesAdapter state: name = "+state.getName()+ ", id = "+state.getId());
            if(state.isSkeleton()){
                hideValueZero();
                hideValueOneTwo();
            } else if(state.isTimeState()){ // this is NOT skeleton ...
                TimeState timeState = (TimeState) state;
                if(mEdit) {
                    hideName();
                    hideValueZero();
                    showValueOneTwo();
                    tvStateValueOne.setText("From: ");
                    tvStateValueTwo.setText("To: ");
                    bStateValueOne.setText(timeState.getTimeFrom().toString());
                    bStateValueTwo.setText(timeState.getTimeTo().toString());
                } else {
                    showName();
                    hideValueZero();
                    hideValueOneTwo();
                    tvStateName.setText("From:   "+timeState.getTimeFrom().toString()+"   to:   "+timeState.getTimeTo().toString());
                }
            } else if(state.isLocationState()) {
                LocationState locationState = (LocationState) state;
                if (mEdit) {
                    hideName();
                    showValueZero();
                    hideValueOneTwo();
                    tvStateValueZero.setText("Currently at: ");
                    bStateValueZero.setText(locationState.getLocation().getName());
                } else {
                    showName();
                    hideValueZero();
                    hideValueOneTwo();
                    tvStateName.setText("Currently at:   " + locationState.getLocation().getName());
                }
            }
        }

        @Override
        public void onClick(View view) {
            myStateOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
