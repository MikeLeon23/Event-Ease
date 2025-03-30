package com.example.eventease;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class OrganizerPastEvents extends AppCompatActivity {

    RecyclerView recyclerView;
    OrganizerListEventAdapter adapter;
    DBHelper dbHelper;
    String organizerId;
    TextView tvOrgName, tvEventCount , btnGoBack, btnAttendeeList;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer_past_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        recyclerView = findViewById(R.id.recyclerViewOrganizerPast);
        imageView = findViewById(R.id.imageView);
        tvOrgName = findViewById(R.id.tvOrgName);
        tvEventCount = findViewById(R.id.tvPastEveCountOrg);

        dbHelper = new DBHelper(this);
        organizerId = getIntent().getStringExtra("COLUMN_ID");
        loadUserData(organizerId);
        loadPastEvents(organizerId);

        btnGoBack = findViewById(R.id.btnTurnBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerPastEvents.this, OrganizerAccountManage.class));
            }
        });
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

    private void loadPastEvents(String organizerId) {
        List<Event> pastEvents = dbHelper.getPastEventsByOrganizer(organizerId);
        Log.d("OrganizerPastEvents", "Total past events loaded: " + pastEvents.size());

        for (Event event : pastEvents) {
            Log.d("OrganizerPastEvents", "Event ID: " + event.getEventId() + ", Name: " + event.getEventName());
        }
        tvEventCount.setText("Total " + pastEvents.size() + " events");

        if (adapter == null) {
            adapter = new OrganizerListEventAdapter(pastEvents, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateEvents(pastEvents);
        }
    }


}