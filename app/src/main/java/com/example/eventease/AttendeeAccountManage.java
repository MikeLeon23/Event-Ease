package com.example.eventease;

import android.content.Intent;
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

public class AttendeeAccountManage extends AppCompatActivity {

    TextView tvAttName;
    ImageView imageView;
    DBHelper dbHelper;
    String attendeeId;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_account_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listViewAccount = findViewById(R.id.listViewAttendeeAccount);

        tvAttName = findViewById(R.id.tvAttendeeName);
        imageView = findViewById(R.id.imageAttendee);
        TextView btnSignOut = findViewById(R.id.btnSignOut);

        dbHelper = new DBHelper(this);
        attendeeId = getIntent().getStringExtra("COLUMN_ID");
        loadUserData(attendeeId);

        String[] account = {"Profile Management", "Active Events", "Past Events"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, account);
        listViewAccount.setAdapter(adapter1);
        listViewAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(AttendeeAccountManage.this, AttendeeProfileUpdate.class);
                        intent.putExtra("COLUMN_ID", attendeeId);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(AttendeeAccountManage.this, AttendeeActiveEvents.class);
                        intent.putExtra("COLUMN_ID", attendeeId);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(AttendeeAccountManage.this, AttendeePastEvents.class);
                        intent.putExtra("COLUMN_ID", attendeeId);
                        startActivity(intent);
                        break;
                }
            }
        });


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeeAccountManage.this, MainActivity.class));
            }
        });

        // Set up BottomNavigationView using the helper
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationHelper.setupBottomNavigation(this, bottomNavigationView);
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
            tvAttName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME)));

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