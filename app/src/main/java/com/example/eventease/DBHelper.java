package com.example.eventease;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userProfile.db";
    private static final int DATABASE_VERSION = 17;
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
    public static final String COLUMN_ORGANIZER_ID = "organizer_id";
    public static final String COLUMN_CHECK = "event_checked";

    // NOTIFICATIONS TABLE
    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COLUMN_NOTIFICATION_ID = "notification_id";
    public static final String COLUMN_NOTIFICATION_EVENT_ID = "event_id";
    public static final String COLUMN_NOTIFICATION_USER_ID = "user_id";
    public static final String COLUMN_NOTIFICATION_MESSAGE = "message";
    public static final String COLUMN_NOTIFICATION_STATUS = "status"; // pending, accepted, declined

    // INTEREST TABLE
    public static final String TABLE_INTERESTED_EVENTS = "interested_events";
    public static final String COLUMN_INTEREST_ID = "interest_id";
    public static final String COLUMN_INTEREST_USER_ID = "interest_user_id";
    public static final String COLUMN_INTEREST_EVENT_ID = "interest_event_id";

    // TICKET TABLE
    public static final String TABLE_TICKET = "tickets";
    public static final String COLUMN_TICKET_ID = "ticket_id";
    public static final String COLUMN_TICKET_USER_ID = "ticket_user_id";
    public static final String COLUMN_TICKET_EVENT_ID = "ticket_event_id";

    // EVENT INVITATIONS TABLE (already exists)
    public static final String TABLE_EVENT_INVITATIONS = "event_invitations";
    public static final String COLUMN_INVITATION_ID = "invitation_id";
    public static final String COLUMN_INVITATION_EVENT_ID = "event_id";
    public static final String COLUMN_INVITATION_EMAIL = "email";
    public static final String COLUMN_INVITATION_STATUS = "status";

    // TABLE OTP FOR PASSWORD RESET
    private static final String TABLE_OTP = "otp_password_reset";
    private static final String COLUMN_OTP_ID = "otp_id";
    private static final String COLUMN_EMAIL_RESET = "email_reset";
    private static final String COLUMN_NEW_PASSWORD = "new_password";
    private static final String COLUMN_OTP = "otp";


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
                COLUMN_IMAGE_PATH + " TEXT, " +
                COLUMN_ORGANIZER_ID + " TEXT, " +
                COLUMN_CHECK + " INTEGER)";
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

        // Create Event Invitations Table
        String CREATE_INVITATION_TABLE = "CREATE TABLE event_invitations (" +
                "invitation_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "event_id INTEGER, " +
                "email TEXT, " +
                "status TEXT, " +
                "FOREIGN KEY(event_id) REFERENCES events(event_id))";
        db.execSQL(CREATE_INVITATION_TABLE);

        // Create interested events table
        String CREATE_INTERESTED_EVENT_TABLE = "CREATE TABLE " + TABLE_INTERESTED_EVENTS + "(" +
                COLUMN_INTEREST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_INTEREST_USER_ID + " INTEGER, " +
                COLUMN_INTEREST_EVENT_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_INTEREST_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY (" + COLUMN_INTEREST_EVENT_ID + ") REFERENCES " + TABLE_EVENTS + "(" + COLUMN_EVENT_ID + "))";
        db.execSQL(CREATE_INTERESTED_EVENT_TABLE);

        // Create ticket table
        String CREATE_TICKET_TABLE = "CREATE TABLE " + TABLE_TICKET + "(" +
                COLUMN_TICKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TICKET_USER_ID + " INTEGER, " +
                COLUMN_TICKET_EVENT_ID + " INTEGER, " +
                "FOREIGN KEY (" + COLUMN_TICKET_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY (" + COLUMN_TICKET_EVENT_ID + ") REFERENCES " + TABLE_EVENTS + "(" + COLUMN_EVENT_ID + "))";
        db.execSQL(CREATE_TICKET_TABLE);

        String CREATE_OTP_TABLE = "CREATE TABLE " + TABLE_OTP + " (" +
                COLUMN_OTP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL_RESET + " TEXT UNIQUE, " +
                COLUMN_NEW_PASSWORD + " TEXT, " +
                COLUMN_OTP + " TEXT)";
        db.execSQL(CREATE_OTP_TABLE);

        Log.d("DBHelper", "Database Created Successfully");
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERESTED_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OTP);

        db.execSQL("DROP TABLE IF EXISTS event_invitations");
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

    // Insert event data and save invitations
    public boolean insertEventData(String name, String location, String date, String time,
                                   double fee, String description, String reminder, int seat,
                                   String status, String imagePath, String organizerId,
                                   boolean isChecked, String invitedEmails) {
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
        values.put(COLUMN_ORGANIZER_ID, organizerId);
        values.put(COLUMN_CHECK, isChecked ? 1 : 0);

        long eventId = db.insert(TABLE_EVENTS, null, values);
        if (eventId == -1) {
            Log.e("DBHelper", "Failed to insert event: " + name);
            db.close();
            return false;
        }

        // Save invitations if provided
        if (invitedEmails != null && !invitedEmails.trim().isEmpty()) {
            String[] emails = invitedEmails.split(",");
            for (String email : emails) {
                email = email.trim();
                if (!email.isEmpty()) {
                    saveEventInvitation(String.valueOf(eventId), email);
                }
            }
        }

        db.close();
        return true;
    }

    public Cursor getEventById(String eventId){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_EVENTS , null, COLUMN_EVENT_ID + " = ?", new String[]{eventId}, null, null, null);
    }

    public Event getEventObjById(String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, null, "event_id = ?", new String[]{eventId}, null, null, null);

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
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORGANIZER_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECK)) == 1
            );
            cursor.close();
            return event;
        }

        return null;
    }

    public String getEventImagePath(String eventId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String imagePath = null;

        Cursor cursor = db.query(TABLE_EVENTS, new String[]{COLUMN_IMAGE_PATH}, COLUMN_EVENT_ID + " = ?", new String[]{eventId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH));
            cursor.close();
        }
        db.close();
        return imagePath;
    }

    public String getUserImagePath(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String imagePath = null;

        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_IMAGE_PATH}, COLUMN_ID + " = ?", new String[]{userId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH));
            cursor.close();
        }
        db.close();
        return imagePath;
    }

    // Update event data and invitations
    public boolean updateEventData(String eventId, String name, String location, String date, String time,
                                   double fee, String description, String reminder, int seat,
                                   String status, String imagePath, String organizerId,
                                   boolean isChecked, String invitedEmails) {
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
        values.put(COLUMN_ORGANIZER_ID, organizerId);
        values.put(COLUMN_CHECK, isChecked ? 1 : 0);

        long result = db.update(TABLE_EVENTS, values, COLUMN_EVENT_ID + " = ?", new String[]{eventId});
        if (result == -1) {
            Log.e("DBHelper", "Failed to update event: " + eventId);
            db.close();
            return false;
        }

        // Update invitations: Delete existing and re-insert
        db.delete(TABLE_EVENT_INVITATIONS, COLUMN_INVITATION_EVENT_ID + " = ?", new String[]{eventId});
        if (invitedEmails != null && !invitedEmails.trim().isEmpty()) {
            String[] emails = invitedEmails.split(",");
            for (String email : emails) {
                email = email.trim();
                if (!email.isEmpty()) {
                    saveEventInvitation(eventId, email);
                }
            }
        }

        db.close();
        return true;
    }

    // Fetch events where the user is invited
    public List<Event> getInvitedEventsByEmail(String email) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT e.* " +
                "FROM " + TABLE_EVENTS + " e " +
                "JOIN " + TABLE_EVENT_INVITATIONS + " ei " +
                "ON e." + COLUMN_EVENT_ID + " = ei." + COLUMN_INVITATION_EVENT_ID + " " +
                "WHERE ei." + COLUMN_INVITATION_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORGANIZER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECK)) == 1
                );
                events.add(event);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        Log.d("DBHelper", "Invited Events Retrieved: " + events.size() + " for email=" + email);
        return events;
    }

    public boolean deleteEvent(String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        // Begin a transaction to ensure atomicity
        db.beginTransaction();
        try {
            // 1. Delete related tickets from the tickets table
            int ticketsDeleted = db.delete(TABLE_TICKET,
                    COLUMN_TICKET_EVENT_ID + " = ?",
                    new String[]{eventId});
            Log.d("DBHelper", "deleteEvent: Deleted " + ticketsDeleted + " tickets for eventId=" + eventId);

            // 2. Delete related interested events from the interested_events table
            int interestedEventsDeleted = db.delete(TABLE_INTERESTED_EVENTS,
                    COLUMN_INTEREST_EVENT_ID + " = ?",
                    new String[]{eventId});
            Log.d("DBHelper", "deleteEvent: Deleted " + interestedEventsDeleted + " interested events for eventId=" + eventId);

            // 3. Delete related notifications from the notifications table
            int notificationsDeleted = db.delete(TABLE_NOTIFICATIONS,
                    COLUMN_NOTIFICATION_EVENT_ID + " = ?",
                    new String[]{eventId});
            Log.d("DBHelper", "deleteEvent: Deleted " + notificationsDeleted + " notifications for eventId=" + eventId);

            // 4. Delete related event invitations from the event_invitations table
            int invitationsDeleted = db.delete("event_invitations",
                    "event_id = ?",
                    new String[]{eventId});
            Log.d("DBHelper", "deleteEvent: Deleted " + invitationsDeleted + " event invitations for eventId=" + eventId);

            // 5. Delete the event itself from the event_info table
            int eventsDeleted = db.delete(TABLE_EVENTS,
                    COLUMN_EVENT_ID + " = ?",
                    new String[]{eventId});
            Log.d("DBHelper", "deleteEvent: Deleted " + eventsDeleted + " event(s) for eventId=" + eventId);

            // If the event was deleted successfully, mark the transaction as successful
            if (eventsDeleted > 0) {
                success = true;
                db.setTransactionSuccessful();
            } else {
                Log.e("DBHelper", "deleteEvent: No event found with eventId=" + eventId);
            }
        } catch (SQLiteException e) {
            Log.e("DBHelper", "deleteEvent: Error deleting event or related data", e);
            success = false;
        } finally {
            // End the transaction (commit if successful, rollback if not)
            db.endTransaction();
            db.close();
        }

        return success;
    }

//    public List<Event> getActiveEventsByAttendee(String attendeeId) {
//        List<Event> events = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selection = "attendee_id = ? AND event_status = ?"; //BE CAREFUL WITH ID's NAME
//        String[] selectionArgs = {attendeeId, "enable"};
//
//        Cursor cursor = db.query(TABLE_EVENTS, null, selection, selectionArgs, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                Event event = new Event(
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_LOCATION)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATE)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TIME)),
//                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_EVENT_FEE)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_REMINDER)),
//                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_SEAT)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORGANIZER_ID)),
//                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECK)) ==1
//                );
//                events.add(event);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//
//        db.close();
//        // Log the number of events retrieved
//        Log.d("DBHelper", "Active Events Retrieved: " + events.size());
//        return events;
//    }

//    public List<Event> getPastEventsByAttendee(String attendeeId) {
//        List<Event> events = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String selection = "attendee_id = ? AND event_status = ?"; //BE CAREFUL WITH ID's NAME
//        String[] selectionArgs = {attendeeId, "disable"};
//
//        Cursor cursor = db.query(TABLE_EVENTS, null, selection, selectionArgs, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                Event event = new Event(
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_LOCATION)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATE)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TIME)),
//                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_EVENT_FEE)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DESCRIPTION)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_REMINDER)),
//                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_SEAT)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
//                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORGANIZER_ID)),
//                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECK)) ==1
//                );
//                events.add(event);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//
//        db.close();
//        // Log the number of events retrieved
//        Log.d("DBHelper", "Past Events Retrieved: " + events.size());
//        return events;
//    }

    public List<Event> getActiveEventsByOrganizer(String organizerId) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "organizer_id = ? AND (event_status = ? OR event_status = ?)";
        String[] selectionArgs = {organizerId, "enable", "draft"};

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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORGANIZER_ID)),
                       cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECK)) ==1
                );
                events.add(event);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        // Log the number of events retrieved
        Log.d("DBHelper", "Active Events Retrieved: " + events.size());
        return events;
    }

    public List<Event> getPastEventsByOrganizer(String organizerId) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "organizer_id = ? AND event_status = ?";
        String[] selectionArgs = {organizerId, "disable"};

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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORGANIZER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECK)) ==1
                );
                events.add(event);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        // Log the number of events retrieved
        Log.d("DBHelper", "Past Events Retrieved: " + events.size());
        return events;
    }

    public boolean insertInterestedEvent(String userId, String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_INTEREST_USER_ID, userId);
        values.put(COLUMN_INTEREST_EVENT_ID, eventId);

        long result = db.insert(TABLE_INTERESTED_EVENTS, null, values);
        db.close();
        return result != -1;
    }

    public boolean deleteInterestedEventById(String userId, String eventId) {
        // Validate inputs
        if (userId == null || userId.trim().isEmpty() || eventId == null || eventId.trim().isEmpty()) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            int result = db.delete(TABLE_INTERESTED_EVENTS,
                    COLUMN_INTEREST_USER_ID + " = ? AND " + COLUMN_INTEREST_EVENT_ID + " = ?",
                    new String[]{userId, eventId});
            success = result > 0;
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        return success;
    }

    public List<Event> getAllActiveEventsByUserId(String userId, String eventName, String eventDate, String organizerName) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT e.*, " +
                " CASE" +
                " WHEN ie." + COLUMN_INTEREST_USER_ID + " IS NOT NULL THEN 1" +
                " ELSE 0" +
                " END AS is_interested" +
                " FROM " + TABLE_EVENTS + " e" +
                " LEFT JOIN " + TABLE_INTERESTED_EVENTS + " ie" +
                " ON e." + COLUMN_EVENT_ID + " = ie." + COLUMN_INTEREST_EVENT_ID + " AND ie." + COLUMN_INTEREST_USER_ID + " = ?" +
                " LEFT JOIN " + TABLE_USERS + " u" +
                " ON e." + COLUMN_ORGANIZER_ID + " = u." + COLUMN_ID +
                " WHERE e." + COLUMN_EVENT_STATUS + " = 'enable'";

        // Add search conditions
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(userId); // Add userId as the first argument for the JOIN condition

        if (eventName != null && !eventName.trim().isEmpty()) {
            query += " AND e." + COLUMN_EVENT_NAME + " LIKE ?";
            selectionArgs.add("%" + eventName.trim() + "%");
        }
        if (eventDate != null && !eventDate.trim().isEmpty()) {
            query += " AND e." + COLUMN_EVENT_DATE + " = ?";
            selectionArgs.add(eventDate.trim());
        }
        if (organizerName != null && !organizerName.trim().isEmpty()) {
            query += " AND u." + COLUMN_NAME + " LIKE ?";
            selectionArgs.add("%" + organizerName.trim() + "%");
        }

        Cursor cursor = db.rawQuery(query, selectionArgs.toArray(new String[0]));

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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORGANIZER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECK)) == 1
                );
                event.setInterested(cursor.getInt(cursor.getColumnIndexOrThrow("is_interested")) == 1);
                events.add(event);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return events;
    }
    public List<Event> getInterestedEventsByUserId(String userId, String eventName, String eventDate, String organizerName) {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT e.*, " +
                " CASE" +
                " WHEN ie." + COLUMN_INTEREST_USER_ID + " IS NOT NULL THEN 1" +
                " ELSE 0" +
                " END AS is_interested" +
                " FROM " + TABLE_EVENTS + " e" +
                " INNER JOIN " + TABLE_INTERESTED_EVENTS + " ie" +
                " ON e." + COLUMN_EVENT_ID + " = ie." + COLUMN_INTEREST_EVENT_ID +
                " LEFT JOIN " + TABLE_USERS + " u" +
                " ON e." + COLUMN_ORGANIZER_ID + " = u." + COLUMN_ID +
                " WHERE ie." + COLUMN_INTEREST_USER_ID + " = ?";

        // Add search conditions
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(userId); // Add userId as the first argument for the JOIN condition

        if (eventName != null && !eventName.trim().isEmpty()) {
            query += " AND e." + COLUMN_EVENT_NAME + " LIKE ?";
            selectionArgs.add("%" + eventName.trim() + "%");
        }
        if (eventDate != null && !eventDate.trim().isEmpty()) {
            query += " AND e." + COLUMN_EVENT_DATE + " = ?";
            selectionArgs.add(eventDate.trim());
        }
        if (organizerName != null && !organizerName.trim().isEmpty()) {
            query += " AND u." + COLUMN_NAME + " LIKE ?";
            selectionArgs.add("%" + organizerName.trim() + "%");
        }

        Cursor cursor = db.rawQuery(query, selectionArgs.toArray(new String[0]));

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
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORGANIZER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CHECK)) == 1
                );
                event.setInterested(cursor.getInt(cursor.getColumnIndexOrThrow("is_interested")) == 1);
                events.add(event);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return events;
    }

    public boolean insertTicket(String userId, String eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TICKET_USER_ID, userId);
        values.put(COLUMN_TICKET_EVENT_ID, eventId);

        long result = db.insert(TABLE_TICKET, null, values);
        db.close();
        return result != -1;
    }

    public boolean deleteTicket(String userId, String eventId) {
        // Validate inputs
        if (userId == null || userId.trim().isEmpty() || eventId == null || eventId.trim().isEmpty()) {
            Log.e("DBHelper", "deleteTicket: Invalid input (userId=" + userId + ", eventId=" + eventId + ")");
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            int result = db.delete(TABLE_TICKET,
                    COLUMN_TICKET_USER_ID + " = ? AND " + COLUMN_TICKET_EVENT_ID + " = ?",
                    new String[]{userId, eventId});
            success = result > 0;
            Log.d("DBHelper", "deleteTicket: Deleted " + result + " row(s) for userId=" + userId + " and eventId=" + eventId);
        } catch (SQLiteException e) {
            Log.e("DBHelper", "deleteTicket: Error deleting ticket", e);
            e.printStackTrace();
        } finally {
            db.close(); // Ensure database is closed properly
        }

        return success;
    }

//    public boolean deleteTicketsss(String userId, String eventId) {
//        // Validate inputs
//        if (userId == null || userId.trim().isEmpty() || eventId == null || eventId.trim().isEmpty()) {
//            Log.e("DBHelper", "deleteTicket: Invalid input (userId=" + userId + ", eventId=" + eventId + ")");
//            return false;
//        }
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        boolean success = false;
//
//        try {
//            int result = db.delete(TABLE_TICKET,
//                    COLUMN_TICKET_USER_ID + " = ? AND " + COLUMN_TICKET_ID + " = ?",
//                    new String[]{userId, eventId});
//            success = result > 0;
//            Log.d("DBHelper", "deleteTicket: Deleted " + result + " row(s) for userId=" + userId + " and eventId=" + eventId);
//        } catch (SQLiteException e) {
//            Log.e("DBHelper", "deleteTicket: Error deleting ticket", e);
//            e.printStackTrace();
//        }finally {
//            db.close(); // Ensure database is closed properly
//        }
//
//        return success;
//    }



    public List<Ticket> getTicketsByUserId(String userId) {
        List<Ticket> tickets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query =  "SELECT t." + COLUMN_TICKET_ID +
                ", u.*" +
                ", e.*" +
                " FROM " + TABLE_TICKET + " t" +
                " JOIN " + TABLE_USERS + " u" +
                    " ON" + " t." + COLUMN_TICKET_USER_ID + " = u." + COLUMN_ID +
                " JOIN " + TABLE_EVENTS + " e" +
                    " ON" + " t." + COLUMN_TICKET_EVENT_ID + " = e." + COLUMN_EVENT_ID +
                " WHERE" + " t." + COLUMN_TICKET_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{userId});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                );
                Event event = new Event(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_STATUS))
                );
                Ticket ticket = new Ticket(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TICKET_ID)),
                        user,
                        event
                );
                tickets.add(ticket);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return tickets;
    }

    public List<User> getAttendeesByOrganizerId(String organizerId) {
        List<User> attendees = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT u." + COLUMN_ID + ", u." + COLUMN_NAME +
                " FROM " + TABLE_USERS + " u" +
                " JOIN " + TABLE_TICKET + " t ON u." + COLUMN_ID + " = t." + COLUMN_TICKET_USER_ID +
                " JOIN " + TABLE_EVENTS + " e ON t." + COLUMN_TICKET_EVENT_ID + " = e." + COLUMN_EVENT_ID +
                " WHERE e." + COLUMN_ORGANIZER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{organizerId});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User attendee = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                );
                attendees.add(attendee);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return attendees;
    }

    public List<User> getAttendeesByOrganizerIdAndStatus(String organizerId, String eventStatus) {
        List<User> attendees = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT DISTINCT u." + COLUMN_ID + ", u." + COLUMN_NAME +
                " FROM " + TABLE_USERS + " u" +
                " JOIN " + TABLE_TICKET + " t ON u." + COLUMN_ID + " = t." + COLUMN_TICKET_USER_ID +
                " JOIN " + TABLE_EVENTS + " e ON t." + COLUMN_TICKET_EVENT_ID + " = e." + COLUMN_EVENT_ID +
                " WHERE e." + COLUMN_ORGANIZER_ID + " = ? AND e." + COLUMN_EVENT_STATUS + " = ? ";

        Cursor cursor = db.rawQuery(query, new String[]{organizerId, eventStatus});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User attendee = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                );
                attendees.add(attendee);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return attendees;
    }

    // Method to add OTP to the database
    public long  addOtp(String email, String otp) {
        // Get writable database instance
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a ContentValues object to hold the data
        ContentValues values = new ContentValues();
        values.put("email_reset", email);
        values.put("otp", otp);

        // Use INSERT OR REPLACE to insert or update the OTP record
        long rowId = db.insertWithOnConflict("otp_password_reset", null, values, SQLiteDatabase.CONFLICT_REPLACE);

        // Check if the insert was successful
        if (rowId == -1) {
            // If the insertion failed, log or handle the failure
            Log.e("DB_ERROR", "Error inserting OTP for email: " + email);
        } else {
            // Successfully inserted or replaced
            Log.d("DB_SUCCESS", "OTP for email " + email + " was inserted/replaced successfully.");
        }

        // Close the database connection
        db.close();

        return rowId;
    }

    // Method to verify OTP
    public boolean verifyOtp(String otp) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OTP, null, COLUMN_OTP + " = ?", new String[]{otp}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Method to update password
    public void updatePassword(String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        db.update(TABLE_USERS, values, null, null);
        db.close();
    }

    // Method to check if email exists in the database
    public boolean isEmailExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM user_profile WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;  // If the cursor has data, the email exists
        cursor.close();

        Log.d("DBHelper", "Checking if email exists: " + email + " - Exists: " + exists);
        return exists;
    }




}
