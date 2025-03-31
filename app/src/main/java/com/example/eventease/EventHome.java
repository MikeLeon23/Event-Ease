package com.example.eventease;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class EventHome extends AppCompatActivity {
    private DBHelper dbHelper;
    private EventAdapter adapter;
    private TabLayout tabLayout;
    private SharedPreferences prefs;
    private String userId;
    private EditText searchText;
    private ImageView menuIcon;
    private ImageView searchIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_home);

        menuIcon = findViewById(R.id.hamburger_menu);
        searchText = findViewById(R.id.search_text);
        searchIcon = findViewById(R.id.search_icon);
        prefs = EventHome.this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = prefs.getString("user_id", null);

        // Set up TabLayout
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.getTabAt(0).select(); // Select "ALL" tab by default

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        List<Event> events = dbHelper.getAllActiveEventsByUserId(userId, null);
        // Initialize adapter with action listener
        adapter = new EventAdapter(this, events, new EventAdapter.OnEventActionListener() {
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
                boolean newStatus = !event.isInterested();
                if (!event.isInterested()) {
                    dbHelper.insertInterestedEvent(userId, event.getEventId());
                } else {
                    dbHelper.deleteInterestedEventById(userId, event.getEventId());
                }
                event.setInterested(newStatus);
                updateEventList(); // Refresh the list after toggling
            }
        });
        recyclerView.setAdapter(adapter);

        // TabLayout listener for switching tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateEventList();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set up BottomNavigationView using the helper
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationHelper.setupBottomNavigation(this, bottomNavigationView);

        String str = searchText.getText().toString();
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEventList();
            }
        });
    }
    // Method to update the event list based on the selected tab
    private void updateEventList() {
        String str = searchText.getText().toString();
        int selectedTab = tabLayout.getSelectedTabPosition();
        List<Event> events;
        if (selectedTab == 0) {
            events = dbHelper.getAllActiveEventsByUserId(userId, str); // ALL tab
        } else {
            events = dbHelper.getInterestedEventsByUserId(userId, str); // Interested tab
        }
        adapter.updateEvents(events);
    }
}