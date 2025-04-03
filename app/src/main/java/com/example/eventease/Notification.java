package com.example.eventease;

public class Notification {

    private int notificationId;
    private int eventId;
    private int userId;
    private String message;
    private String status; // "pending", "accepted", "declined"

    // Constructor
    public Notification(int notificationId, int eventId, int userId, String message, String status) {
        this.notificationId = notificationId;
        this.eventId = eventId;
        this.userId = userId;
        this.message = message;
        this.status = status;
    }

    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
