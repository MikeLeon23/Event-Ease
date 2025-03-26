package com.example.eventease;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        50, "http://example.com/asdfadf.jpg", ""));
        events.add(new Event("4123478", "Super Star Activity", "Royal City", "Wed, Feb 5",
                "7 pm", 9.99, "\"Laugh Track\" is a high-energy, fast-paced comedy show", "",
                50, "http://example.com/asdfadf.jpg", ""));
        EventAdapter adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_events); // Highlight "Events"
    }
}