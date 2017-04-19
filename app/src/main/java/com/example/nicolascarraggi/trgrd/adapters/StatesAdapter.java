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
    private MyStateOnItemClickListener mListener;

    public StatesAdapter(MyStateOnItemClickListener listener, Set<State> mDataset) {
        this.mListener = listener;
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
        holder.bind(mDataset.get(position),mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MyStateOnItemClickListener myStateOnItemClickListener;
        private ImageView ivStateDevice, ivState;
        private TextView tvStateName;

        public StateViewHolder(View itemView) {
            super(itemView);
            this.ivStateDevice = (ImageView) itemView.findViewById(R.id.ivStateDevice);
            this.ivState = (ImageView) itemView.findViewById(R.id.ivState);
            this.tvStateName = (TextView) itemView.findViewById(R.id.tvStateName);
            tvStateName.setOnClickListener(this);
        }

        public void bind(State state, MyStateOnItemClickListener listener){
            this.myStateOnItemClickListener = listener;
            this.ivStateDevice.setImageResource(state.getDevice().getIconResource());
            this.ivState.setImageResource(state.getIconResource());
            this.tvStateName.setText(state.getName());
        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.tvStateName:
                    myStateOnItemClickListener.onItemClick(mDataset.get(getAdapterPosition()));
                    break;
            }
        }
    }
}
