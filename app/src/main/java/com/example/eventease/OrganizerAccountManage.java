package com.example.eventease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class OrganizerAccountManage extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1;
    TextView tvOrgName, tvActEveOrgCount;
    ImageView imageView;
    DBHelper dbHelper;
    String organizerId;
    String userEmail;
    private static final int PICK_IMAGE_REQUEST = 1;
//    private static final int STORAGE_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer_account_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listViewAccount = findViewById(R.id.listViewOrganizerAccount);

        tvOrgName = findViewById(R.id.tvOrgName);
        imageView = findViewById(R.id.imageOrg);
//        tvActEveOrgCount = findViewById(R.id.tvActEveOrgCount); //to be done when have time
        TextView btnSignOut = findViewById(R.id.btnSignOut);

        dbHelper = new DBHelper(this);

        SharedPreferences prefs = OrganizerAccountManage.this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        organizerId = prefs.getString("user_id", null);
        loadUserData(organizerId);

        prefs = OrganizerAccountManage.this.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userEmail = prefs.getString("user_email", null);

        String[] account = {"Profile Management", "Active Events", "Past Events", "Create Event", "Invitations"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, account);
        listViewAccount.setAdapter(adapter1);
        listViewAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(OrganizerAccountManage.this, OrganizerProfileUpdate.class);
                        intent.putExtra("COLUMN_ID", organizerId);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(OrganizerAccountManage.this, OrganizerActiveEvents.class);
                        intent.putExtra("COLUMN_ID", organizerId);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(OrganizerAccountManage.this, OrganizerPastEvents.class);
                        intent.putExtra("COLUMN_ID", organizerId);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(OrganizerAccountManage.this, OrganizerEventCreation.class);
                        intent.putExtra("COLUMN_ID", organizerId);
                        startActivity(intent);;
                        break;
                    case 4:
                        intent = new Intent(OrganizerAccountManage.this, Invitations.class);
                        intent.putExtra("user_email", userEmail); // Replace with actual email
                        startActivity(intent);
                        break;
                }
            }
        });


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerAccountManage.this, MainActivity.class));
            }
        });

        // Set up BottomNavigationView using the helper
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationHelper.setupBottomNavigation(this, bottomNavigationView);

//        imageView.setOnClickListener(v -> requestStoragePermission());
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
    }

    private void loadUserData(String id) {
        Cursor cursor = dbHelper.getUserById(id);

        if (cursor != null && cursor.moveToFirst()) {
            tvOrgName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME)));

            String storedImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IMAGE_PATH));
            if (storedImagePath != null && !storedImagePath.isEmpty() && new File(storedImagePath).exists()) {
                Glide.with(this).load(new File(storedImagePath)).into(imageView);
            } else {
                Glide.with(this).load(R.drawable.dc_logo).into(imageView);
            }
        }
        cursor.close();
    }

//    private void requestStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, STORAGE_PERMISSION_CODE);
//            } else {
//                pickImageFromGallery();
//            }
//        } else {
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//            } else {
//                pickImageFromGallery();
//            }
//        }
//    }
//
//    // Handle permission result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                pickImageFromGallery();
//            } else {
//                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    // Launch the gallery to pick an image
//    private void pickImageFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
}