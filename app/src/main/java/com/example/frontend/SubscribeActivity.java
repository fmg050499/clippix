package com.example.frontend;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubscribeActivity extends AppCompatActivity{
    FirebaseAuth authentication;
    FirebaseFirestore db;

    TextView textView;

    private RecyclerView mRecyclerView;
    private SubscribeAdapter mAdapter;

    private List<Agency> agencies;
    private List<String> mySubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(SubscribeActivity.this,"this is a test",Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_subscribe);
        authentication = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.subscriptionRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        agencies = new ArrayList<>();

        mAdapter = new SubscribeAdapter(this,agencies);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new SubscribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Agency agency =agencies.get(position);
                Toast.makeText(SubscribeActivity.this, ""+agency.getUsername(), Toast.LENGTH_SHORT).show();

            }
        });

        textView = findViewById(R.id.subscriptionTextView);

        db
                .collection("agencies")
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task)->{

                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> data = document.getData();

                        String username = data.get("username").toString();
                        String userId = data.get("userId").toString();

                        Agency agency = new Agency(username,userId);

                        agencies.add(agency);
                        mAdapter = new SubscribeAdapter(SubscribeActivity.this, agencies);
                        mRecyclerView.setAdapter(mAdapter);

                    }
                });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem1 -> {

            switch (menuItem1.getItemId()){
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
        });
    }

}