package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity{
    FirebaseAuth authentication;
    FirebaseFirestore db;

    TextView textView;

    private DrawerLayout drawer;

    private RecyclerView mRecyclerView;
    private NotificationAdapter mAdapter;

    private List<String> mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        authentication = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.notificationRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSubscriptions = new ArrayList<>();

        mAdapter = new NotificationAdapter(NotificationActivity.this,mSubscriptions);
        mRecyclerView.setAdapter(mAdapter);

        textView = findViewById(R.id.notificationTextView);
        textView.setText("hello");

        db
                .collection("subscription")
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task)->{

                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> data = document.getData();

                        String userId = data.get("userId").toString();

                        mSubscriptions.add(userId);
                    }
                });

        db
                .collection("news")
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task)->{

                    String agenciesSubscribed = "";
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> data = document.getData();

                        String userId = data.get("user_id").toString();
                        String fileName = data.get("filename").toString();
                        String headline = data.get("headline").toString();
                        String body = data.get("body").toString();
                        for (int i=0 ; i<=mSubscriptions.size(); i++){
                            if (mSubscriptions.get(i).equals(userId)){
                                agenciesSubscribed+=userId;
                            }
                        }


//                        for (int i=0 ; i<=mSubscriptions.size(); i++){
//                            if (mSubscriptions.get(i).equals(userId)){
//                                agenciesSubscribed+=userId;
//                            }
//                        }
                    }
                    textView.setText("hello");
                });


        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(NotificationActivity.this, NotificationActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_subscribe:
                        Intent intent2 = new Intent(NotificationActivity.this,SubscribeActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_post:
                        Intent intent3 = new Intent(NotificationActivity.this,UploadActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_read:
                        Intent intent4 = new Intent(NotificationActivity.this,ReadActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_notification:
                        Intent intent5 = new Intent(NotificationActivity.this,NotificationActivity.class);
                        startActivity(intent5);
                        break;
                }

                return false;
            }
        });
    }

}
