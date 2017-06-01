package com.example.nicolascarraggi.trgrd.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.logging.MyLogger;
import com.example.nicolascarraggi.trgrd.rulesys.Action;
import com.example.nicolascarraggi.trgrd.rulesys.Event;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 17/04/17.
 */

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.RuleViewHolder> {

    private ArrayList<Rule> mDataset;
    private MyOnItemClickListener<Rule> mListener;
    private boolean mShowSwitch;

    public RulesAdapter(MyOnItemClickListener<Rule> listener, Set<Rule> mDataset, boolean showSwitch) {
        this.mListener = listener;
        this.mShowSwitch = showSwitch;
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        sort();
    }

    private void sort(){
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Rule>() {
            @Override
            public int compare(Rule rule2, Rule rule1)
            {
                return  rule2.getName().compareTo(rule1.getName());
            }
        });
    }

    public void updateData(Set<Rule> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        sort();
        notifyDataSetChanged();
    }

    @Override
    public RuleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rule_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        RuleViewHolder vh = new RuleViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RuleViewHolder holder, int position) {
        holder.bind(mDataset.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class RuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private MyOnItemClickListener myOnItemClickListener;
        private ImageView ivRule, ivEvent, ivState, ivAction;
        private TextView tvRuleName;
        private LinearLayout llRuleIcons, llEvent, llStates, llActions;
        private SwitchCompat switchRuleActive;

        public RuleViewHolder(View itemView) {
            super(itemView);
            //this.ivRule = (ImageView) itemView.findViewById(R.id.ivRule);
            this.ivEvent = (ImageView) itemView.findViewById(R.id.ivRuleEvent);
            this.ivState = (ImageView) itemView.findViewById(R.id.ivRuleState);
            this.ivAction = (ImageView) itemView.findViewById(R.id.ivRuleAction);
            this.tvRuleName = (TextView) itemView.findViewById(R.id.tvRuleName);
            this.llRuleIcons = (LinearLayout) itemView.findViewById(R.id.llRuleIcons);
            this.llEvent = (LinearLayout) itemView.findViewById(R.id.llRuleEvent);
            this.llStates = (LinearLayout) itemView.findViewById(R.id.llRuleStates);
            this.llActions = (LinearLayout) itemView.findViewById(R.id.llRuleActions);
            tvRuleName.setOnClickListener(this);
            llRuleIcons.setOnClickListener(this);
            this.switchRuleActive = (SwitchCompat) itemView.findViewById(R.id.switchRuleActive);
            switchRuleActive.setOnCheckedChangeListener(this);
            if(!mShowSwitch) switchRuleActive.setVisibility(View.GONE);
        }

        public void bind(Rule rule, MyOnItemClickListener listener){
            this.myOnItemClickListener = listener;
            this.tvRuleName.setText(rule.getName());
            if (rule.getEvents().isEmpty()){
                llEvent.setVisibility(View.GONE);
            } else {
                for(Event e: rule.getEvents()){
                    ivEvent.setImageResource(e.getDevice().getIconResource());
                }
            }
            if (rule.getStates().isEmpty()){
                llStates.setVisibility(View.GONE);
            } else {
                for(State s: rule.getStates()){
                    // TODO inflate every other state?
                    ivState.setImageResource(s.getDevice().getIconResource());
                }
            }
            for (Action a: rule.getActions()){
                // TODO inflate every other action?
                ivAction.setImageResource(a.getDevice().getIconResource());
            }
            if (mShowSwitch) this.switchRuleActive.setChecked(rule.isActive());
        }

        @Override
        public void onClick(View view) {
            myOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }

        private void switchRuleActive(Rule rule, boolean b) {
            if(rule.isActive()!=b){
                rule.setActive(b);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch(compoundButton.getId()) {
                case R.id.switchRuleActive:
                    switchRuleActive(mDataset.get(getAdapterPosition()), b);
                    break;
            }
        }
    }
}
