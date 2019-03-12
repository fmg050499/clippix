package com.example.frontend;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth authentication;
    FirebaseFirestore db;

    TextView textView;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private List<News> mNews;

    Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        authentication = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNews = new ArrayList<>();

        mAdapter = new ImageAdapter(HomeActivity.this, mNews);
        mRecyclerView.setAdapter(mAdapter);

        textView = findViewById(R.id.textView);

        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemCLick(int position) {
                Toast.makeText(HomeActivity.this,""+mNews.get(position),Toast.LENGTH_LONG).show();

            }
        });


        db
                .collection("news")
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task) -> {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> data = document.getData();

                        String fileName = data.get("filename").toString();
                        String headline = data.get("headline").toString();
                        String body = data.get("body").toString();
                        String tags = data.get("tags").toString();
                        String time = data.get("time").toString();
                        String userId = data.get("user_id").toString();

                        News news = new News(headline, body, fileName, tags, time, userId);

                        mNews.add(news);
                        mAdapter = new ImageAdapter(HomeActivity.this, mNews);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        logOutButton = findViewById(R.id.logOutButton);

        logOutButton.setOnClickListener((View view) -> {
            logOut();
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem1 -> {

            switch (menuItem1.getItemId()) {
                case R.id.nav_home:
                    Intent intent1 = new Intent(HomeActivity.this, HomeActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.nav_subscribe:
                    Intent intent2 = new Intent(HomeActivity.this, SubscribeActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_post:
                    Intent intent3 = new Intent(HomeActivity.this, UploadActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.nav_read:
                    Intent intent4 = new Intent(HomeActivity.this, ReadActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.nav_notification:
                    Intent intent5 = new Intent(HomeActivity.this, NotificationActivity.class);
                    startActivity(intent5);
                    break;
            }

            return false;
        });
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
}
