package com.example.frontend;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.ViewHolder> {
    private StorageReference storageRefTemp;
    private Context mContext;
    private List<Agency> mAgencies;
    private OnItemClickListener mListener;

    public SubscribeAdapter(Context context,List<Agency> agency){
        mContext = context;
        mAgencies= agency;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.subscribe_item,parent,false);
        return  new ViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRefTemp = storage.getReference();

        Agency agencyCurrent = mAgencies.get(position);
        holder.headlineTextView.setText(agencyCurrent.getUsername());


//        holder.toggleButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//                if((holder.toggleButton.isChecked())) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("tgpref", true); // value to store
//                    editor.commit();
//                }
//                else
//                {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("tgpref", false); // value to store
//                    editor.commit();
//                }
//            }
//        });
//
//        SharedPreferences sharedPreferences = getS(MODE_PRIVATE);
//        boolean tgpref = sharedPreferences.getBoolean("tgpref", true);  //default is true
//        if (tgpref = true) //if (tgpref) may be enough, not sure
//        {
//            tg.setChecked(true);
//        }
//        else
//        {
//            tg.setChecked(false);
//        }

        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                FirebaseAuth auth;
                auth = FirebaseAuth.getInstance();

                String topic = agencyCurrent.getUsername();

                Subscriptions subscription = new Subscriptions (topic,auth.getCurrentUser().getUid());
                if(isChecked){
                    subscription.subscribe();
                }else{
                    subscription.unsubscribe();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mAgencies.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView headlineTextView;
        public ToggleButton toggleButton;

        public ViewHolder(View itemView, OnItemClickListener listener ) {
            super(itemView);

            headlineTextView = itemView.findViewById(R.id.headlineTextView);
            toggleButton = itemView.findViewById(R.id.toggleButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }


    }




}
