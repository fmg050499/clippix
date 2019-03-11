package com.example.frontend;

import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class Subscriptions {
    String topic;
    String userId;

    public Subscriptions(String topic,String userId) {
        this.topic = topic;
        this.userId = userId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void subscribe(){
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();

        Map<String, Object> subs = new HashMap<>();
        subs.put("subs", topic);
        subs.put("userId",userId);

        db.collection("subscribers")
                .add(subs);
    }

    public void unsubscribe(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }
}
