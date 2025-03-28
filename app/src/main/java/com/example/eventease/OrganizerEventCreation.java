package com.example.eventease;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.Calendar;

public class OrganizerEventCreation extends AppCompatActivity {

    EditText edEventName, edEventLocation, edEventFee, edEventDescription, edEventSeat;
    TextView edEventDate, edEventTime, btnUploadImage, btnEventCreate;
    Spinner spinnerReminder;
    Calendar calendar;
    DBHelper dbHelper;
    private ImageView imageView;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer_event_creation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edEventName = findViewById(R.id.edEventName);
        edEventLocation = findViewById(R.id.edEventLocation);
        edEventDate = findViewById(R.id.tvEventDate);
        edEventTime = findViewById(R.id.tvEventTime);
        edEventFee = findViewById(R.id.edEventFee);
        edEventDescription = findViewById(R.id.edEventDescription);
        edEventSeat = findViewById(R.id.edEventSeats);
        spinnerReminder = findViewById(R.id.spEventReminder);
        imageView = findViewById(R.id.imageEventCreate);
        btnUploadImage = findViewById(R.id.btnEvnImgUpload);
        btnEventCreate = findViewById(R.id.btnEventCreate);

        btnUploadImage.setOnClickListener(v -> openGallery());

        calendar = Calendar.getInstance();

        edEventDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    OrganizerEventCreation.this,
                    (view, year, month, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        edEventDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Time Picker
        edEventTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    OrganizerEventCreation.this,
                    (view, hourOfDay, minute) -> {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        edEventTime.setText(selectedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true // 24-hour format
            );
            timePickerDialog.show();
        });

        spinnerReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedReminder = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Reminder set: " + selectedReminder, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        dbHelper = new DBHelper(this);

        // Save event to database
        btnEventCreate.setOnClickListener(v -> saveEventToDatabase());
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            // Get the image path from the URI
            String imagePath = getImagePath(imageUri);
            if (imagePath != null) {
                this.imagePath = imagePath; // Save it for later use in registration
            }

            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    // Helper method to get the file path from URI
    private String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        } else {
            return null;
        }
    }

    private void saveEventToDatabase() {
        String name = edEventName.getText().toString();
        String location = edEventLocation.getText().toString();
        String date = edEventDate.getText().toString();
        String time = edEventTime.getText().toString();
        double fee = Double.parseDouble(edEventFee.getText().toString());
        String description = edEventDescription.getText().toString();
        String reminder = spinnerReminder.getSelectedItem().toString();
        int seat = Integer.parseInt(edEventSeat.getText().toString());

        // Insert event data into the database and get the event ID
        long eventId = dbHelper.insertEventData(name, location, date, time, fee, description, reminder, seat, imagePath);
// Insert event data into the database
     //   long isInserted = dbHelper.insertEventData(name, location, date, time, fee, description, reminder, seat, imagePath);

        if (eventId != -1) {
            Toast.makeText(this, "Event Saved Successfully", Toast.LENGTH_SHORT).show();

            // Start the ShareActivity and pass the event ID
            Intent intent = new Intent(OrganizerEventCreation.this, ShareEventActivity.class);
            intent.putExtra("eventId", eventId); // Pass the eventId to ShareActivity
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to Save Event", Toast.LENGTH_SHORT).show();
        }
    }

}