package com.example.eventease;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OtpVerificationActivity extends AppCompatActivity {
    private EditText edtOtp, edtNewPassword;
    private Button btnVerifyOtp, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        edtOtp = findViewById(R.id.edOtpVerify);
        edtNewPassword = findViewById(R.id.edNewPassword);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OtpVerificationActivity.this, ForgotPasswordActivity.class));
            }
        });

        btnVerifyOtp = findViewById(R.id.btnPasswordReset);
        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edtOtp.getText().toString().trim();
                String newPassword = edtNewPassword.getText().toString().trim();

                if (otp.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(OtpVerificationActivity.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check OTP and update password in DB
                DBHelper dbHelper = new DBHelper(OtpVerificationActivity.this);
                boolean isOtpValid = dbHelper.verifyOtp(otp);  // Add method to verify OTP

                if (isOtpValid) {
                    dbHelper.updatePassword(newPassword);  // Add method to update password in DB
                    Toast.makeText(OtpVerificationActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OtpVerificationActivity.this, MainActivity.class));
                    // Redirect to login page
                } else {
                    Toast.makeText(OtpVerificationActivity.this, "Invalid OTP.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
