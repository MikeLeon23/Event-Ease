package com.example.eventease;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerEventDetail extends AppCompatActivity {

    private TextView tvEventName, tvEventLocation, tvEventDate, tvEventTime, tvEventFee, tvEventDescription;
    private DBHelper dbHelper;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_from_organizer_account);

        // Initialize UI components
        tvEventName = findViewById(R.id.textViewOrgDetail);

        dbHelper = new DBHelper(this);

        // Get eventId from intent
        eventId = getIntent().getStringExtra("eventId");

        if (eventId != null) {
            loadEventDetails(eventId);
        }
    }

    private void loadEventDetails(String eventId) {
        Event event = dbHelper.getEventById(eventId);
        if (event != null) {
            tvEventName.setText(event.getEventName());
//            tvEventLocation.setText(event.getEventLocation());
//            tvEventDate.setText(event.getEventDate());
//            tvEventTime.setText(event.getEventTime());
//            tvEventFee.setText(String.valueOf(event.getEventFee()));
//            tvEventDescription.setText(event.getEventDescription());
        }
    }

}
