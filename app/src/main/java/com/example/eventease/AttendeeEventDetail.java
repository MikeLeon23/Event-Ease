package com.example.eventease;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AttendeeEventDetail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_from_attendee_account);

        String itemTitle = getIntent().getStringExtra("itemTitle");
        TextView textView = findViewById(R.id.textView);
        textView.setText(itemTitle);
    }
}
