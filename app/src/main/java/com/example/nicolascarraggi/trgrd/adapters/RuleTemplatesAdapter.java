package com.example.nicolascarraggi.trgrd.adapters;

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
import com.example.nicolascarraggi.trgrd.rulesys.Rule;
import com.example.nicolascarraggi.trgrd.rulesys.RuleTemplate;
import com.example.nicolascarraggi.trgrd.rulesys.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 17/04/17.
 */

public class RuleTemplatesAdapter extends RecyclerView.Adapter<RuleTemplatesAdapter.RuleTemplateViewHolder> {

    private ArrayList<RuleTemplate> mDataset;
    private MyOnItemClickListener<RuleTemplate> mListener;

    public RuleTemplatesAdapter(MyOnItemClickListener<RuleTemplate> listener, Set<RuleTemplate> mDataset) {
        this.mListener = listener;
        this.mDataset = new ArrayList<>();
        this.mDataset.addAll(mDataset);
        sort();
    }

    private void sort(){
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<RuleTemplate>() {
            @Override
            public int compare(RuleTemplate rule2, RuleTemplate rule1)
            {
                return  rule2.getName().compareTo(rule1.getName());
            }
        });
    }

    public void updateData(Set<RuleTemplate> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        sort();
        notifyDataSetChanged();
    }

    @Override
    public RuleTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rule_template_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        RuleTemplateViewHolder vh = new RuleTemplateViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RuleTemplateViewHolder holder, int position) {
        holder.bind(mDataset.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class RuleTemplateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MyOnItemClickListener myOnItemClickListener;
        private ImageView ivRuleTemplate, ivRuleTemplateEvent, ivRuleTemplateState, ivRuleTemplateAction;
        private LinearLayout llEvent, llState, llAction;
        private TextView tvRuleTemplateName;

        public RuleTemplateViewHolder(View itemView) {
            super(itemView);
            this.ivRuleTemplate = (ImageView) itemView.findViewById(R.id.ivRuleTemplate);
            this.tvRuleTemplateName = (TextView) itemView.findViewById(R.id.tvRuleTemplateName);
            this.ivRuleTemplateEvent = (ImageView) itemView.findViewById(R.id.ivRuleTemplateEvent);
            this.ivRuleTemplateState = (ImageView) itemView.findViewById(R.id.ivRuleTemplateState);
            this.ivRuleTemplateAction = (ImageView) itemView.findViewById(R.id.ivRuleTemplateAction);
            this.llEvent = (LinearLayout) itemView.findViewById(R.id.llRuleTemplateEvent);
            this.llState = (LinearLayout) itemView.findViewById(R.id.llRuleTemplateStates);
            this.llAction = (LinearLayout) itemView.findViewById(R.id.llRuleTemplateActions);
            tvRuleTemplateName.setOnClickListener(this);
        }

        public void bind(RuleTemplate rule, MyOnItemClickListener listener){
            this.myOnItemClickListener = listener;
            this.tvRuleTemplateName.setText(rule.getName());
            boolean hasStateType = false;
            for (Type t : rule.getTriggerTypes()){
                if (t.isEventType()){
                    ivRuleTemplateEvent.setImageResource(t.getIconResource());
                } else if(t.isStateType()){
                    hasStateType = true;
                    ivRuleTemplateState.setImageResource(t.getIconResource());
                }
            }
            if(hasStateType) {
                llState.setVisibility(View.VISIBLE);
            } else {
                llState.setVisibility(View.GONE);
            }
            for (Type t : rule.getTriggerTypes()){
                if (t.isActionType()){
                    ivRuleTemplateAction.setImageResource(t.getIconResource());
                }
            }
        }

        @Override
        public void onClick(View view) {
            myOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
