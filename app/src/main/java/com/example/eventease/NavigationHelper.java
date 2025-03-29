package com.example.eventease;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {

    public static void setupBottomNavigation(Activity activity, BottomNavigationView bottomNavigationView) {
        // Set the selected item based on the current activity
        if (activity instanceof EventHome) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_events);
        } else if (activity instanceof AttendeeAccountManage || activity instanceof OrganizerAccountManage) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_account);
        }
//        else if (activity instanceof TicketsActivity) {
//            bottomNavigationView.setSelectedItemId(R.id.navigation_tickets);
//        } else if (activity instanceof AccountActivity) {
//            bottomNavigationView.setSelectedItemId(R.id.navigation_account);
//        }

        // Set up the navigation listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            SharedPreferences prefs = activity.getSharedPreferences("UserInfo", MODE_PRIVATE);
            String userId = prefs.getString("user_id", null);
            String userType = prefs.getString("user_type", null);

            // If user is not logged in, redirect to login
            if (userId == null || userType == null) {
                Intent intent = new Intent(activity, Login.class);
                activity.startActivity(intent);
                activity.finish();
                return true;
            }

            // If the selected item is the current activity, do nothing
            if (itemId == R.id.navigation_events && activity instanceof EventHome) {
                return true;
            } else if (itemId == R.id.navigation_account && (activity instanceof AttendeeAccountManage || activity instanceof OrganizerAccountManage)) {
                return true;
            }
//            else if (itemId == R.id.navigation_tickets && activity instanceof TicketsActivity) {
//                return true;
//            } else if (itemId == R.id.navigation_account && activity instanceof AccountActivity) {
//                return true;
//            }

            // Navigate to the appropriate activity
            if (itemId == R.id.navigation_events) {
                Intent intent = new Intent(activity, EventHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
                activity.finish();
                return true;
            } else if (itemId == R.id.navigation_tickets) {
                //
                return true;
            } else if (itemId == R.id.navigation_account) {
                Intent intent;
                if ("Organizer".equals(userType)) {
                    intent = new Intent(activity, OrganizerAccountManage.class);
                } else if ("Attendee".equals(userType)) {
                    intent = new Intent(activity, AttendeeAccountManage.class);
                } else {
                    // Unknown user type, redirect to login
                    intent = new Intent(activity, Login.class);
                }

                intent.putExtra("COLUMN_ID", userId); // Pass the user ID
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
                activity.finish();
                return true;
            }
//            else if (itemId == R.id.navigation_tickets) {
//                Intent intent = new Intent(activity, TicketsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                activity.startActivity(intent);
//                activity.finish();
//                return true;
//            } else if (itemId == R.id.navigation_account) {
//                Intent intent = new Intent(activity, AccountActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                activity.startActivity(intent);
//                activity.finish();
//                return true;
//            }
            return false;
        });
    }
}