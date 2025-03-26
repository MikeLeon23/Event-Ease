package com.example.eventease;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendeeListEventAdapter extends RecyclerView.Adapter<AttendeeListEventAdapter.ViewHolder> {

    private List<ListEvents> listEvents;
    private Context context;

    public AttendeeListEventAdapter(List<ListEvents> listEvents, Context context) {
        this.context = context;
        this.listEvents = listEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListEvents item = listEvents.get(position);
        holder.textView.setText(item.getTitle());

        // Set OnClickListener to navigate to DetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AttendeeEventDetail.class);
            intent.putExtra("itemTitle", item.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

}
