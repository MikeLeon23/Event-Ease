package com.example.eventease;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class EventHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_home);

        // Set up TabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.getTabAt(0).select(); // Select "ALL" tab by default

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Event> events = new ArrayList<>();
//        events.add(new Event("GIGI Comedy Show", "Wed, Feb 5, 9 p.m, Royal City"));
        events.add(new Event("412341", "GIGI Comedy Show", "Royal City", "Wed, Feb 5",
                "7 pm", 9.99, "\"Laugh Track\" is a high-energy, fast-paced comedy", "",
        50, "http://example.com/asdfadf.jpg", "", "", false));
        events.add(new Event("4123478", "Super Star Activity", "Royal City", "Wed, Feb 5",
                "7 pm", 9.99, "\"Laugh Track\" is a high-energy, fast-paced comedy show", "",
                50, "http://example.com/asdfadf.jpg", "", "", false));
        // Initialize adapter with action listener
        EventAdapter adapter = new EventAdapter(events, new EventAdapter.OnEventActionListener() {
            @Override
            public void onEditClick(Event event, int position) {
                Toast.makeText(EventHome.this, "Edit clicked for: " + event.getEventName(), Toast.LENGTH_SHORT).show();
                // to do
            }

            @Override
            public void onDeleteClick(Event event, int position) {
                Toast.makeText(EventHome.this, "Delete clicked for: " + event.getEventName(), Toast.LENGTH_SHORT).show();
                // to do
            }

            @Override
            public void onStarClick(Event event, int position) {
                Toast.makeText(EventHome.this, "Star clicked for: " + event.getEventName(), Toast.LENGTH_SHORT).show();
                // to do
            }
        });
        recyclerView.setAdapter(adapter);

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_events); // Highlight "Events"
    }
}