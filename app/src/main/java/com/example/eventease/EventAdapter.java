package com.example.eventease;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<Event> eventList;
    private Context context;
    private OnEventActionListener actionListener;

    // Interface for handling edit and delete actions
    public interface OnEventActionListener {
        void onEditClick(Event event, int position);
        void onDeleteClick(Event event, int position);
        void onStarClick(Event event, int position);
    }

    public EventAdapter(Context context, List<Event> eventList, OnEventActionListener actionListener) {
        this.eventList = eventList != null ? eventList : new ArrayList<>();
        this.actionListener = actionListener;
        this.context = context;
    }

    public void updateEvents(List<Event> newEvents) {
        this.eventList = newEvents != null ? newEvents : new ArrayList<>();
        notifyDataSetChanged();
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

        // Load the event image using Glide
        String imagePath = event.getEventImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(context)
                    .load(imagePath) // Load from file path
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder) // Placeholder while loading
                            .error(R.drawable.placeholder) // Image to show if loading fails
                            .diskCacheStrategy(DiskCacheStrategy.ALL)) // Cache the image
                    .into(holder.imageView);
        } else {
            // If imagePath is null or empty, show a placeholder
            holder.imageView.setImageResource(R.drawable.placeholder);
        }

        // Set star icon based on isInterested
        holder.starIcon.setImageResource(event.isInterested() ?
                R.drawable.ic_star_fill : R.drawable.ic_star);

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

        // Set click listener for the entire card
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetail.class);
            intent.putExtra("event_id", event.getEventId()); // Pass the event ID
            context.startActivity(intent);
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