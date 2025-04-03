package com.example.eventease;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Set;

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
        btnAttendeeList = findViewById(R.id.btnAttendeeListPast);
        btnAttendeeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> selectedIds = adapter.getSelectedEventIds();
                if (selectedIds.isEmpty()) {
                    Toast.makeText(OrganizerPastEvents.this, "No attendee selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadAttendeeList(organizerId);

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

    private void loadAttendeeList(String organizerId) {
        Set<String> selectedIds = adapter.getSelectedEventIds();

        if (selectedIds.isEmpty()) {
            Toast.makeText(OrganizerPastEvents.this, "No events selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming you select one event for now, get the first event's ID and name
        String eventId = selectedIds.iterator().next();  // Get the first selected event ID

        Cursor event = dbHelper.getEventById(eventId);  // Retrieve the event name from the database
        if (event != null && event.moveToFirst()) {
            // Cursor is not empty, and we've moved to the first row
            String eventName = event.getString(event.getColumnIndexOrThrow("event_name"));
            String eventStatus = event.getString(event.getColumnIndexOrThrow("event_status"));

            // Now you can use the eventName as needed
            Intent intent = new Intent(OrganizerPastEvents.this, OrganizerViewListAttendee.class);
            intent.putExtra("ORGANIZER_ID", organizerId);
            intent.putExtra("EVENT_NAME", eventName);
            intent.putExtra("EVENT_ID", eventId);
            intent.putExtra("EVENT_STATUS", eventStatus);
            startActivity(intent);
        } else {
            // Handle the case where the event was not found or the cursor is empty
            Toast.makeText(OrganizerPastEvents.this, "Event not found", Toast.LENGTH_SHORT).show();
        }
    }


}