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
import java.util.ArrayList;
import java.util.List;

public class AttendeePastEvents extends AppCompatActivity {

    RecyclerView recyclerView;
    AttendeeListEventAdapter adapter;
    DBHelper dbHelper;
    String attendeeId;
    TextView tvAttendeeName, tvEventCount , btnGoBack;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendee_past_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        recyclerView = findViewById(R.id.recyclerViewAttendeePast);
        imageView = findViewById(R.id.imageView);
        tvAttendeeName = findViewById(R.id.tvAttNamePast);
        tvEventCount = findViewById(R.id.tvPastEveCountAtt);

        dbHelper = new DBHelper(this);
        attendeeId = getIntent().getStringExtra("COLUMN_ID");
        loadUserData(attendeeId);
        loadPastEvents(attendeeId);

        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendeePastEvents.this, AttendeeAccountManage.class));
            }
        });
    }

    private void loadUserData(String id) {
        Cursor cursor = dbHelper.getUserById(id);

        if (cursor != null && cursor.moveToFirst()) {
            tvAttendeeName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME)));

            String storedImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IMAGE_PATH));
            if (storedImagePath != null && !storedImagePath.isEmpty() && new File(storedImagePath).exists()) {
                Glide.with(this).load(new File(storedImagePath)).into(imageView);
            } else {
                Glide.with(this).load(R.drawable.dc_logo).into(imageView);
            }
        }
        cursor.close();
    }

    private void loadPastEvents(String attendeeId) {
        List<Ticket> tickets = dbHelper.getTicketsByUserId(attendeeId);
        Log.d("AttendeePastEvents", "Total past events loaded: " + tickets.size());

//        for (Ticket ticket : tickets) {
//            Log.d("AttendeeActiveEvents", "Event ID: " + ticket.getEvent().getEventId() +
//                    ", Name: " + ticket.getEvent().getEventName() +
//                    ", Event_Status: " + ticket.getEvent().getEventStatus());
//        }



        // Extract list of events from the tickets
        List<Event> events = new ArrayList<>();
        for (Ticket ticket : tickets) {
            Event event = ticket.getEvent();
            if ("disable".equalsIgnoreCase(event.getEventStatus())) { // Only add disabled events
                Log.d("AttendeePastEvents", "Event ID: " + event.getEventId() +
                        ", Name: " + event.getEventName() +
                        ", Status: " + event.getEventStatus());
                events.add(event);

            }
        }
        tvEventCount.setText("Total " + events.size() + " events");

        if (adapter == null) {
            adapter = new AttendeeListEventAdapter(events, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateEvents(events);
        }
    }
}