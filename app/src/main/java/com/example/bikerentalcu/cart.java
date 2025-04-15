package com.example.bikerentalcu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class cart extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartList;
    private ProgressBar progressBar;
    private Button nextButton;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);

        // Setup RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartList = CartManager.getCart();  // Fetch bikes added to the cart
        cartAdapter = new CartAdapter(this, cartList);
        cartRecyclerView.setAdapter(cartAdapter);

        // Show ProgressBar while loading data
        progressBar.setVisibility(View.VISIBLE);
        loadCartData();

        // Back Button Click Listener
        backButton.setOnClickListener(v -> finish());

        // Next Button Click Listener (Navigate to Checkout)
//        nextButton.setOnClickListener(v -> {
//            Intent intent = new Intent(cart.this, confirm_bike.class);
//            startActivity(intent);
//        });
    }

    // Load Cart Data (Simulating delay for fetching data)
    private void loadCartData() {
        new android.os.Handler().postDelayed(() -> {
            cartAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }, 1000); // Simulating delay of 1.5 seconds to load data
    }
}
