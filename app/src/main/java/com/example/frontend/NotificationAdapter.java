package com.example.frontend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private StorageReference storageRefTemp;
    private Context mContext;
    private List<News> mSubscription;
    private SubscribeAdapter.OnItemClickListener mListener;

    public NotificationAdapter(Context context, List<News> subscriptions){
        mContext = context;
        mSubscription = subscriptions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRefTemp = storage.getReference();

        News subscriptionCurrent = mSubscription.get(position);
        holder.headlineTextView.setText(subscriptionCurrent.headline);
        holder.tagsTextView.setText(subscriptionCurrent.getTags());
        holder.timeTextView.setText(subscriptionCurrent.getTime());

        try {
            StorageReference tempRef = storageRefTemp.child("news").child(subscriptionCurrent.getFilename());

            File file = File.createTempFile("images", ".jpg");
            tempRef
                    .getFile(file)
                    .addOnSuccessListener(imageUri->{
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        holder.homeImageView.setImageBitmap(bitmap);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mSubscription.size();
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(SubscribeAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView headlineTextView;
        public TextView tagsTextView;
        public TextView timeTextView;
        public ImageView homeImageView;


        public ViewHolder(View itemView) {
            super(itemView);

            headlineTextView = itemView.findViewById(R.id.headlineTextView);
            homeImageView = itemView.findViewById(R.id.notificationImageView);
            tagsTextView = itemView.findViewById(R.id.tagsTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);

        }
    }




}
