package com.example.frontend;

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class Subscribe {
    String topic;

//    public Subscribe(String topic) {
//        this.topic = topic;
//    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void subscribe(String topic ){
        FirebaseMessaging.getInstance().subscribeToTopic(topic);

        FirebaseFirestore db;
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        Map<String, Object> subs = new HashMap<>();
        subs.put("subs", topic);
        subs.put("userId",auth.getUid());

        db.collection("subs")
                .add(subs);
    }

    public void unsubscribe(String topic){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }
}
