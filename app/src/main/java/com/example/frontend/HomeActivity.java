package com.example.frontend;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth authentication;

    TextView homeTextView;
    Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        authentication = FirebaseAuth.getInstance();

        homeTextView=findViewById(R.id.homeTextView);
        homeTextView.setText("Home");
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        logOutButton=findViewById(R.id.logOutButton);

        logOutButton.setOnClickListener((View view)->{
            logOut();
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(HomeActivity.this,HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_subscribe:
                        Intent intent2 = new Intent(HomeActivity.this,SubscribeActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_post:
                        Intent intent3 = new Intent(HomeActivity.this,UploadActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_read:
                        Intent intent4 = new Intent(HomeActivity.this,ReadActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_notification:
                        Intent intent5 = new Intent(HomeActivity .this,NotificationActivity.class);
                        startActivity(intent5);
                        break;
                }

                return false;
            }
        });
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,LogInActivity.class);
        startActivity(intent);
    }

}
