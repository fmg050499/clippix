package com.example.frontend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mSubscriptions;

    public SubscribeAdapter(Context context, List<String> subscriptions){
        mContext = context;
        mSubscriptions = subscriptions;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.subscribe_item,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String subscriptionCurrent = mSubscriptions.get(i);
        viewHolder.textView.setText(subscriptionCurrent);
        viewHolder.subscribeToggleButton
                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked){

                    }else{

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mSubscriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ToggleButton subscribeToggleButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.subscribeNameTextView);
            subscribeToggleButton = itemView.findViewById(R.id.subscribeToggleButton);


        }
    }
}
