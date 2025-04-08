package com.example.bikerentalcu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class item_view extends AppCompatActivity {

    private int quantity = 1; // Default quantity
    private TextView quantityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

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

        // Get data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            // Check if the data is of type bikeModel or CartItem
            if (intent.hasExtra("bike")) {
                // Retrieve bikeModel
                bikeModel bike = (bikeModel) intent.getSerializableExtra("bike");

                if (bike != null) {
                    // Set values to views for bikeModel
                    bikeName.setText(bike.getName());
                    bikePrice.setText("₹ " + bike.getPrice());
                    bikeTransmission.setText(bike.getTransmission());
                    bikeSpeed.setText(bike.getSpeed());
                    bikeMileage.setText(bike.getMileage());
                    ownerName.setText(bike.getOwnerName());

                    // Load bike image using Glide
                    Glide.with(this).load(bike.getImageUrl()).into(bikeImage);
                    Glide.with(this)
                            .load(bike.getownerurl())
                            .placeholder(R.drawable.exclamation_mark)
                            .error(R.drawable.exclamation_mark)
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
                }
            } else if (intent.hasExtra("cartItems")) {
                // Retrieve CartItem
                CartItem cartItem = intent.getParcelableExtra("cartItems");
                int quantity = intent.getIntExtra("quantity", 1);

                if (cartItem != null) {
                    // Set values to views for CartItem
                    bikeName.setText(cartItem.getName());
                    bikePrice.setText("₹ " + cartItem.getPrice());
                    bikeTransmission.setText(cartItem.getTransmission());
                    bikeSpeed.setText(cartItem.getSpeed());
                    bikeMileage.setText(cartItem.getMileage());
                    ownerName.setText(cartItem.getOwnerName());
                    quantityText.setText(String.valueOf(quantity));

                    // Load bike image using Glide
                    Glide.with(this).load(cartItem.getImageUrl()).into(bikeImage);
                    Glide.with(this)
                            .load(cartItem.getOwnerUrl())
                            .placeholder(R.drawable.exclamation_mark)
                            .error(R.drawable.exclamation_mark)
                            .into(owner_image);

                    // Email click listener
                    ownerEmail.setOnClickListener(v -> {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:" + cartItem.getOwnerEmail()));
                        startActivity(emailIntent);
                    });

                    // Contact click listener
                    ownerContact.setOnClickListener(v -> {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + cartItem.getOwnerContact()));
                        startActivity(callIntent);
                    });
                }
            }
        }

        // Back button
        backButton.setOnClickListener(v -> finish());
        bookNow.setOnClickListener(v -> {
            CartItem cartItemToAdd = null;

            if (getIntent().hasExtra("bike")) {
                bikeModel bike = (bikeModel) getIntent().getSerializableExtra("bike");
                if (bike != null && !CartManager.isBikeInCart(bike)) {
                    cartItemToAdd = new CartItem(
                            bike.getName(),
                            bike.getPrice() * quantity,             // price
                            bike.getTransmission(),                 // transmission
                            bike.getSpeed(),                        // speed
                            bike.getMileage(),                      // mileage
                            bike.getOwnerName(),                    // owner name
                            bike.getOwnerEmail(),                   // owner email
                            bike.getOwnerContact(),                 // owner contact
                            bike.getImageUrl(),                     // image URL
                            bike.getownerurl()                      // owner URL
                    );

                } else {
                    // Optional: show message if already in cart
                    Toast.makeText(this, "Bike already in cart!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (getIntent().hasExtra("cartItems")) {
                cartItemToAdd = getIntent().getParcelableExtra("cartItems");
                if (cartItemToAdd != null) {
                    cartItemToAdd.setPrice(cartItemToAdd.getPrice() * quantity); // update price based on quantity
                }
            }

            if (cartItemToAdd != null) {
                CartManager.addToCart(cartItemToAdd);
                Toast.makeText(this, "Bike added to cart", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, cart.class));
            }
        });

        // Quantity selection listeners
        decreaseQuantity.setOnClickListener(v -> updateQuantity(false));
        increaseQuantity.setOnClickListener(v -> updateQuantity(true));
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
