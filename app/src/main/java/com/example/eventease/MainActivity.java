package com.example.eventease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private EditText edEmailLogin, edPassLogin;
    private Button btnLogin, btnCheckEvents;
    private DBHelper dbHelper;

    TextView btnForgotPass, btnCreateNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmailLogin = findViewById(R.id.edEmailLogin);
        edPassLogin = findViewById(R.id.edPassLogin);
        btnLogin = findViewById(R.id.btnLogin);
        dbHelper = new DBHelper(this);

        btnLogin.setOnClickListener(v -> {
            loginUser();
            requestStoragePermission();});

        btnForgotPass = findViewById(R.id.btnForgotPassword);
        btnCreateNewAccount = findViewById(R.id.btnCreateNewAccount);
        btnCheckEvents = findViewById(R.id.btnCheckEventss);
        btnCheckEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EventHome.class));
            }
        });
        btnCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserAccountCreation.class));
            }
        });
    }

    private void loginUser() {
        String username = edEmailLogin.getText().toString().trim();
        String password = edPassLogin.getText().toString().trim();

        // Check if the credentials are valid
        User user = dbHelper.getUserByUsernameAndPassword(username, password);

        if (user != null) {
            // Save user information using SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("user_id", user.getId());
            editor.putString("user_name", user.getName());
            editor.putString("user_type", user.getUserType());
            editor.commit();

            //Store email in SharedPreferences when login is successful
            sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("user_email", user.getEmail()); // Store the email from the user object
            editor.apply();
            if(user.getUserType().equals("Attendee")){
                // Successful login, navigate to profile update page or main screen
                Intent intent = new Intent(MainActivity.this, AttendeeAccountManage.class);
                intent.putExtra("COLUMN_ID", user.getId());
                startActivity(intent);
                finish();
            }
            if(user.getUserType().equals("Organizer")){
                // Successful login, navigate to profile update page or main screen
                Intent intent = new Intent(MainActivity.this, OrganizerAccountManage.class);
                intent.putExtra("COLUMN_ID", user.getId());
                startActivity(intent);
                finish();
            }

        } else {
            // Show error message if login fails
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, STORAGE_PERMISSION_CODE);
            } else {
//                pickImageFromGallery();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
//                pickImageFromGallery();
            }
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                pickImageFromGallery();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Launch the gallery to pick an image
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

}