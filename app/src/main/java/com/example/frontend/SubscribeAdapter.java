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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        FirebaseAuth authentication;
        FirebaseFirestore db;
        authentication = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        List<String> mySubscriptions;
        mySubscriptions = new ArrayList<>();

        Agency agencyCurrent = mAgencies.get(position);
        holder.headlineTextView.setText(agencyCurrent.getUsername());

        db
                .collection("subscribers")
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task)->{
                    String output="";
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> data = document.getData();

                        String userId = data.get("userId").toString();
                        String agency = data.get("subs").toString();

                        if(userId.equals(authentication.getCurrentUser().getUid())){
                            mySubscriptions.add(agency);
                            output+=agency;
                        }


                    }
                });
        for(String sub : mySubscriptions){
            if(sub.equals(agencyCurrent.getUsername())){
                holder.toggleButton.isChecked();
            }else {
                holder.toggleButton.setChecked(false);
            }
        }

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
