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
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionViewHolder> {

    private MyActionOnItemClickListener mListener;
    private ArrayList<Action> mDataset;
    private boolean mEdit;

    public ActionsAdapter(MyActionOnItemClickListener listener, Set<Action> mDataset, boolean edit) {
        this.mListener = listener;
        this.mDataset = new ArrayList<>();
        this.mEdit = edit;
        this.mDataset.addAll(mDataset);
        sort();
    }

    private void sort(){
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Action>() {
            @Override
            public int compare(Action action2, Action action1)
            {
                return  action2.getName().compareTo(action1.getName());
            }
        });
    }

    public void updateData(Set<Action> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        sort();
        notifyDataSetChanged();
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
        holder.bind(mDataset.get(position),mListener);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MyActionOnItemClickListener myActionOnItemClickListener;
        private ImageView ivActionDevice, ivAction, ivActionDelete;
        private TextView tvActionName, tvActionValueOne;
        private Button bActionValueOne;

        public ActionViewHolder(View itemView) {
            super(itemView);
            this.ivActionDevice = (ImageView) itemView.findViewById(R.id.ivActionDevice);
            this.ivAction = (ImageView) itemView.findViewById(R.id.ivAction);
            this.ivActionDelete = (ImageView) itemView.findViewById(R.id.ivActionDelete);
            this.tvActionName = (TextView) itemView.findViewById(R.id.tvActionName);
            this.tvActionValueOne = (TextView) itemView.findViewById(R.id.tvActionValueOne);
            this.bActionValueOne = (Button) itemView.findViewById(R.id.bActionValueOne);
            tvActionName.setOnClickListener(this);
            ivActionDelete.setOnClickListener(this);
            bActionValueOne.setOnClickListener(this);
        }

        public void bind(Action action, MyActionOnItemClickListener listener){
            this.myActionOnItemClickListener = listener;
            this.ivActionDevice.setImageResource(action.getDevice().getIconResource());
            this.ivAction.setImageResource(action.getIconResource());
            this.tvActionName.setText(action.getName());
            if(!mEdit && ivActionDelete != null){
                ivActionDelete.setVisibility(View.GONE);
            }
            if(action.isSkeleton()){
                // Hide unwanted views!
                if(tvActionValueOne != null) tvActionValueOne.setVisibility(View.GONE);
                if(bActionValueOne != null) bActionValueOne.setVisibility(View.GONE);
            } else if (action.isNotificationAction()){
                NotificationAction notificationAction = (NotificationAction) action;
                if(tvActionValueOne != null & bActionValueOne != null){
                    tvActionValueOne.setVisibility(View.VISIBLE);
                    bActionValueOne.setVisibility(View.VISIBLE);
                    tvActionName.setVisibility(View.GONE);
                    tvActionValueOne.setText("Notify:");
                    bActionValueOne.setText(notificationAction.getTitle());
                    if(!mEdit) bActionValueOne.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }

        @Override
        public void onClick(View view) {
            myActionOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
