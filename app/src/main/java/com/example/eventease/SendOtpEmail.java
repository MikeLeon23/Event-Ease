package com.example.eventease;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendOtpEmail extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = "SendOtpEmail";
    private Context context;
    private String recipientEmail;
    private String otpCode;

    public SendOtpEmail(Context context, String recipientEmail, String otpCode) {
        this.context = context;
        this.recipientEmail = recipientEmail;
        this.otpCode = otpCode;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        final String senderEmail = "your_email@gmail.com"; // Your email
        final String senderPassword = "your_password"; // Your email password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP Code is: " + otpCode);

            Transport.send(message);
            Log.d(TAG, "Email sent successfully");
        } catch (MessagingException e) {
            Log.e(TAG, "Error sending email", e);
            Toast.makeText(context, "Failed to send OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
