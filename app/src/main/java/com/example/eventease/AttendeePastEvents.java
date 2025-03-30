package com.example.eventease;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AttendeePastEvents extends AppCompatActivity {

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

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAttendeePast);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        // Add items to list
//        List<ListEvents> items = new ArrayList<>();
//        items.add(new ListEvents("Item 4"));
//        items.add(new ListEvents("Item 5"));
//        items.add(new ListEvents("Item 6"));
//
//        // Set adapter
//        AttendeeListEventAdapter adapter = new AttendeeListEventAdapter(items, this);
//        recyclerView.setAdapter(adapter);
    }
}