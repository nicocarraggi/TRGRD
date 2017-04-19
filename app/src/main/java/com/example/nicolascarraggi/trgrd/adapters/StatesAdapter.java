package com.example.nicolascarraggi.trgrd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.StateViewHolder> {
    
    private ArrayList<State> mDataset;

    public StatesAdapter(Set<State> mDataset) {
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<State>() {
            @Override
            public int compare(State state2, State state1)
            {
                return  state1.getName().compareTo(state2.getName());
            }
        });
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
        final State state = mDataset.get(position);
        holder.ivStateDevice.setImageResource(state.getDevice().getIconResource());
        holder.ivState.setImageResource(state.getIconResource());
        holder.tvStateName.setText(state.getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivStateDevice, ivState;
        public TextView tvStateName;

        public StateViewHolder(View itemView) {
            super(itemView);
            this.ivStateDevice = (ImageView) itemView.findViewById(R.id.ivStateDevice);
            this.ivState = (ImageView) itemView.findViewById(R.id.ivState);
            this.tvStateName = (TextView) itemView.findViewById(R.id.tvStateName);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
