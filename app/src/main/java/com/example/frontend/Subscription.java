package com.example.frontend;

public class Subscription {
    String subscription;
    String userId;

    public Subscription(String subscription, String userId) {
        this.userId = userId;
        this.subscription = subscription;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }
}
