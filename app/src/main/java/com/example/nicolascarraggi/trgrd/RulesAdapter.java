package com.example.nicolascarraggi.trgrd;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public RulesAdapter(Set<Rule> mDataset) {
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
        final Rule rule = mDataset.get(position);
        holder.tvRuleName.setText(rule.getName());
        holder.switchRuleActive.setChecked(rule.isActive());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class RuleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivRule;
        public TextView tvRuleName;
        public SwitchCompat switchRuleActive;

        public RuleViewHolder(View itemView) {
            super(itemView);
            this.ivRule = (ImageView) itemView.findViewById(R.id.ivRule);
            this.tvRuleName = (TextView) itemView.findViewById(R.id.tvRuleName);
            this.switchRuleActive = (SwitchCompat) itemView.findViewById(R.id.switchRuleActive);
        }

        @Override
        public void onClick(View view) {
            // example
            /*switch(view.getId()) {
                case R.id.ivPersonDelete:
                    delete(getPosition());
                    break;
            }*/
        }
    }
}
