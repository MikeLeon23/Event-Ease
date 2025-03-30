package com.example.eventease;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Calendar;

public class OrganizerEventUpdate extends AppCompatActivity {

    private TextView tvEveNameUpdate, tvEveLocationUpdate, tvEveDateUpdate, tvEveTimeUpate,
            tvEveFeeUpdate, tvEveDescriptionUpdate, tvEveSeatUpdate, btnGoBack, btnUploadImage, btnUpdate;
    private Spinner spEveReminderUpdate, spEveStatusUpdate;
    private DBHelper dbHelper;
    private String eventId, organizerId;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 3;
    private String imagePath;
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_event_update);

        // Initialize UI components
        tvEveNameUpdate = findViewById(R.id.edEventNameUpdate);
        tvEveLocationUpdate = findViewById(R.id.edEventLocationUpdate);
        tvEveDateUpdate = findViewById(R.id.edEventDateUpdate);
        tvEveTimeUpate = findViewById(R.id.edEventTimeUpdate);
        tvEveFeeUpdate = findViewById(R.id.edEventFeeUpdate);
        tvEveDescriptionUpdate = findViewById(R.id.edEventDescriptionUpdate);
        tvEveSeatUpdate = findViewById(R.id.edEventSeatsUpdate);
        spEveReminderUpdate = findViewById(R.id.spEventReminderUpdate);
        spEveStatusUpdate = findViewById(R.id.spEventStatusUpdate);
        imageView = findViewById(R.id.imageEventUpdate);

        btnUploadImage = findViewById(R.id.btnEvnImgUpdate);
        btnUploadImage.setOnClickListener(v -> openGallery());

        btnGoBack = findViewById(R.id.btnTurnBack);
        btnUpdate = findViewById(R.id.btnEventUpdate);

        calendar = Calendar.getInstance();

        tvEveDateUpdate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    OrganizerEventUpdate.this,
                    (view, year, month, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvEveDateUpdate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Time Picker
        tvEveTimeUpate.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    OrganizerEventUpdate.this,
                    (view, hourOfDay, minute) -> {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        tvEveTimeUpate.setText(selectedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true // 24-hour format
            );
            timePickerDialog.show();
        });



        dbHelper = new DBHelper(this);

        // Get eventId from intent
        eventId = getIntent().getStringExtra("event_id");

        if (eventId != null) {
            loadEventDetails(eventId);
        }

        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrganizerEventUpdate.this, OrganizerActiveEvents.class));
            }
        });

        organizerId = getIntent().getStringExtra("organizer_id");
        btnUpdate.setOnClickListener(v -> updateEventToDatabase(organizerId));


    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of image selection from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            // Use Glide to load the selected image into the ImageView
            Glide.with(this)
                    .load(selectedImageUri)
                    .into(imageView);
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

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

    private void loadEventDetails(String eventId) {
//        Event event = dbHelper.getEventById(eventId);
//
//        if (event != null) {
//            tvEveNameUpdate.setText(event.getEventName());
//            tvEveLocationUpdate.setText(event.getEventLocation());
//            tvEveDateUpdate.setText(event.getEventDate());
//            tvEveTimeUpate.setText(event.getEventTime());
//            tvEveFeeUpdate.setText(String.valueOf(event.getEventFee()));
//            tvEveDescriptionUpdate.setText(event.getEventDescription());
//            tvEveSeatUpdate.setText(event.getEventSeat());
//            selectSpinnerItemByValue(spEveReminderUpdate, event.getEventReminder());
//            selectSpinnerItemByValue(spEveStatusUpdate, event.getEventStatus());
//        }
//    }
        Cursor cursor = dbHelper.getEventById(eventId);
        if (cursor != null && cursor.moveToFirst()) {
            tvEveNameUpdate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_NAME)));
            tvEveLocationUpdate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_LOCATION)));
            tvEveDateUpdate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_DATE)));
            tvEveTimeUpate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_TIME)));
            tvEveFeeUpdate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_FEE)));
            tvEveDescriptionUpdate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_DESCRIPTION)));
            tvEveSeatUpdate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_SEAT)));
            String reminderValue = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_REMINDER));
            String statusValue = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_STATUS));

            selectSpinnerItemByValue(spEveReminderUpdate, reminderValue);
            selectSpinnerItemByValue(spEveStatusUpdate, statusValue);

            String storedImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IMAGE_PATH));
            if (storedImagePath != null && !storedImagePath.isEmpty() && new File(storedImagePath).exists()) {
                Glide.with(this).load(new File(storedImagePath)).into(imageView);
            } else {
                Glide.with(this).load(R.drawable.dc_logo).into(imageView);
            }
        }
        cursor.close();
    }

    private void selectSpinnerItemByValue(Spinner spinner, String value) {
        if (spinner.getAdapter() != null) {
            for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
                if (spinner.getAdapter().getItem(i).toString().equals(value)) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private void updateEventToDatabase(String organizerId) {
        String name = tvEveNameUpdate.getText().toString();
        String location = tvEveLocationUpdate.getText().toString();
        String date = tvEveDateUpdate.getText().toString();
        String time = tvEveTimeUpate.getText().toString();
        double fee = Double.parseDouble(tvEveFeeUpdate.getText().toString());
        String description = tvEveDescriptionUpdate.getText().toString();
        String reminder = spEveReminderUpdate.getSelectedItem().toString();
        int seat = Integer.parseInt(tvEveSeatUpdate.getText().toString());
        String status = spEveStatusUpdate.getSelectedItem().toString();

        // If no new image is selected, keep the old image path
        if (imagePath == null || imagePath.isEmpty()) {
            imagePath = dbHelper.getEventImagePath(eventId);
        }

        boolean isInserted = dbHelper.updateEventData(eventId, name, location, date, time, fee, description, reminder, seat, status, imagePath, organizerId, false);

        if (isInserted) {
            Toast.makeText(this, "Event Updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to Update Event", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(OrganizerEventUpdate.this, OrganizerActiveEvents.class));
    }



}
