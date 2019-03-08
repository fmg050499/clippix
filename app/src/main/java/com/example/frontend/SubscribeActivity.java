package com.example.frontend;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubscribeActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView subscribeTextView;
    private RecyclerView mRecyclerView;
    private SubscribeAdapter mAdapter;

    List<String> mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.subscriptionsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSubscriptions = new ArrayList<>();

        mAdapter = new SubscribeAdapter(this,mSubscriptions);
        mRecyclerView.setAdapter(mAdapter);

        subscribeTextView = findViewById(R.id.subscribeTextView);
        subscribeTextView.setText("Subscribe");
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        db
                .collection("agencies")
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task)->{

                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> data = document.getData();


                        String agencyName = data.get("name").toString();

                        mSubscriptions.add(agencyName);
                    }
                });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(SubscribeActivity.this,HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_subscribe:
                        Intent intent2 = new Intent(SubscribeActivity.this,SubscribeActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_post:
                        Intent intent3 = new Intent(SubscribeActivity.this,UploadActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_read:
                        Intent intent4 = new Intent(SubscribeActivity.this,ReadActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_notification:
                        Intent intent5 = new Intent(SubscribeActivity.this,NotificationActivity.class);
                        startActivity(intent5);
                        break;
                }

                return false;
            }
        });


    }

}