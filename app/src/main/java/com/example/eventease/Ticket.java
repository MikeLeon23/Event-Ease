package com.example.eventease;

public class Ticket {
    private String ticketId;
    private User user;
    private Event event;

    public Ticket(String ticketId, User user, Event event) {
        this.ticketId = ticketId;
        this.user = user;
        this.event = event;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
