package com.example.nicolascarraggi.trgrd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.NotificationAction;
import com.example.nicolascarraggi.trgrd.rulesys.ScoreValueAction;

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
    private boolean mEdit, mShowDelete;

    public ActionsAdapter(MyActionOnItemClickListener listener, Set<Action> mDataset, boolean edit, boolean showDelete) {
        this.mListener = listener;
        this.mDataset = new ArrayList<>();
        this.mEdit = edit;
        this.mShowDelete = showDelete;
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
        private TextView tvActionTypeName, tvActionName, tvActionValueZero, tvActionValueThree,
                            tvActionValueThreeValue;
        private LinearLayout llActionValueZero, llActionValueThree;
        private Button bActionValueZero;

        public ActionViewHolder(View itemView) {
            super(itemView);
            this.ivActionDevice = (ImageView) itemView.findViewById(R.id.ivActionDevice);
            this.ivAction = (ImageView) itemView.findViewById(R.id.ivAction);
            this.ivActionDelete = (ImageView) itemView.findViewById(R.id.ivActionDelete);
            this.tvActionTypeName = (TextView) itemView.findViewById(R.id.tvActionTypeName);
            this.tvActionName = (TextView) itemView.findViewById(R.id.tvActionName);
            this.tvActionValueZero = (TextView) itemView.findViewById(R.id.tvActionValueZero);
            this.tvActionValueThree = (TextView) itemView.findViewById(R.id.tvActionValueThree);
            this.tvActionValueThreeValue = (TextView) itemView.findViewById(R.id.tvActionValueThreeValue);
            this.bActionValueZero = (Button) itemView.findViewById(R.id.bActionValueZero);
            this.llActionValueZero = (LinearLayout) itemView.findViewById(R.id.llActionValueZero);
            this.llActionValueThree = (LinearLayout) itemView.findViewById(R.id.llActionValueThree);
            tvActionName.setOnClickListener(this);
            ivActionDelete.setOnClickListener(this);
            bActionValueZero.setOnClickListener(this);
        }

        private void showName() {
            tvActionName.setVisibility(View.VISIBLE);
        }

        private void hideName() {
            tvActionName.setVisibility(View.GONE);
        }

        private void showValueZero() {
            llActionValueZero.setVisibility(View.VISIBLE);
        }
        private void hideValueZero(){
            llActionValueZero.setVisibility(View.GONE);
        }

        private void showValueThree() {
            llActionValueThree.setVisibility(View.VISIBLE);
        }
        private void hideValueThree(){
            llActionValueThree.setVisibility(View.GONE);
        }

        public void bind(Action action, MyActionOnItemClickListener listener){
            MyLogger.debugLog("TRGRD","ActionsAdapter action "+action.getName());
            this.myActionOnItemClickListener = listener;
            this.ivActionDevice.setImageResource(action.getDevice().getIconResource());
            this.ivAction.setImageResource(action.getIconResource());
            this.tvActionTypeName.setText(action.getActionType().getName());
            this.tvActionName.setText(action.getName());
            if(!mShowDelete && ivActionDelete != null){
                ivActionDelete.setVisibility(View.GONE);
            }
            hideName();
            hideValueZero();
            hideValueThree();
            if(action.isSkeleton()){
                showName();
            } else if (action.isNotificationAction()){
                NotificationAction notificationAction = (NotificationAction) action;
                if (mEdit) {
                    showValueZero();
                    tvActionValueZero.setText("Notify: ");
                    bActionValueZero.setText(notificationAction.getTitle()+" - "+notificationAction.getText());
                } else {
                    showValueThree();
                    tvActionValueThree.setText("Notify:   ");
                    tvActionValueThreeValue.setText(notificationAction.getTitle()+" - "+notificationAction.getText());
                }
            } else if (action.isScoreValueAction()){
                ScoreValueAction scoreValueAction = (ScoreValueAction) action;
                if (mEdit) {
                    showValueZero();
                    tvActionValueZero.setText(scoreValueAction.getName());
                    bActionValueZero.setText(Integer.toString(scoreValueAction.getValue()));
                } else {
                    showValueThree();
                    tvActionValueThree.setText(scoreValueAction.getName());
                    tvActionValueThreeValue.setText(Integer.toString(scoreValueAction.getValue()));
                }
            }
        }

        @Override
        public void onClick(View view) {
            myActionOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
