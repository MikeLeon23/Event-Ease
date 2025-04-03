package com.example.eventease;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendeeListEventAdapter extends RecyclerView.Adapter<AttendeeListEventAdapter.ViewHolder> {

    private List<Event> listEvents;
    private Context context;

    public AttendeeListEventAdapter(List<Event> listEvents, Context context) {
        this.listEvents = listEvents;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_event_list_attendee_view, parent, false);
        return new ViewHolder(view);
    }

    public void updateEvents(List<Event> newEvents) {
        listEvents.clear();  // Clear the old data
        listEvents.addAll(newEvents);  // Add new data
        notifyDataSetChanged();  // Refresh the RecyclerView
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeListEventAdapter.ViewHolder holder, int position) {
        Event event = listEvents.get(position);
        // Log the event being bound
        Log.d("RecyclerView", "Binding event: " + event.getEventName() + " at position " + position);
        // Bind data to the views
        holder.titleTextView.setText(event.getEventName());
        // Set a click listener for the TextView (event name)
        holder.titleTextView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetail.class);
            intent.putExtra("event_id", event.getEventId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvEventNameAtt);
        }
    }

}
