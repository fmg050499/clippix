package com.example.frontend;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseFirestore db;
    private StorageReference storageRef;
    private StorageTask mUploadTask;

    private Button uploadButton, chooseImageButton;

    private EditText headlineEditText,bodyEditText;

    private ImageView imageView;
    private ProgressBar progressBar;

    private String fileName;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        db = FirebaseFirestore.getInstance();

        uploadButton = findViewById(R.id.uploadButton);
        chooseImageButton = findViewById(R.id.chooseImageButton);

        headlineEditText = findViewById(R.id.headlineEditText);
        bodyEditText= findViewById(R.id.bodyEditText);

        imageView = findViewById(R.id.imageView);

        progressBar = findViewById(R.id.progressBar);


        chooseImageButton.setOnClickListener((View v)-> openFileChooser());
        uploadButton.setOnClickListener(v -> {
            if (mUploadTask != null && mUploadTask.isInProgress()){
                Toast.makeText(this, "Upload is in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadFile();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(UploadActivity.this,HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.nav_subscribe:
                        Intent intent2 = new Intent(UploadActivity.this,SubscribeActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_post:
                        Intent intent3 = new Intent(UploadActivity.this,UploadActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.nav_read:
                        Intent intent4 = new Intent(UploadActivity.this,ReadActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_notification:
                        Intent intent5 = new Intent(UploadActivity.this,NotificationActivity.class);
                        startActivity(intent5);
                        break;
                }

                return false;
            }
        });

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(imageView);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference("news");
        if (imageUri != null){

            fileName = System.currentTimeMillis()+"."+getFileExtension(imageUri);

            StorageReference fileReference = storageRef.child(fileName);

            mUploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {

                        }, 5000);

                        Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG).show();

                        String headline = headlineEditText.getText().toString();
                        String body = bodyEditText.getText().toString();

                        Map<String, Object> news = new HashMap<>();
                        news.put("headline",headline);
                        news.put("body", body);
                        news.put("filename",fileName);

                        db.collection("news")
                                .add(news)
                                .addOnSuccessListener((DocumentReference ref)->{
                                })
                                .addOnFailureListener((Exception ex)->{
                                });
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBar.setProgress((int) progress);
                    });
        } else {
            Toast.makeText(this,"No file selected", Toast.LENGTH_LONG).show();
        }
    }

}