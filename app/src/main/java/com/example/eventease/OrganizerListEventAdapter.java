package com.example.eventease;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrganizerListEventAdapter extends RecyclerView.Adapter<OrganizerListEventAdapter.ViewHolder>{

    private List<Event> listEvents;
    private Context context;
    private Set<String> selectedEventIds = new HashSet<>(); // Store selected event IDs

    public OrganizerListEventAdapter(List<Event> listEvents, Context context) {
        this.listEvents = listEvents;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_event_list, parent, false);
        return new ViewHolder(view);
    }

    public void updateEvents(List<Event> newEvents) {
        listEvents.clear();  // Clear the old data
        listEvents.addAll(newEvents);  // Add new data
        notifyDataSetChanged();  // Refresh the RecyclerView
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizerListEventAdapter.ViewHolder holder, int position) {
        Event event = listEvents.get(position);
        // Log the event being bound
        Log.d("RecyclerView", "Binding event: " + event.getEventName() + " at position " + position);
        // Bind data to the views
        holder.titleTextView.setText(event.getEventName());

        // Handle CheckBox selection
        holder.checkBox.setOnCheckedChangeListener(null); // Prevent unwanted calls when recycling views
        holder.checkBox.setChecked(selectedEventIds.contains(event.getEventId())); // Restore state

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedEventIds.add(event.getEventId()); // Add event ID when checked
            } else {
                selectedEventIds.remove(event.getEventId()); // Remove when unchecked
            }
        });



        // Set a click listener for the TextView (event name)
        holder.titleTextView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrganizerEventUpdate.class);
            intent.putExtra("event_id", event.getEventId());
            intent.putExtra("organizer_id", event.getOrganizerId());
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public Set<String> getSelectedEventIds() {
        return selectedEventIds;
    }

    public void removeDeletedEvents() {
        listEvents.removeIf(event -> selectedEventIds.contains(event.getEventId())); // Remove selected events from list
        selectedEventIds.clear();
        notifyDataSetChanged(); // Refresh RecyclerView
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tvEventName);
            checkBox = itemView.findViewById(R.id.cbSelectEvent);
        }
    }

}
