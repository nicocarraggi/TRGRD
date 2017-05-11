package com.example.nicolascarraggi.trgrd.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nicolascarraggi.trgrd.R;
import com.example.nicolascarraggi.trgrd.rulesys.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by nicolascarraggi on 18/04/17.
 */

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.TypeViewHolder> {

    private ArrayList<Type> mDataset;
    private MyOnItemClickListener mListener;
    boolean mEdit;

    public TypesAdapter(MyOnItemClickListener listener, ArrayList<Type> mDataset, boolean edit) {
        this.mListener = listener;
        this.mDataset = mDataset;
        this.mEdit = edit;
        sort();
    }

    private void sort(){
        // TODO put Events above States
        // Sorting on name ... TODO other filters?
        Collections.sort(this.mDataset, new Comparator<Type>() {
            @Override
            public int compare(Type type2, Type type1)
            {
                return  type2.getName().compareTo(type1.getName());
            }
        });
    }

    public void updateData(Set<Type> mDataset) {
        this.mDataset.clear();
        this.mDataset.addAll(mDataset);
        sort();
        notifyDataSetChanged();
    }

    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.type_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        TypeViewHolder vh = new TypeViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TypeViewHolder holder, int position) {
        holder.bind(mDataset.get(position), mListener, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class TypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private MyOnItemClickListener<Type> myOnItemClickListener;
        private ImageView ivTypeDelete;
        private TextView tvTypeName, tvTypeIntro;
        private LinearLayout llType;

        public TypeViewHolder(View itemView) {
            super(itemView);
            this.tvTypeName = (TextView) itemView.findViewById(R.id.tvTypeName);
            this.tvTypeIntro = (TextView) itemView.findViewById(R.id.tvTypeIntro);
            this.ivTypeDelete = (ImageView) itemView.findViewById(R.id.ivTypeDelete);
            this.llType = (LinearLayout) itemView.findViewById(R.id.llType);
            tvTypeName.setOnClickListener(this);
            ivTypeDelete.setOnClickListener(this);
        }

        public void bind(Type type, MyOnItemClickListener listener, int position){
            this.myOnItemClickListener = listener;
            this.tvTypeName.setText(type.getName());
            if(!mEdit && ivTypeDelete != null){
                ivTypeDelete.setVisibility(View.GONE);
            }
            if(type.isEventType()){
                tvTypeIntro.setText("When");
                llType.setBackgroundResource(R.color.colorEvent);
            } else if(type.isStateType()){
                tvTypeIntro.setText("While");
                llType.setBackgroundResource(R.color.colorState);
            } else if (type.isActionType()){
                // First item show "Then"
                // TODO Other items show "And"
                tvTypeIntro.setText("Then");
                llType.setBackgroundResource(R.color.colorAction);
            }
        }

        @Override
        public void onClick(View view) {
            myOnItemClickListener.onItemClick(view, mDataset.get(getAdapterPosition()));
        }
    }
}
