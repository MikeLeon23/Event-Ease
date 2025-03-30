package com.example.eventease;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;

public class OrganizerProfileUpdate extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 3;
    private String imagePath;

    private EditText edName, edEmail, edPhone, edPassword, edAddress;
    private ImageView imageView;
    private String organizerId;

    private DBHelper dbHelper;
    private TextView btnUpdate, btnUploadImage, btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer_profile_update);

        edName = findViewById(R.id.edOrgName);
        edEmail = findViewById(R.id.edOrgEmail);
        edPhone = findViewById(R.id.edOrgPhone);
        edPassword = findViewById(R.id.edOrgPassword);
        edAddress = findViewById(R.id.edOrgAddress);
        imageView = findViewById(R.id.imageView);
        btnUploadImage = findViewById(R.id.btnEvnImgUpload);
        btnUpdate = findViewById(R.id.btnEventDelete);
        btnGoBack = findViewById(R.id.btnTurnBack);

        dbHelper = new DBHelper(this);
        organizerId = getIntent().getStringExtra("COLUMN_ID"); // Assuming you pass organizerId from previous activity

        loadUserData(organizerId);

        btnUploadImage.setOnClickListener(v -> openGallery());

        btnUpdate.setOnClickListener(v -> updateUser());

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerProfileUpdate.this, OrganizerAccountManage.class));
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of image selection from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Use Glide to load the selected image into the ImageView
            Glide.with(this)
                    .load(selectedImageUri)
                    .into(imageView);
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            // Get the image path from the URI
            String imagePath = getImagePath(imageUri);
            if (imagePath != null) {
                this.imagePath = imagePath; // Save it for later use in registration
            }

            Glide.with(this).load(imageUri).into(imageView);
        }

    }
    // Helper method to get the file path from URI
    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        } else {
            return null;
        }
    }

    private void updateUser() {
        String name = edName.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String phone = edPhone.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String address = edAddress.getText().toString().trim();

        // If no new image is selected, keep the old image path
        if (imagePath == null || imagePath.isEmpty()) {
            imagePath = dbHelper.getUserImagePath(organizerId);
        }

        // Update the user data in the database with the image path
        boolean result = dbHelper.updateUserData(organizerId, name, email, phone, password, address, imagePath);
        if (result) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(OrganizerProfileUpdate.this, OrganizerAccountManage.class));
    }

    private void loadUserData(String id) {
        Cursor cursor = dbHelper.getUserById(id);

        if (cursor != null && cursor.moveToFirst()) {
            edName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME)));
            edEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMAIL)));
            edPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PHONE)));
            edPassword.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PASSWORD)));
            edAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ADDRESS)));

            String storedImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IMAGE_PATH));
            if (storedImagePath != null && !storedImagePath.isEmpty() && new File(storedImagePath).exists()) {
                Glide.with(this).load(new File(storedImagePath)).into(imageView);
            } else {
                Glide.with(this).load(R.drawable.dc_logo).into(imageView);
            }
        }
        cursor.close();
    }

}