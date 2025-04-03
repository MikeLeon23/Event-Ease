package com.example.eventease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Invitations extends AppCompatActivity implements EventAdapter.OnEventActionListener {

    private RecyclerView rvInvitations;
    private EventAdapter eventAdapter;
    private List<Event> invitedEvents;
    private String userEmail;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);

        // Retrieve userEmail from intent
        userEmail = getIntent().getStringExtra("user_email");
        if (userEmail == null) {
            userEmail = "default@example.com"; // Replace with actual authentication logic
        }

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize views
        rvInvitations = findViewById(R.id.rvInvitations);
        ImageView backArrow = findViewById(R.id.back_arrow);

        // Set up RecyclerView
        rvInvitations.setLayoutManager(new LinearLayoutManager(this));
        invitedEvents = dbHelper.getInvitedEventsByEmail(userEmail);
        eventAdapter = new EventAdapter(this, invitedEvents, null, this, true); // null userId since not needed here
        rvInvitations.setAdapter(eventAdapter);

        // Back arrow click listener
        backArrow.setOnClickListener(v -> finish());
    }

    @Override
    public void onEditClick(Event event, int position) {
        // Not applicable for invitations page
    }

    @Override
    public void onDeleteClick(Event event, int position) {
        // Not applicable for invitations page
    }

    @Override
    public void onStarClick(Event event, int position) {
        event.setInterested(!event.isInterested());
        if (event.isInterested()) {
            dbHelper.insertInterestedEvent(event.getOrganizerId(), event.getEventId());
        } else {
            dbHelper.deleteInterestedEventById(event.getOrganizerId(), event.getEventId());
        }
        eventAdapter.notifyItemChanged(position);
    }
}