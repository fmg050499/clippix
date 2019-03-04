package com.example.frontend;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    TextView notificationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationTextView = findViewById(R.id.notificationTextView);
        notificationTextView.setText("Notification");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem1 -> {

            switch (menuItem1.getItemId()){
                case R.id.nav_home:
                    Intent intent1 = new Intent(NotificationActivity.this,HomeActivity.class);
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
        });
    }

}
