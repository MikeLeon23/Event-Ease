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
    private DBHelper dbHelper;

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

        dbHelper = new DBHelper(this);
        List<Event> events = dbHelper.getActiveEvents();
        if (events.isEmpty()) {
            Toast.makeText(this, "No active events found", Toast.LENGTH_SHORT).show();
        }
        // Initialize adapter with action listener
        EventAdapter adapter = new EventAdapter(this, events, new EventAdapter.OnEventActionListener() {
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
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_events); // Highlight "Events"
        // Set up BottomNavigationView using the helper
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationHelper.setupBottomNavigation(this, bottomNavigationView);
    }
}