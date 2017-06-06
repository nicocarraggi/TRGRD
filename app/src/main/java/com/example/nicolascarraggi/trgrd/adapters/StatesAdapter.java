package com.example.nicolascarraggi.trgrd.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.LocationState;
import com.example.nicolascarraggi.trgrd.rulesys.State;
import com.example.nicolascarraggi.trgrd.rulesys.TimeState;

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
        private TextView tvStateTypeName, tvStateName, tvStateValueOne, tvStateValueTwo;
        private Button bStateValueOne, bStateValueTwo;

        public StateViewHolder(View itemView) {
            super(itemView);
            this.ivStateDevice = (ImageView) itemView.findViewById(R.id.ivStateDevice);
            this.ivState = (ImageView) itemView.findViewById(R.id.ivState);
            this.ivStateDelete = (ImageView) itemView.findViewById(R.id.ivStateDelete);
            this.tvStateTypeName = (TextView) itemView.findViewById(R.id.tvStateTypeName);
            this.tvStateName = (TextView) itemView.findViewById(R.id.tvStateName);
            this.tvStateValueOne = (TextView) itemView.findViewById(R.id.tvStateValueOne);
            this.tvStateValueTwo = (TextView) itemView.findViewById(R.id.tvStateValueTwo);
            this.bStateValueOne = (Button) itemView.findViewById(R.id.bStateValueOne);
            this.bStateValueTwo = (Button) itemView.findViewById(R.id.bStateValueTwo);
            tvStateName.setOnClickListener(this);
            ivStateDelete.setOnClickListener(this);
            bStateValueOne.setOnClickListener(this);
            bStateValueTwo.setOnClickListener(this);
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
                // Hide unwanted views!
                if(tvStateValueOne != null) tvStateValueOne.setVisibility(View.GONE);
                if(bStateValueOne != null) bStateValueOne.setVisibility(View.GONE);
                if(tvStateValueTwo != null) tvStateValueTwo.setVisibility(View.GONE);
                if(bStateValueTwo != null) bStateValueTwo.setVisibility(View.GONE);
            } else if(state.isTimeState()){ // this is NOT skeleton ...
                TimeState timeState = (TimeState) state;
                if(mEdit) {
                    // TODO really needed?
                    if (tvStateValueOne != null
                            && bStateValueOne != null
                            && tvStateValueTwo != null
                            && bStateValueTwo != null) {
                        tvStateValueOne.setVisibility(View.VISIBLE);
                        bStateValueOne.setVisibility(View.VISIBLE);
                        tvStateValueTwo.setVisibility(View.VISIBLE);
                        bStateValueTwo.setVisibility(View.VISIBLE);
                        tvStateName.setVisibility(View.GONE);
                        tvStateValueOne.setText("From");
                        tvStateValueTwo.setText("To");
                        bStateValueOne.setText(timeState.getTimeFrom().toString());
                        bStateValueTwo.setText(timeState.getTimeTo().toString());
                    }
                } else {
                    tvStateName.setText("From   "+timeState.getTimeFrom().toString()+"   to   "+timeState.getTimeTo().toString());
                    // Hide unwanted views!
                    if(tvStateValueOne != null) tvStateValueOne.setVisibility(View.GONE);
                    if(bStateValueOne != null) bStateValueOne.setVisibility(View.GONE);
                    if(tvStateValueTwo != null) tvStateValueTwo.setVisibility(View.GONE);
                    if(bStateValueTwo != null) bStateValueTwo.setVisibility(View.GONE);
                }
            } else if(state.isLocationState()) {
                LocationState locationState = (LocationState) state;
                if (mEdit) {
                    // TODO really needed?
                    if (tvStateValueOne != null
                            && bStateValueOne != null
                            && tvStateValueTwo != null
                            && bStateValueTwo != null) {
                        tvStateValueOne.setVisibility(View.VISIBLE);
                        bStateValueOne.setVisibility(View.VISIBLE);
                        tvStateValueTwo.setVisibility(View.GONE);
                        bStateValueTwo.setVisibility(View.GONE);
                        tvStateName.setVisibility(View.GONE);
                        tvStateValueOne.setText("Currently at: ");
                        bStateValueOne.setText(locationState.getLocation().getName());
                    }
                } else {
                        tvStateName.setText("Currently at:   " + locationState.getLocation().getName());
                        // Hide unwanted views!
                        if (tvStateValueOne != null) tvStateValueOne.setVisibility(View.GONE);
                        if (bStateValueOne != null) bStateValueOne.setVisibility(View.GONE);
                        if (tvStateValueTwo != null) tvStateValueTwo.setVisibility(View.GONE);
                        if (bStateValueTwo != null) bStateValueTwo.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View view) {
            myStateOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
