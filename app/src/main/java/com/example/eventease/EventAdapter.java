package com.example.eventease;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<Event> eventList;
    private OnEventActionListener actionListener;

    // Interface for handling edit and delete actions
    public interface OnEventActionListener {
        void onEditClick(Event event, int position);
        void onDeleteClick(Event event, int position);
        void onStarClick(Event event, int position);
    }

    public EventAdapter(List<Event> eventList, OnEventActionListener actionListener) {
        this.eventList = eventList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.titleTextView.setText(event.getEventName());
        holder.detailsTextView.setText(event.getEventDate() + ", " + event.getEventTime() + ", " + event.getEventLocation());

        // Set click listeners for edit and delete icons
        holder.editIcon.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEditClick(event, position);
            }
        });

        holder.deleteIcon.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteClick(event, position);
            }
        });

        holder.starIcon.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onStarClick(event, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView detailsTextView;
        ImageView starIcon;
        ImageView editIcon;
        ImageView deleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.event_image);
            titleTextView = itemView.findViewById(R.id.event_title);
            detailsTextView = itemView.findViewById(R.id.event_details);
            starIcon = itemView.findViewById(R.id.star_icon);
            editIcon = itemView.findViewById(R.id.edit_icon);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }
}