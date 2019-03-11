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

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private StorageReference storageRefTemp;
    private Context mContext;
    private List<News> mNews;

    public ImageAdapter(Context context,List<News> news){
        mContext = context;
        mNews = news;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return  new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ImageViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRefTemp = storage.getReference();

        News newsCurrent = mNews.get(position);
        holder.headlineTextView.setText(newsCurrent.getHeadline());
        holder.usernameTextView.setText(newsCurrent.getUserId());
        holder.tagsTextView.setText(newsCurrent.getTags());
        holder.timeTextView.setText(newsCurrent.getTime());

        try {
            StorageReference tempRef = storageRefTemp.child("news").child(newsCurrent.getFilename());

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
        return mNews.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView headlineTextView;
        public TextView usernameTextView;
        public TextView tagsTextView;
        public TextView timeTextView;
        public ImageView homeImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            headlineTextView = itemView.findViewById(R.id.headlineTextView);
            homeImageView = itemView.findViewById(R.id.homeImageView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            tagsTextView = itemView.findViewById(R.id.tagsTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }
}
