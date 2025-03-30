package com.example.eventease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private Context context;
    private List<Ticket> tickets;

    public TicketAdapter(Context context, List<Ticket> tickets) {
        this.context = context;
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        Event event = ticket.getEvent();
        holder.eventName.setText(event.getEventName());
        holder.eventDateTime.setText(String.format("%s, %s", event.getEventDate(), event.getEventTime()));
        holder.eventDetails.setText(event.getEventLocation());
        // Image loading can be added here (e.g., Glide) if eventImagePath is used
        // Glide.with(context).load(event.getEventImagePath()).placeholder(R.drawable.placeholder_event_image).into(holder.eventImage);
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public void updateTickets(List<Ticket> newTickets) {
        this.tickets = newTickets;
        notifyDataSetChanged();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventName;
        TextView eventDateTime;
        TextView eventDetails;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            eventDateTime = itemView.findViewById(R.id.event_date_time);
            eventDetails = itemView.findViewById(R.id.event_details);
        }
    }
}