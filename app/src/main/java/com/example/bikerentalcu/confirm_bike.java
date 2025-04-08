package com.example.bikerentalcu;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class confirm_bike extends AppCompatActivity {

    private TextView bikeName, bikePrice, quantityText, totalPriceText, totalAmountText, locationText;
    private ImageView bikeImage;
    private EditText editDays;
    private Button confirmBtn;
    private TextView startDateText, endDateText;

    private CartItem cartItem;
    private int quantity = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_bike);

        initializeViews();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        Intent intent = getIntent();
        cartItem = intent.getParcelableExtra("cartItems");
        quantity = intent.getIntExtra("quantity", 1);

        if (cartItem != null) {
            Log.d("ConfirmBike", "Received cart item: " + cartItem.getName());
        }

        populateData(cartItem, quantity);

        startDateText.setOnClickListener(v -> showDatePickerDialog(startDateText));
        endDateText.setOnClickListener(v -> showDatePickerDialog(endDateText));

        confirmBtn.setOnClickListener(v -> {
            String location = locationText.getText().toString();
            String startDate = startDateText.getText().toString();
            String endDate = endDateText.getText().toString();
            String amount = totalAmountText.getText().toString();
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            String userName = prefs.getString("userName", "Guest");
            // Replace with actual user name logic
            String ownerEmail = cartItem.getOwnerEmail(); // Assuming this exists in CartItem

            Intent inten = new Intent(confirm_bike.this, checkout.class);
            intent.putExtra("location", location);
            intent.putExtra("startDate", startDate);
            intent.putExtra("endDate", endDate);
            intent.putExtra("amount", amount);
            intent.putExtra("userName", userName);
            intent.putExtra("ownerEmail", ownerEmail);
            intent.putExtra("bikeName", cartItem.getName()); // Optional: if needed in checkout
            startActivity(inten);
        });

    }

    private void initializeViews() {
        bikeName = findViewById(R.id.bikeName);
        bikePrice = findViewById(R.id.bikePrice);
        quantityText = findViewById(R.id.selectedCount);
        bikeImage = findViewById(R.id.bikeImage);
        totalPriceText = findViewById(R.id.totalPrice);
        totalAmountText = findViewById(R.id.totalAmount);
        editDays = findViewById(R.id.editDays);
        confirmBtn = findViewById(R.id.confirmBtn);
        locationText = findViewById(R.id.locationText);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
    }

    private void populateData(CartItem item, int quantity) {
        if (item != null) {
            bikeName.setText(item.getName());
            bikePrice.setText("₹" + item.getPrice());
            quantityText.setText("Selected: " + quantity);
            Glide.with(this).load(item.getImageUrl()).into(bikeImage);
        }
    }

    private void calculateAndDisplayPrice(CartItem item, int quantity, int days) {
        int price = item.getPrice() * quantity * days;
        int driverFee = 50;
        int totalAmount = price + driverFee;

        totalPriceText.setText("Price: ₹" + price);
        totalAmountText.setText("TOTAL    ₹" + totalAmount);
    }

    private void showDatePickerDialog(TextView targetTextView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year1, month1, dayOfMonth);
                    String selectedDate = dateFormat.format(selectedCal.getTime());
                    targetTextView.setText(selectedDate);

                    String startDateStr = startDateText.getText().toString();
                    String endDateStr = endDateText.getText().toString();

                    if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                        try {
                            Date start = dateFormat.parse(startDateStr);
                            Date end = dateFormat.parse(endDateStr);
                            long diff = end.getTime() - start.getTime();
                            int days = (int) (diff / (1000 * 60 * 60 * 24)) + 1;
                            if (days > 0) {
                                editDays.setText(String.valueOf(days));
                                calculateAndDisplayPrice(cartItem, quantity, days);
                            } else {
                                editDays.setText("");
                                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        setAddressFromLocation(location);
                    } else {
                        locationText.setText("Unable to fetch location");
                    }
                });
    }

    private void setAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
                locationText.setText(addressLine);
            } else {
                locationText.setText("Address not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationText.setText("Geocoder error");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            } else {
                locationText.setText("Location permission denied");
            }
        }
    }
}
