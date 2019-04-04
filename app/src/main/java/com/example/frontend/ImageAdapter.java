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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private StorageReference storageRefTemp;
    private Context mContext;
    private List<News> mews;
    private OnItemClickListener mListener;

    public ImageAdapter(Context context,List<News> news){
        mContext = context;
        mews = news;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return  new ImageViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder( ImageViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRefTemp = storage.getReference();

        News newsCurrent = mews.get(position);
        holder.headlineTextView.setText(newsCurrent.getHeadline());
        holder.tagsTextView.setText(newsCurrent.getTags());
        holder.timeTextView.setText(newsCurrent.getTime());
        holder.bodyTextView.setText(newsCurrent.getBody());

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
        return mews.size();
    }

    public interface OnItemClickListener{
        void onItemCLick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public TextView headlineTextView;
        public TextView tagsTextView;
        public TextView timeTextView;
        public ImageView homeImageView;
        public TextView bodyTextView;


        public ImageViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);

            headlineTextView = itemView.findViewById(R.id.homeHeadlineTextView);
            homeImageView = itemView.findViewById(R.id.homeImageView);
            tagsTextView = itemView.findViewById(R.id.homeTagsTextView);
            timeTextView = itemView.findViewById(R.id.homeTimeTextView);
            bodyTextView = itemView.findViewById(R.id.homeBodyTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemCLick(position);
                        }
                    }
                }
            });
        }
    }
}
