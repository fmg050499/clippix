package com.example.frontend;

import android.content.Intent;
import android.icu.lang.UScript;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadActivity extends AppCompatActivity{
    FirebaseAuth authentication;
    FirebaseFirestore db;

    TextView textView;

    private RecyclerView mRecyclerView;
    private ReadAdapter mAdapter;

    private List<News> newsSubscribedRead;
    private List<String> currentUsersSubscriptions;
    private List<String> agencyIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        authentication = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.readRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentUsersSubscriptions = new ArrayList<>();
        newsSubscribedRead = new ArrayList<>();
        agencyIds = new ArrayList<>();

        mAdapter = new ReadAdapter(ReadActivity.this,newsSubscribedRead);
        mRecyclerView.setAdapter(mAdapter);

        textView = findViewById(R.id.readTextView);

        db
                .collection("subscribers")
                .whereEqualTo("userId", authentication.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task)->{

                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> data = document.getData();

                        String subscriptions = data.get("subs").toString();

                        currentUsersSubscriptions.add(subscriptions);
                    }

                });

        db
                .collection("agencies")
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task)->{

                    for (QueryDocumentSnapshot document : task.getResult()){
                        for(String topic:currentUsersSubscriptions){
                            Map<String, Object> data = document.getData();
                            if(topic.equals(data.get("username").toString())){

                                String agencyId = data.get("userId").toString();

                                agencyIds.add(agencyId);

                            }
                        }
                    }


                });

        db
                .collection("news")
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task)->{
                    String output = "";
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> data = document.getData();
                        for(String id : agencyIds){
                            if(id.equals(data.get("user_id").toString())){
                                String fileName = data.get("filename").toString();
                                String headline = data.get("headline").toString();
                                String body = data.get("body").toString();
                                String tags = data.get("tags").toString();
                                String time = data.get("time").toString();
                                String userId = data.get("user_id").toString();

                                News news = new News (headline,body,fileName,tags,time,userId);

                                newsSubscribedRead.add(news);
                                mAdapter = new ReadAdapter(ReadActivity.this,newsSubscribedRead);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                    }

                });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(ReadActivity.this,HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_subscribe:
                        Intent intent2 = new Intent(ReadActivity.this,SubscribeActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_post:
                        Intent intent3 = new Intent(ReadActivity.this,UploadActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_read:
                        Intent intent4 = new Intent(ReadActivity.this,ReadActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_notification:
                        Intent intent5 = new Intent(ReadActivity.this,NotificationActivity.class);
                        startActivity(intent5);
                        break;
                }

                return false;
            }
        });
    }
}
