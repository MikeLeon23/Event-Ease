package com.example.eventease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class TicketsActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private TicketAdapter adapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        // Get userId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        userId = prefs.getString("user_id", null);
        if (userId == null) {
            // Redirect to login if not logged in
            finish();
            return;
        }

        dbHelper = new DBHelper(this);

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.ticket_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TicketAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Load all tickets
        updateTicketList();

        // Set up BottomNavigationView using the helper
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationHelper.setupBottomNavigation(this, bottomNavigationView);

//        // Set up BottomNavigationView
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setSelectedItemId(R.id.nav_tickets);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_events:
//                    startActivity(new Intent(TicketsActivity.this, EventHome.class));
//                    finish();
//                    return true;
//                case R.id.nav_tickets:
//                    return true;
//                case R.id.nav_account:
//                    startActivity(new Intent(TicketsActivity.this, AccountActivity.class));
//                    finish();
//                    return true;
//                default:
//                    return false;
//            }
//        });
    }

    private void updateTicketList() {
        List<Ticket> tickets = dbHelper.getTicketsByUserId(userId);
        adapter.updateTickets(tickets);
    }
}