package com.example.eventease;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class EventDetail extends AppCompatActivity {
    private DBHelper dbHelper;
    private String eventId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        SharedPreferences prefs = EventDetail.this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = prefs.getString("user_id", null);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Get event ID from intent
        eventId = getIntent().getStringExtra("event_id");
        if (eventId == null) {
            Toast.makeText(this, "No event ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load event data into UI
        loadEventData(eventId);

        // Set up click listeners
        findViewById(R.id.back_arrow).setOnClickListener(v -> finish());
        findViewById(R.id.star_icon).setOnClickListener(v -> toggleFavorite());
        findViewById(R.id.register_button).setOnClickListener(v -> registerForEvent());
    }

    private void loadEventData(String eventId) {
        Event event = dbHelper.getEventById(eventId);
        if (event != null) {
            // Set event title
            TextView title = findViewById(R.id.event_title);
            title.setText(event.getEventName());

            // Set event location
            TextView location = findViewById(R.id.event_location);
            location.setText(event.getEventLocation());

            // Set event date and time
            TextView date = findViewById(R.id.event_date);
            String dateTime = event.getEventDate() + " at " + event.getEventTime();
            date.setText(dateTime);

            // Set event description
            TextView description = findViewById(R.id.event_description);
            description.setText(event.getEventDescription());

            // Load event image or show placeholder
            ImageView image = findViewById(R.id.event_image);
            if (event.getEventImagePath() != null && !event.getEventImagePath().isEmpty()) {
                // Replace with actual image loading logic (e.g., Glide or Picasso)
                // Example: Glide.with(this).load(event.getImagePath()).into(image);
                image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder));
            } else {
                image.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.placeholder));
            }
        } else {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void toggleFavorite() {
        // Implement favorite logic (e.g., update database and toggle star icon)
        Toast.makeText(this, "Favorite toggled", Toast.LENGTH_SHORT).show();
    }

    private void registerForEvent() {
        // Implement registration logic (e.g., API call or database update)
        dbHelper.insertTicket(userId, eventId);
        Toast.makeText(this, "Registered for event", Toast.LENGTH_SHORT).show();
    }
}