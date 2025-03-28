package com.example.eventease;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ShareEventActivity extends AppCompatActivity {

    EditText edEmail;
    Button btnSendInvitation;
    DBHelper dbHelper;
    String eventId; // Event ID to be shared

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_event);

        edEmail = findViewById(R.id.edEmail);
        btnSendInvitation = findViewById(R.id.btnSendInvitation);
        dbHelper = new DBHelper(this);
       // eventId = getIntent().getStringExtra("EVENT_ID");
        long eventId = getIntent().getLongExtra("eventId", -1);  // Use long type to match what was passed
        btnSendInvitation.setOnClickListener(v -> {
            String email = edEmail.getText().toString();
            if (email.isEmpty()) {
                Toast.makeText(ShareEventActivity.this, "Please enter an email address", Toast.LENGTH_SHORT).show();
                return;
            }
            sendEmailInvite(email);
        });
    }

    private void sendEmailInvite(String email) {
        // You can use an email client (using an implicit intent) or send an email programmatically
        String subject = "You are invited to an event!";
        String body = "We thought you might be interested in an event. Please check out the details in our app.";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(emailIntent, "Send email"));

        // Optionally, save the invitation in the database
        dbHelper.saveEventInvitation(eventId, email);
        Toast.makeText(this, "Invitation sent", Toast.LENGTH_SHORT).show();
    }
}