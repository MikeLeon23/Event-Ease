package com.example.eventease;

import android.content.Context;
import android.content.Intent;
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

public class OrganizerListAttendeeAdapter extends RecyclerView.Adapter<OrganizerListAttendeeAdapter.ViewHolder> {

    private List<User> attendeeList;

    private Context context;
    private Set<String> selectedAttendeeIds = new HashSet<>();

    public OrganizerListAttendeeAdapter(List<User> attendeeList, Context context) {
        this.attendeeList = attendeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrganizerListAttendeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_attendee_list, parent, false);
        return new OrganizerListAttendeeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizerListAttendeeAdapter.ViewHolder holder, int position) {
        User attendee = attendeeList.get(position);
        holder.tvAttendeeName.setText(attendee.getName());

        // Handle CheckBox selection
//        holder.checkBox.setOnCheckedChangeListener(null); // Prevent unwanted calls when recycling views
//        holder.checkBox.setChecked(selectedAttendeeIds.contains(attendee.getId())); // Restore state
//
//        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                selectedAttendeeIds.add(attendee.getId()); // Add event ID when checked
//            } else {
//                selectedAttendeeIds.remove(attendee.getId()); // Remove when unchecked
//            }
//        });

        // Set a click listener for the TextView (event name)
        holder.tvAttendeeName.setOnClickListener(v -> {
            Intent intent = new Intent(context, AttendeeProfileUpdate.class);
            intent.putExtra("COLUMN_ID", attendee.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return attendeeList.size();
    }

//    public void removeDeletedAttendees() {
//        attendeeList.removeIf(attendee -> selectedAttendeeIds.contains(attendee.getId())); // Remove selected events from list
//        selectedAttendeeIds.clear();
//        notifyDataSetChanged(); // Refresh RecyclerView
//    }
    public Set<String> getSelectedAttendeeIds() {
        return selectedAttendeeIds;
    }

    public void updateAttendees(List<User> newAttendees) {
        attendeeList = newAttendees;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttendeeName;
//        CheckBox checkBox;
        ViewHolder(View itemView) {
            super(itemView);
            tvAttendeeName = itemView.findViewById(R.id.tvAttendeeName);
//            checkBox = itemView.findViewById(R.id.cbSelectEvent);
        }
    }
}
