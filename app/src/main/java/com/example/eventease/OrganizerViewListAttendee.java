package com.example.eventease;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Set;

public class OrganizerViewListAttendee extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrganizerListAttendeeAdapter attendeeAdapter;
    private DBHelper dbHelper;
    private String organizerId, eventName, eventId, eventStatus;
    private TextView tvAttendeeCount, tvEventName, btnDelete, btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer_view_list_attendee);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerAttendeeList);
        tvEventName = findViewById(R.id.tvEventName);
        tvAttendeeCount = findViewById(R.id.tvAttendeeCount);
        dbHelper = new DBHelper(this);

        // Get organizer ID from intent
        organizerId = getIntent().getStringExtra("ORGANIZER_ID");
        eventName = getIntent().getStringExtra("EVENT_NAME");
        eventId = getIntent().getStringExtra("EVENT_ID");
        if (eventId == null || eventId.trim().isEmpty()) {
            Toast.makeText(this, "Error: Event ID not found", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if there's no valid event
        }
        eventStatus = getIntent().getStringExtra("EVENT_STATUS");

        tvEventName.setText(eventName);
        // Load attendee list
        loadAttendeeList(organizerId);

        btnGoBack = findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerViewListAttendee.this, OrganizerActiveEvents.class));
            }
        });

//        btnDelete = findViewById(R.id.btnAttendeeDelete);
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Set<String> selectedIds = attendeeAdapter.getSelectedAttendeeIds();
//
//                if (selectedIds.isEmpty()) {
//                    Toast.makeText(OrganizerViewListAttendee.this, "No events selected", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(OrganizerViewListAttendee.this);
//                builder.setTitle("Delete Events")
//                        .setMessage("Are you sure you want to delete the selected attendees?")
//                        .setPositiveButton("Yes", (dialog, which) -> {
//                            DBHelper dbHelper = new DBHelper(OrganizerViewListAttendee.this);
//                            boolean allDeleted = true;
//
//                            for (String attendeeId : selectedIds) {
//                                Log.d("OrganizerAttendees", "AttendeeID: " + attendeeId + " EventID: " + eventId );
//                                boolean deleted = dbHelper.deleteTicket(attendeeId, eventId);
//                                if (!deleted) {
//                                    allDeleted = false;
//                                }
//                            }
//
//                            if (allDeleted) {
//                                attendeeAdapter.removeDeletedAttendees(); // Update UI only if all deletions succeeded
//                                Toast.makeText(OrganizerViewListAttendee.this, "Attendees deleted", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(OrganizerViewListAttendee.this, "Some attendees could not be deleted", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
//            }
//        });
    }

    private void loadAttendeeList(String organizerId) {
        List<User> attendees = dbHelper.getAttendeesByOrganizerIdAndStatus(organizerId, eventStatus);
        Log.d("OrganizerAttendees", "Total attendees loaded: " + attendees.size());

        tvAttendeeCount.setText("Total " + attendees.size() + " attendees");

        attendeeAdapter = new OrganizerListAttendeeAdapter(attendees, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(attendeeAdapter);
    }

}