package com.example.eventease;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class SendGridHelper {

    private static final String TAG = "SendGridHelper"; // Logcat tag for debugging

    public static void sendOtpEmail(String recipientEmail, String otp) {
        new SendOtpEmailTask().execute(recipientEmail, otp); // Execute AsyncTask
    }

    private static class SendOtpEmailTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                String recipientEmail = params[0];
                String otp = params[1];

                Log.d(TAG, "Preparing to send OTP email to: " + recipientEmail);

                // Disable SSL verification (for debugging only, NOT safe for production!)
                SSLContext sslContext = SSLContext.getInstance("TLS");
                TrustManager[] trustAllCertificates = new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() { return null; }
                            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                        }
                };
                sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

                // Prepare API request
                URL url = new URL("https://api.sendgrid.com/v3/mail/send");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Set headers
                connection.setRequestProperty("Authorization", "Bearer " + Config.SENDGRID_API_KEY);
                connection.setRequestProperty("Content-Type", "application/json");

                // Prepare JSON payload
                String jsonPayload = "{"
                        + "\"personalizations\": [{\"to\": [{\"email\": \"" + recipientEmail + "\"}]}],"
                        + "\"from\": {\"email\": \"" + Config.SENDER_EMAIL + "\"},"
                        + "\"subject\": \"Your OTP Code\","
                        + "\"content\": [{\"type\": \"text/plain\", \"value\": \"Your OTP code is: " + otp + "\"}]"
                        + "}";

                Log.d(TAG, "Request JSON:\n" + jsonPayload);

                // Send data
                OutputStream os = connection.getOutputStream();
                os.write(jsonPayload.getBytes("utf-8"));
                os.close();

                // Get response
                int responseCode = connection.getResponseCode();
                Log.d(TAG, "Response Code: " + responseCode);

                // Read response
                BufferedReader br;
                if (responseCode >= 200 && responseCode < 300) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    Log.d(TAG, "OTP email sent successfully!");
                } else {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    Log.e(TAG, "Failed to send OTP email.");
                }

                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine);
                }
                br.close();

                Log.d(TAG, "SendGrid Response: " + response.toString());

                return response.toString();
            } catch (Exception ex) {
                Log.e(TAG, "Exception occurred while sending OTP email: ", ex);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // This method is called after the background task finishes
            if (result != null) {
                Log.d(TAG, "Email response: " + result);
            } else {
                Log.e(TAG, "Failed to send OTP email. No response from server.");
            }
        }
    }
}