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
    TextView edEventDate, edEventTime, btnUploadImage, btnEventCreate, btnGoBack;
    Spinner spEventReminder, spEventStatus;
    Calendar calendar;
    DBHelper dbHelper;
    private ImageView imageView;
    String imagePath, organizerId;

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

        dbHelper = new DBHelper(this);
        organizerId = getIntent().getStringExtra("COLUMN_ID");

        edEventName = findViewById(R.id.edEventName);
        edEventLocation = findViewById(R.id.edEventLocation);
        edEventDate = findViewById(R.id.tvEventDate);
        edEventTime = findViewById(R.id.tvEventTime);
        edEventFee = findViewById(R.id.edEventFee);
        edEventDescription = findViewById(R.id.edEventDescription);
        edEventSeat = findViewById(R.id.edEventSeats);
        spEventReminder = findViewById(R.id.spEventReminder);
        spEventStatus = findViewById(R.id.spEventStatus);
        imageView = findViewById(R.id.imageEventCreate);
        btnUploadImage = findViewById(R.id.btnEvnImgUpload);
        btnEventCreate = findViewById(R.id.btnEventCreate);
        btnGoBack = findViewById(R.id.btnTurnBack);

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

        spEventReminder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedReminder = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Reminder selected: " + selectedReminder, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        spEventStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Status selected: " + selectedStatus, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Save event to database
        btnEventCreate.setOnClickListener(v -> saveEventToDatabase(organizerId));
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerEventCreation.this, OrganizerAccountManage.class));
            }
        });
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

    private void saveEventToDatabase(String organizerId) {
        String name = edEventName.getText().toString();
        String location = edEventLocation.getText().toString();
        String date = edEventDate.getText().toString();
        String time = edEventTime.getText().toString();
        double fee = Double.parseDouble(edEventFee.getText().toString());
        String description = edEventDescription.getText().toString();
        String reminder = spEventReminder.getSelectedItem().toString();
        int seat = Integer.parseInt(edEventSeat.getText().toString());
        String status = spEventStatus.getSelectedItem().toString();

        boolean isInserted = dbHelper.insertEventData(name, location, date, time, fee, description, reminder, seat, status, imagePath, organizerId, false);

        if (isInserted) {
            Toast.makeText(this, "Event Saved Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to Save Event", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(OrganizerEventCreation.this, OrganizerAccountManage.class));
    }

}