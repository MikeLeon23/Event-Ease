package com.example.eventease;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView notificationsRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        dbHelper = new DBHelper(this);
        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize notification list
        notificationList = new ArrayList<>();

        // Sample: Fetch notifications for the current user
        loadNotificationsFromDatabase();

        // Set adapter
        notificationAdapter = new NotificationAdapter(notificationList);
        notificationsRecyclerView.setAdapter(notificationAdapter);
    }

    private void loadNotificationsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query event_invitations to get notifications for the current user
        String query = "SELECT * FROM event_invitations WHERE email = ?";  // Replace '?' with current user's email
        Cursor cursor = db.rawQuery(query, new String[]{"user@example.com"});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Ensure the column indices are valid
                int invitationIdIndex = cursor.getColumnIndex("invitation_id");
                int eventIdIndex = cursor.getColumnIndex("event_id");
                int emailIndex = cursor.getColumnIndex("email");
                int statusIndex = cursor.getColumnIndex("status");

                if (invitationIdIndex != -1 && eventIdIndex != -1 && emailIndex != -1 && statusIndex != -1) {
                    int invitationId = cursor.getInt(invitationIdIndex);
                    int eventId = cursor.getInt(eventIdIndex);
                    String email = cursor.getString(emailIndex);
                    String status = cursor.getString(statusIndex);
                    String message = "You are invited to event: " + eventId;

                    notificationList.add(new Notification(invitationId, eventId, 0, message, status));
                } else {
                    // Handle the case where any column is missing
                    Log.e("NotificationActivity", "Column not found in the result set");
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

}