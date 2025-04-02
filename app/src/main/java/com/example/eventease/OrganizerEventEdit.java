package com.example.eventease;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class OrganizerEventEdit extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText edEventName, edEventLocation, edEventFee, edEventDescription, edEventSeats;
    private TextView tvEventDate, tvEventTime;
    private Spinner spEventReminder, spEventStatus;
    private ImageView imageEventCreate;
    private TextView btnEventCreate, btnTurnBack, btnEvnImgUpload;
    private String eventId, imagePath, organizerId;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_edit);

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Initialize UI components
        edEventName = findViewById(R.id.edEventName);
        edEventLocation = findViewById(R.id.edEventLocation);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventTime = findViewById(R.id.tvEventTime);
        edEventFee = findViewById(R.id.edEventFee);
        edEventDescription = findViewById(R.id.edEventDescription);
        spEventReminder = findViewById(R.id.spEventReminder);
        edEventSeats = findViewById(R.id.edEventSeats);
        spEventStatus = findViewById(R.id.spEventStatus);
        imageEventCreate = findViewById(R.id.imageEventCreate);
        btnEventCreate = findViewById(R.id.btnEventCreate);
        btnTurnBack = findViewById(R.id.btnTurnBack);
        btnEvnImgUpload = findViewById(R.id.btnEvnImgUpload);

        // Get the event ID from the intent
        Intent intent = getIntent();
        eventId = intent.getStringExtra("event_id");

        if (eventId == null) {
            Toast.makeText(this, "Error: Event ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load the event details from the database
        Event event = dbHelper.getEventObjById(eventId);
        if (event == null) {
            Toast.makeText(this, "Error: Event not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate the UI with the event details
        edEventName.setText(event.getEventName());
        edEventLocation.setText(event.getEventLocation());
        tvEventDate.setText(event.getEventDate());
        tvEventTime.setText(event.getEventTime());
        edEventFee.setText(String.valueOf(event.getEventFee()));
        edEventDescription.setText(event.getEventDescription());
        edEventSeats.setText(String.valueOf(event.getEventSeat()));
        imagePath = event.getEventImagePath();
        organizerId = event.getOrganizerId();
        isChecked = event.isChecked();

        // Set the spinners to the correct values
        setSpinnerSelection(spEventReminder, event.getEventReminder());
        setSpinnerSelection(spEventStatus, event.getEventStatus());

        // Load the event image if available
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageEventCreate);
        } else {
            imageEventCreate.setImageResource(R.drawable.placeholder);
        }

        // Handle image upload (simplified for now, assuming same logic as creation)
        btnEvnImgUpload.setOnClickListener(v -> {
            // Implement image upload logic here (same as in OrganizerEventCreation)
            // For now, we'll assume the imagePath remains the same unless updated
            Toast.makeText(this, "Image upload not implemented", Toast.LENGTH_SHORT).show();
        });

        // Handle the Save button click
        btnEventCreate.setOnClickListener(v -> saveEvent());

        // Handle the Back button click
        btnTurnBack.setOnClickListener(v -> finish());
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        if (value == null) return;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveEvent() {
        // Get the updated values from the UI
        String name = edEventName.getText().toString().trim();
        String location = edEventLocation.getText().toString().trim();
        String date = tvEventDate.getText().toString().trim();
        String time = tvEventTime.getText().toString().trim();
        String feeStr = edEventFee.getText().toString().trim();
        String description = edEventDescription.getText().toString().trim();
        String reminder = spEventReminder.getSelectedItem().toString();
        String seatsStr = edEventSeats.getText().toString().trim();
        String status = spEventStatus.getSelectedItem().toString();

        // Validate inputs
        if (name.isEmpty() || location.isEmpty() || date.isEmpty() || time.isEmpty() ||
                feeStr.isEmpty() || description.isEmpty() || seatsStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double fee;
        int seats;
        try {
            fee = Double.parseDouble(feeStr);
            seats = Integer.parseInt(seatsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid fee or seats value", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the event in the database
        boolean updated = dbHelper.updateEventData(
                eventId,
                name,
                location,
                date,
                time,
                fee,
                description,
                reminder,
                seats,
                status,
                imagePath, // Use the existing image path (or update if image upload is implemented)
                organizerId,
                isChecked
        );

        if (updated) {
            Toast.makeText(this, "Event updated successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK); // Indicate success to the calling activity
            finish();
        } else {
            Toast.makeText(this, "Failed to update event", Toast.LENGTH_SHORT).show();
        }
    }
}