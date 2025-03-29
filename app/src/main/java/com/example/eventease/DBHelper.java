package com.example.eventease;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.eventease.Notification;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userProfile.db";
    private static final int DATABASE_VERSION = 7;
    public static final String COLUMN_IMAGE_PATH = "image_path"; // Store the image path here

    // USER PROFILE TABLE
    public static final String TABLE_USERS  = "user_profile";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ADDRESS = "address";

    public static final String COLUMN_USER_TYPE = "user_type";

    // EVENT INFO TABLE
    public static final String TABLE_EVENTS = "event_info";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_EVENT_NAME = "event_name";
    public static final String COLUMN_EVENT_LOCATION = "event_location";
    public static final String COLUMN_EVENT_DATE = "event_date";
    public static final String COLUMN_EVENT_TIME = "event_time";
    public static final String COLUMN_EVENT_FEE = "event_fee";
    public static final String COLUMN_EVENT_DESCRIPTION = "event_description";
    public static final String COLUMN_EVENT_REMINDER = "event_reminder";
    public static final String COLUMN_EVENT_SEAT = "event_seat";
    public static final String COLUMN_EVENT_STATUS = "event_status";
    // NOTIFICATIONS TABLE
    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COLUMN_NOTIFICATION_ID = "notification_id";
    public static final String COLUMN_NOTIFICATION_EVENT_ID = "event_id";
    public static final String COLUMN_NOTIFICATION_USER_ID = "user_id";
    public static final String COLUMN_NOTIFICATION_MESSAGE = "message";
    public static final String COLUMN_NOTIFICATION_STATUS = "status"; // pending, accepted, declined

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_USER_TYPE + " TEXT, " +
                COLUMN_IMAGE_PATH + " TEXT)";
        db.execSQL(createUserTable);

        String createEventTable = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_NAME + " TEXT, " +
                COLUMN_EVENT_LOCATION + " TEXT, " +
                COLUMN_EVENT_DATE + " TEXT, " +
                COLUMN_EVENT_TIME + " TEXT, " +
                COLUMN_EVENT_FEE + " REAL, " +
                COLUMN_EVENT_DESCRIPTION + " TEXT, " +
                COLUMN_EVENT_REMINDER + " TEXT, " +
                COLUMN_EVENT_SEAT + " INTEGER, " +
                COLUMN_EVENT_STATUS + " TEXT, " +
                COLUMN_IMAGE_PATH + " TEXT)";
        db.execSQL(createEventTable);

        // by liako created

        String createNotificationTable = "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTIFICATION_EVENT_ID + " INTEGER, " +
                COLUMN_NOTIFICATION_USER_ID + " INTEGER, " +
                COLUMN_NOTIFICATION_MESSAGE + " TEXT, " +
                COLUMN_NOTIFICATION_STATUS + " TEXT DEFAULT 'pending', " +
                "FOREIGN KEY (" + COLUMN_NOTIFICATION_EVENT_ID + ") REFERENCES " + TABLE_EVENTS + "(" + COLUMN_EVENT_ID + "), " +
                "FOREIGN KEY (" + COLUMN_NOTIFICATION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))";
        db.execSQL(createNotificationTable);


        String createTicketsTable = "CREATE TABLE tickets (" +
                "ticket_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "event_id INTEGER, " +
                "FOREIGN KEY(user_id) REFERENCES user_profile(id), " +
                "FOREIGN KEY(event_id) REFERENCES event_info(event_id))";
        db.execSQL(createTicketsTable);

        Log.d("DBHelper", "Database Created Successfully");


        // Create Event Invitations Table
        String CREATE_INVITATION_TABLE = "CREATE TABLE event_invitations (" +
                "invitation_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "event_id INTEGER, " +
                "email TEXT, " +
                "status TEXT, " +
                "FOREIGN KEY(event_id) REFERENCES events(event_id))";
        db.execSQL(CREATE_INVITATION_TABLE);
    }

//To track which users are invited to which events, add a method in your DBHelper class to store invitations.
    public void saveEventInvitation(String eventId, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("event_id", eventId);
        values.put("email", email);
        values.put("status", "pending");  // Default status of the invitation

        long result = db.insert("event_invitations", null, values);
        if (result == -1) {
            Log.e("DBHelper", "Failed to save event invitation");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);

        db.execSQL("DROP TABLE IF EXISTS event_invitations");
        db.execSQL("DROP TABLE IF EXISTS tickets");
        onCreate(db);
    }

// Whenever a new event is created, notifications should be sent to potential attendees.
    public boolean sendNotification(int eventId, int userId, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION_EVENT_ID, eventId);
        values.put(COLUMN_NOTIFICATION_USER_ID, userId);
        values.put(COLUMN_NOTIFICATION_MESSAGE, message);
        values.put(COLUMN_NOTIFICATION_STATUS, "pending"); // Default status

        long result = db.insert(TABLE_NOTIFICATIONS, null, values);
        db.close();
        return result != -1;
    }
//fetch Pending Notifications for a User
    public List<Notification> getNotificationsForUser(int userId) {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, null, COLUMN_NOTIFICATION_USER_ID + "=? AND " + COLUMN_NOTIFICATION_STATUS + "=?",
                new String[]{String.valueOf(userId), "pending"}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int notificationId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_ID));
                int eventId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_EVENT_ID));
                String message = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_MESSAGE));
                String status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTIFICATION_STATUS));

                notifications.add(new Notification(notificationId, eventId, userId, message, status));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return notifications;
    }

//Allow the user to accept or decline a notification status and
// update the ticket count for the event if the status is set to "accepted"

    public boolean updateNotificationStatus(int notificationId, String status, Integer eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTIFICATION_STATUS, status);

        long result = db.update(TABLE_NOTIFICATIONS, values, COLUMN_NOTIFICATION_ID + "=?", new String[]{String.valueOf(notificationId)});

        if (status.equals("accepted") && eventId != null) {
            // If the notification is accepted, update the ticket count for the event
            db.execSQL("UPDATE " + TABLE_EVENTS + " SET " + COLUMN_EVENT_SEAT + " = " + COLUMN_EVENT_SEAT + " - 1 WHERE " + COLUMN_EVENT_ID + " = " + eventId);
        }

        db.close();
        return result != -1;
    }
//public boolean updateNotificationStatus(int notificationId, String status) {
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues values = new ContentValues();
//    values.put(COLUMN_NOTIFICATION_STATUS, status);
//
//    long result = db.update(TABLE_NOTIFICATIONS, values, COLUMN_NOTIFICATION_ID + "=?", new String[]{String.valueOf(notificationId)});
//    db.close();
//    return result != -1;
//}

//update ticket count
//public boolean updateNotificationStatus(int notificationId, String status, int eventId) {
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues values = new ContentValues();
//    values.put(COLUMN_NOTIFICATION_STATUS, status);
//
//    long result = db.update(TABLE_NOTIFICATIONS, values, COLUMN_NOTIFICATION_ID + "=?", new String[]{String.valueOf(notificationId)});
//
//    // If the user accepted, reduce the available seats
//    if (status.equals("accepted")) {
//        db.execSQL("UPDATE " + TABLE_EVENTS + " SET " + COLUMN_EVENT_SEAT + " = " + COLUMN_EVENT_SEAT + " - 1 WHERE " + COLUMN_EVENT_ID + " = " + eventId);
//    }
//
//    db.close();
//    return result != -1;
//}

    // Insert user data into the database
    public boolean insertUserData(String name, String email, String phone, String password, String address, String userType, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        // ContentValues to insert user data
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_USER_TYPE, userType);
        contentValues.put(COLUMN_IMAGE_PATH, imagePath);

        // Insert data into the database
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;  // Returns true if insertion was successful, false otherwise
    }

    // Get user by username (email) and password
    public User getUserByUsernameAndPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {

            // Logging the data
            Log.d("DBHelper", "User found: " + email);
            // Retrieve column indices
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int phoneIndex = cursor.getColumnIndex(COLUMN_PHONE);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int addressIndex = cursor.getColumnIndex(COLUMN_ADDRESS);
            int userTypeIndex = cursor.getColumnIndex(COLUMN_USER_TYPE);
            int imagePathIndex = cursor.getColumnIndex(COLUMN_IMAGE_PATH);

            // Check if the column indices are valid (>= 0)
            if (idIndex >= 0 && nameIndex >= 0 && phoneIndex >= 0 && passwordIndex >= 0 && addressIndex >= 0 && imagePathIndex >= 0) {
                // Retrieve user data from cursor using valid column indices
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String phone = cursor.getString(phoneIndex);
                String address = cursor.getString(addressIndex);
                String userType = cursor.getString(userTypeIndex);
                String imagePath = cursor.getString(imagePathIndex);

                // Close the cursor
                cursor.close();

                // Return an Attendee object with the retrieved data
                return new User(id, name, email, phone, password, address, userType,imagePath);
            } else {
                cursor.close();
                return null; // Invalid column index (should not happen if database schema is correct)
            }
        }

        cursor.close();
        Log.d("DBHelper", "No user found with email: " + email);
        return null; // Return null if no matching user was found
    }

    // Method to load notifications based on logged-in user's email
    public List<Notification> loadNotificationsByEmail(String email) {
        List<Notification> notificationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get event invitations based on the logged-in user's email
        String query = "SELECT * FROM " + TABLE_NOTIFICATIONS + " WHERE email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Ensure valid column indices
                int invitationIdIndex = cursor.getColumnIndex("invitation_id");
                int eventIdIndex = cursor.getColumnIndex("event_id");
                int emailIndex = cursor.getColumnIndex("email");
                int statusIndex = cursor.getColumnIndex("status");

                if (invitationIdIndex != -1 && eventIdIndex != -1 && emailIndex != -1 && statusIndex != -1) {
                    int invitationId = cursor.getInt(invitationIdIndex);
                    int eventId = cursor.getInt(eventIdIndex);
                    String status = cursor.getString(statusIndex);

                    String message = "You are invited to event: " + eventId;
                    notificationList.add(new Notification(invitationId, eventId, 0, message, status));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        return notificationList;
    }

    public boolean updateUserData(String id, String name, String email, String phone, String password, String address, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_IMAGE_PATH, imagePath); // Store the image path

        long result = db.update(TABLE_USERS , values, COLUMN_ID + " = ?", new String[]{id});
        db.close();
        return result != -1;
    }

    public Cursor getUserById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS , null, COLUMN_ID + " = ?", new String[]{id}, null, null, null);
    }

    public long insertEventData(String name, String location, String date, String time,
                                   double fee, String description, String reminder, int seat, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EVENT_NAME, name);
        values.put(COLUMN_EVENT_LOCATION, location);
        values.put(COLUMN_EVENT_DATE, date);
        values.put(COLUMN_EVENT_TIME, time);
        values.put(COLUMN_EVENT_FEE, fee);
        values.put(COLUMN_EVENT_DESCRIPTION, description);
        values.put(COLUMN_EVENT_REMINDER, reminder);
        values.put(COLUMN_EVENT_SEAT, seat);
        values.put(COLUMN_EVENT_STATUS, "active"); // Assuming "active" is the default event status
        values.put(COLUMN_IMAGE_PATH, imagePath);  // Path to image if any

        long result = db.insert(TABLE_EVENTS, null, values);
        db.close(); // Always close the database connection after the operation

        return result; // Return the event ID (long), or -1 if insert failed
    }

    public Event getEventById(String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, "event_id = ?" + eventId, new String[]{eventId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Event event = new Event(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TIME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_EVENT_FEE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_REMINDER)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_SEAT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS))
            );
            cursor.close();
            return event;
        }

        return null;
    }
    public boolean updateEventData(String eventId, String name, String location, String date, String time,
                                   double fee, String description, String reminder, int seat, String status, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, name);
        values.put(COLUMN_EVENT_LOCATION, location);
        values.put(COLUMN_EVENT_DATE, date);
        values.put(COLUMN_EVENT_TIME, time);
        values.put(COLUMN_EVENT_FEE, fee);
        values.put(COLUMN_EVENT_DESCRIPTION, description);
        values.put(COLUMN_EVENT_REMINDER, reminder);
        values.put(COLUMN_EVENT_SEAT, seat);
        values.put(COLUMN_EVENT_STATUS, status);
        values.put(COLUMN_IMAGE_PATH, imagePath);

        long result = db.update(TABLE_EVENTS, values, COLUMN_EVENT_ID + " = ?", new String[]{eventId});
        db.close();
        return result != -1;
    }

    public boolean deleteEvent(String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_EVENTS, COLUMN_EVENT_ID + " = ?", new String[]{eventId});
        db.close();
        return result != -1;
    }

    public List<Event> getActiveEvents() {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "event_status = ?";
        String[] selectionArgs = {"active"};

        Cursor cursor = db.query(TABLE_EVENTS, null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Event event = new Event(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TIME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_EVENT_FEE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_REMINDER)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_SEAT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS))
                );
                events.add(event);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return events;
    }


}
