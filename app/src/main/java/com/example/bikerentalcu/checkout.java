package com.example.bikerentalcu;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class checkout extends AppCompatActivity implements PaymentResultListener {

    EditText simNameEditText, upiIdEditText;
    Button payButton;

    String location, startDate, endDate, amount, userName, ownerEmail, bikeName, ownerPhone, ownerupi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        Checkout.preload(getApplicationContext()); // Razorpay preload

        simNameEditText = findViewById(R.id.simName); // for owner mobile number
        upiIdEditText = findViewById(R.id.phoneNumber); // for user's upi id
        payButton = findViewById(R.id.payButton);

        // Receiving data from previous activity
        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        amount = intent.getStringExtra("amount");
        Log.d("amaount reicived-->",amount);
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userName = prefs.getString("userName", "Guest");
        ownerEmail = intent.getStringExtra("ownerEmail");
        bikeName = intent.getStringExtra("bikeName");
        ownerupi = intent.getStringExtra("ownerupi");
        ownerPhone = intent.getStringExtra("ownerPhone");

        Log.d("Checkout", "Amount: " + amount + ", Owner UPI: " + ownerupi);

        simNameEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String mobile = simNameEditText.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    simNameEditText.setError("Enter UPI-linked mobile number");
                } else {
                    String upiId = mobile + "@upi";
                    upiIdEditText.setText(upiId);
                    upiIdEditText.requestFocus();
                    hideKeyboard();
                }
                return true;
            }
            return false;
        });

        upiIdEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String upiId = upiIdEditText.getText().toString().trim();
                if (TextUtils.isEmpty(upiId)) {
                    upiIdEditText.setError("Enter valid UPI ID");
                } else {
                    Toast.makeText(this, "UPI ID looks good!", Toast.LENGTH_SHORT).show();
                    hideKeyboard();
                }
                return true;
            }
            return false;
        });

        payButton.setOnClickListener(v -> {
            String userUpiId = upiIdEditText.getText().toString().trim();

            if (TextUtils.isEmpty(userUpiId)) {
                upiIdEditText.setError("Enter valid UPI ID");
                return;
            }

            startRazorpayPayment();
        });

        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> finish());
    }

    private void startRazorpayPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_BeskA8VvjnD0KplR26dqh5PP"); // Replace with your actual Razorpay key

        if (TextUtils.isEmpty(amount)) {
            Toast.makeText(this, "Amount is missing. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float amountFloat = Float.parseFloat(amount.trim());
            int amountInPaise = (int) (amountFloat * 100);

            JSONObject options = new JSONObject();
            options.put("name", "Bike Rental");
            options.put("description", "Rental for " + bikeName);
            options.put("currency", "INR");
            options.put("amount", amountInPaise);

            JSONObject prefill = new JSONObject();
            prefill.put("email", ownerEmail);
            prefill.put("contact", ownerPhone);

            options.put("prefill", prefill);

            checkout.open(this, options);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format: " + amount, Toast.LENGTH_LONG).show();
            Log.e("Razorpay Error", "Invalid amount format", e);
        } catch (Exception e) {
            Log.e("Razorpay Error", "Error in payment: ", e);
            Toast.makeText(this, "Payment setup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        sendBookingEmail();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_SHORT).show();
    }

    private void sendBookingEmail() {
        String subject = "New Booking Confirmation - " + bikeName;
        String message = "Hello,\n\nYou have received a new booking:\n\n"
                + "User: " + userName + "\n"
                + "Bike: " + bikeName + "\n"
                + "Location: " + location + "\n"
                + "Start Date: " + startDate + "\n"
                + "End Date: " + endDate + "\n"
                + "Total Amount: ₹" + amount + "\n\n"
                + "Regards,\nBike Rental App";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + ownerEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email via..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
