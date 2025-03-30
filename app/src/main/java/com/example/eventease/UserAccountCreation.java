package com.example.eventease;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class UserAccountCreation extends AppCompatActivity {

    private ImageView imageView;
    private TextView btnUploadImage;
    DBHelper dbHelper;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_creation);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DBHelper(this);

        EditText edName = findViewById(R.id.edUserName);
        EditText edEmail = findViewById(R.id.edUserEmail);
        EditText edPhone = findViewById(R.id.edUserPhone);
        EditText edPassword = findViewById(R.id.edUserPassword);
        EditText edAddress = findViewById(R.id.edUserAddress);
        Spinner edUserType = findViewById(R.id.spUserType);
        imageView = findViewById(R.id.imageView);
        btnUploadImage = findViewById(R.id.btnEvnImgUpload);
        TextView btnCreate = findViewById(R.id.btnAccCreate);

        btnUploadImage.setOnClickListener(v -> openGallery());

        btnCreate.setOnClickListener(v -> {
            String name = edName.getText().toString();
            String email = edEmail.getText().toString();
            String phone = edPhone.getText().toString();
            String password = edPassword.getText().toString();
            String address = edAddress.getText().toString();
            String userType = edUserType.getSelectedItem().toString();


            boolean inserted = dbHelper.insertUserData(name, email, phone, password, address, userType, imagePath);
            if (inserted) {
                Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }

            startActivity(new Intent(UserAccountCreation.this, MainActivity.class));

        });
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

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

}