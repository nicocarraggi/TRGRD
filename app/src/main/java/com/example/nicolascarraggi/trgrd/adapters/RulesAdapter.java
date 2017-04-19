package com.example.nicolascarraggi.trgrd.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.RuleDetailsActivity;
import com.example.nicolascarraggi.trgrd.rulesys.Rule;

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

    public RulesAdapter(MyOnItemClickListener<Rule> listener, Set<Rule> mDataset) {
        this.mListener = listener;
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Rule>() {
            @Override
            public int compare(Rule rule2, Rule rule1)
            {
                return  rule1.getName().compareTo(rule2.getName());
            }
        });
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

    public class RuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MyOnItemClickListener myOnItemClickListener;
        private ImageView ivRule;
        private TextView tvRuleName;
        private SwitchCompat switchRuleActive;

        public RuleViewHolder(View itemView) {
            super(itemView);
            this.ivRule = (ImageView) itemView.findViewById(R.id.ivRule);
            this.tvRuleName = (TextView) itemView.findViewById(R.id.tvRuleName);
            this.switchRuleActive = (SwitchCompat) itemView.findViewById(R.id.switchRuleActive);
            tvRuleName.setOnClickListener(this);
        }

        public void bind(Rule rule, MyOnItemClickListener listener){
            this.myOnItemClickListener = listener;
            this.tvRuleName.setText(rule.getName());
            this.switchRuleActive.setChecked(rule.isActive());
        }

        @Override
        public void onClick(View view) {
            // example
            switch(view.getId()) {
                case R.id.tvRuleName:
                    myOnItemClickListener.onItemClick(mDataset.get(getAdapterPosition()));
                    break;
            }
        }
    }
}
