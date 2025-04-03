package com.example.eventease;

public class Event {

    private String eventId;
    private String eventName;
    private String eventLocation;
    private String eventDate;
    private String eventTime;
    private double eventFee;
    private String eventDescription;
    private String eventReminder;
    private int eventSeat;
    private String eventImagePath;

    private String eventStatus;
    private boolean isInterested;

    public Event(String eventId, String eventName, String eventLocation, String eventDate, String eventTime, String eventStatus) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventStatus = eventStatus;
    }

    private String organizerId;

    private boolean isChecked;

    public Event(String eventId, String eventName, String eventLocation, String eventDate, String eventTime, double eventFee, String eventDescription,
                 String eventReminder, int eventSeat, String eventImagePath, String eventStatus, String organizerId, boolean isChecked) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.eventFee = eventFee;
        this.eventDescription = eventDescription;
        this.eventReminder = eventReminder;
        this.eventSeat = eventSeat;
        this.eventImagePath = eventImagePath;
        this.eventStatus = eventStatus;
        this.organizerId = organizerId;
        this.isChecked = isChecked;
        this.isInterested = false;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public double getEventFee() {
        return eventFee;
    }

    public void setEventFee(double eventFee) {
        this.eventFee = eventFee;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventReminder() {
        return eventReminder;
    }

    public void setEventReminder(String eventReminder) {
        this.eventReminder = eventReminder;
    }

    public int getEventSeat() {
        return eventSeat;
    }

    public void setEventSeat(int eventSeat) {
        this.eventSeat = eventSeat;
    }

    public String getEventImagePath() {
        return eventImagePath;
    }

    public void setEventImagePath(String eventImagePath) {
        this.eventImagePath = eventImagePath;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }
    public boolean isInterested() {
        return isInterested;
    }
    public void setInterested(boolean isInterested) {
        this.isInterested = isInterested;
    }
    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
