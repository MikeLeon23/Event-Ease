package com.example.eventease;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnSendOtp, btnBack;

    private static final String TAG = "SendGridHelper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);
        DBHelper dbHelper = new DBHelper(ForgotPasswordActivity.this);
        edtEmail = findViewById(R.id.edEmailReset);

        btnBack = findViewById(R.id.btnBackk);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, MainActivity.class));
            }
        });

        btnSendOtp = findViewById(R.id.btnSendEmail);
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    edtEmail.setError("Please enter your email.");
                    return;
                }

                // Check if the email exists in the database
                if (!dbHelper.isEmailExist(email)) {
                    edtEmail.setError("Email is incorrect, try again.");
                    return;
                }

                    // Generate OTP
                    String otp = generateOtp();

                    // Save OTP to database

                    dbHelper.addOtp(email, otp);  // Add method to insert OTP into the DB

                    // Send OTP email using SendGrid
                    SendGridHelper.sendOtpEmail(email, otp);

                    // Move to OTP verification screen
                    Toast.makeText(ForgotPasswordActivity.this, "OTP sent to your email.", Toast.LENGTH_SHORT).show();
                    // Intent to navigate to OTP verification screen

                    startActivity(new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class));
                }

        });
    }

    // Simple method to generate OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate a 6-digit OTP
        return String.valueOf(otp);
    }
}