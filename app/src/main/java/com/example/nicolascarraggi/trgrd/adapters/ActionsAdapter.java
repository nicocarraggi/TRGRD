package com.example.nicolascarraggi.trgrd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionViewHolder> {

    private ArrayList<Action> mDataset;

    public ActionsAdapter(Set<Action> mDataset) {
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Action>() {
            @Override
            public int compare(Action action2, Action action1)
            {
                return  action1.getName().compareTo(action2.getName());
            }
        });
    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.action_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ActionViewHolder vh = new ActionViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, int position) {
        final Action action = mDataset.get(position);
        holder.ivActionDevice.setImageResource(action.getDevice().getIconResource());
        holder.ivAction.setImageResource(action.getIconResource());
        holder.tvActionName.setText(action.getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivActionDevice, ivAction;
        public TextView tvActionName;

        public ActionViewHolder(View itemView) {
            super(itemView);
            this.ivActionDevice = (ImageView) itemView.findViewById(R.id.ivActionDevice);
            this.ivAction = (ImageView) itemView.findViewById(R.id.ivAction);
            this.tvActionName = (TextView) itemView.findViewById(R.id.tvActionName);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
