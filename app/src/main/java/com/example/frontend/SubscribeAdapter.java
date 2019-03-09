package com.example.frontend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.ViewHolder> {
    private StorageReference storageRefTemp;
    private Context mContext;
    private List<Subscription> mSubscription;

    public SubscribeAdapter(Context context,List<Subscription> subscriptions){
        mContext = context;
        mSubscription = subscriptions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.subscribe_item,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRefTemp = storage.getReference();

        Subscription subscriptionCurrent = mSubscription.get(position);
        holder.headlineTextView.setText(subscriptionCurrent.getSubscription());


    }

    @Override
    public int getItemCount() {
        return mSubscription.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView headlineTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            headlineTextView = itemView.findViewById(R.id.headlineTextView);

        }
    }




}
