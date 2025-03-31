package com.example.bikerentalcu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class item_view extends AppCompatActivity {

    private int quantity = 1; // Default quantity
    private TextView quantityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_view);

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            bikeModel bike = intent.getParcelableExtra("bike");

            if (bike != null) {
                // Initialize views
                ImageView bikeImage = findViewById(R.id.bike_image);
                ImageView owner_image = findViewById(R.id.owner_image);
                TextView bikeName = findViewById(R.id.bike_name);
                TextView bikePrice = findViewById(R.id.bike_price);
                TextView bikeTransmission = findViewById(R.id.bike_transmission);
                TextView bikeSpeed = findViewById(R.id.bike_speed);
                TextView bikeMileage = findViewById(R.id.bike_mileage);
                TextView ownerName = findViewById(R.id.owner_name);
                ImageView ownerEmail = findViewById(R.id.owner_email);
                ImageView ownerContact = findViewById(R.id.owner_contact);
                Button bookNow = findViewById(R.id.book_now);
                ImageView backButton = findViewById(R.id.back_button);

                ImageView decreaseQuantity = findViewById(R.id.minus1);
                ImageView increaseQuantity = findViewById(R.id.plus1);
                quantityText = findViewById(R.id.quantity);

                // Set values to views
                bikeName.setText(bike.getName());
                bikePrice.setText("₹ " + bike.getPrice());
                bikeTransmission.setText(bike.getTransmission());
                bikeSpeed.setText(bike.getSpeed());
                bikeMileage.setText(bike.getMileage());
                ownerName.setText(bike.getOwnerName());
//                Log.d("owner ka url -->",
//                        bike.getownerurl());
                // Load bike image using Glide
                Glide.with(this).load(bike.getImageUrl()).into(bikeImage);
                //load owner image
                Glide.with(this)
                        .load(bike.getownerurl())
                        .placeholder(R.drawable.exclamation_mark) // Use a valid placeholder image
                        .error(R.drawable.exclamation_mark) // Show an error image if loading fails
                        .into(owner_image);

                // Email click listener
                ownerEmail.setOnClickListener(v -> {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:" + bike.getOwnerEmail()));
                    startActivity(emailIntent);
                });



                // Contact click listener
                ownerContact.setOnClickListener(v -> {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + bike.getOwnerContact()));
                    startActivity(callIntent);
                });

                // Back button
                backButton.setOnClickListener(v -> finish());

                // Quantity selection listeners
                decreaseQuantity.setOnClickListener(v -> updateQuantity(false));
                increaseQuantity.setOnClickListener(v -> updateQuantity(true));
            }
        }
    }

    private void updateQuantity(boolean increase) {
        if (increase) {
            quantity++;
        } else {
            if (quantity > 1) {
                quantity--;
            }
        }
        quantityText.setText(String.valueOf(quantity));
    }
}
