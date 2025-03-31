package com.example.eventease;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class EventHome extends AppCompatActivity {
    private DBHelper dbHelper;
    private EventAdapter adapter;
    private TabLayout tabLayout;
    private SharedPreferences prefs;
    private String userId;
    private EditText searchText, searchDate, searchOrganizer;
    private ImageView arrowToggle, searchIcon;
    private LinearLayout additionalSearchFields;
    private boolean isSearchExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_home);

        // Initialize views
        searchText = findViewById(R.id.search_text);
        searchDate = findViewById(R.id.search_date);
        searchOrganizer = findViewById(R.id.search_organizer);
        arrowToggle = findViewById(R.id.arrow_toggle);
        searchIcon = findViewById(R.id.search_icon);
        additionalSearchFields = findViewById(R.id.additional_search_fields);

        prefs = EventHome.this.getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = prefs.getString("user_id", null);

        // Set up TabLayout
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.getTabAt(0).select(); // Select "ALL" tab by default

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        List<Event> events = dbHelper.getAllActiveEventsByUserId(userId, null, null, null);
        adapter = new EventAdapter(this, events, new EventAdapter.OnEventActionListener() {
            @Override
            public void onEditClick(Event event, int position) {
                Toast.makeText(EventHome.this, "Edit clicked for: " + event.getEventName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Event event, int position) {
                Toast.makeText(EventHome.this, "Delete clicked for: " + event.getEventName(), Toast.LENGTH_SHORT).show();
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
                updateEventList();
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

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationHelper.setupBottomNavigation(this, bottomNavigationView);

        // Arrow toggle click listener
        arrowToggle.setOnClickListener(v -> {
            if (isSearchExpanded) {
                // Collapse the additional fields
                additionalSearchFields.setVisibility(View.GONE);
                arrowToggle.setImageResource(R.drawable.ic_arrow_down);
                // Clear the additional fields
                searchDate.setText("");
                searchOrganizer.setText("");
                isSearchExpanded = false;
                updateEventList(); // Refresh the list with cleared criteria
            } else {
                // Expand the additional fields
                additionalSearchFields.setVisibility(View.VISIBLE);
                arrowToggle.setImageResource(R.drawable.ic_arrow_up);
                isSearchExpanded = true;
            }
        });

        // Search icon click listener
        searchIcon.setOnClickListener(v -> updateEventList());

        // Set up clear functionality for EditText fields
        setupClearButton(searchText);
        setupClearButton(searchDate);
        setupClearButton(searchOrganizer);
    }

    // Method to set up clear button functionality for an EditText
    private void setupClearButton(EditText editText) {
        // Show/hide clear button based on text input
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Toggle the visibility of the clear icon by changing its tint
                editText.setCompoundDrawableTintList(ContextCompat.getColorStateList(
                        EventHome.this,
                        s.length() > 0 ? android.R.color.black : android.R.color.transparent
                ));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Clear text when the clear button (drawableEnd) is clicked
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                // Check if the touch is on the drawableEnd (right side)
                if (event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width() - editText.getPaddingRight())) {
                    editText.setText("");
                    updateEventList(); // Refresh the list after clearing
                    return true;
                }
            }
            return false;
        });
    }

    // Method to update the event list based on the selected tab and search criteria
    private void updateEventList() {
        String eventName = searchText.getText().toString().trim();
        String eventDate = searchDate.getText().toString().trim();
        String organizerName = searchOrganizer.getText().toString().trim();
        int selectedTab = tabLayout.getSelectedTabPosition();
        List<Event> events;

        if (selectedTab == 0) {
            // ALL tab: Search all active events with the given criteria
            events = dbHelper.getAllActiveEventsByUserId(userId, eventName, eventDate, organizerName);
        } else {
            // Interested tab: Search interested events with the given criteria
            events = dbHelper.getInterestedEventsByUserId(userId, eventName, eventDate, organizerName);
        }
        adapter.updateEvents(events);
    }
}