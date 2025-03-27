package com.example.eventease;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrganizerListEventAdapter extends RecyclerView.Adapter<OrganizerListEventAdapter.ViewHolder>{
    private List<Event> listEvents;
    private Context context;

    public OrganizerListEventAdapter(List<Event> listEvents, Context context) {
        this.listEvents = listEvents;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_event_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = listEvents.get(position);
        holder.titleTextView.setText(event.getEventName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrganizerEventDetail.class);
            intent.putExtra("eventId", event.getEventId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
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
