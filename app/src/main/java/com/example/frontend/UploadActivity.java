package com.example.frontend;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 1;

    private FirebaseFirestore db;
    private StorageReference storageRef;
    private StorageTask mUploadTask;

    private Button uploadButton, chooseImageButton, openCameraButton;

    private EditText headlineEditText,bodyEditText,tagsEditText;

    private ImageView imageView;
    private ProgressBar progressBar;
    private FirebaseAuth authentication;
    private String fileName;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        db = FirebaseFirestore.getInstance();

        uploadButton = findViewById(R.id.uploadButton);
        chooseImageButton = findViewById(R.id.chooseImageButton);
//        openCameraButton = findViewById(R.id.openCameraButton);

        headlineEditText = findViewById(R.id.headlineEditText);
        bodyEditText= findViewById(R.id.bodyEditText);
        tagsEditText=findViewById(R.id.tagsEditText);

        imageView = findViewById(R.id.imageView);
        authentication = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);


        chooseImageButton.setOnClickListener((View v)-> openFileChooser());
//        openCameraButton.setOnClickListener((View v)-> takePhotoOnCamera());
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

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem1 -> {

            switch (menuItem1.getItemId()){
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
        });

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

//    private void takePhotoOnCamera() {
//        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent1, CAMERA_REQUEST_CODE);
//        intent1.setType("image/*");
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            //imageUri = getImageUri(photo);
//            imageView.setImageBitmap(photo);
//        }
    }

    public Uri getImageUri(Context inContext, Bitmap photo) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), photo, "Title", null);
        return Uri.parse(path);
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
                        String tags = tagsEditText.getText().toString();
                        String userId = authentication.getCurrentUser().getUid();
                        Date currentTime = Calendar.getInstance().getTime();
                        String time = currentTime.toString();


                        Map<String, Object> news = new HashMap<>();
                        news.put("headline",headline);
                        news.put("body", body);
                        news.put("filename",fileName);
                        news.put("tags",tags);
                        news.put("time",time);
                        news.put("user_id", userId);

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