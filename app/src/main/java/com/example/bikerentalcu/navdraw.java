package com.example.bikerentalcu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class navdraw extends AppCompatActivity {

    private AlertDialog authDialog; // Reference for the auth dialog
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navdraw);

        // Transparent status bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // Get SharedPreferences
        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // ----------- Load Profile Image from SharedPreferences -------------
        String imageUrl = preferences.getString("userimage", null);
        ImageView profileImageView = findViewById(R.id.circle_center_image);
        Log.d("navdraw", "Image URL received from preferences: " + imageUrl);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.endsWith(".svg")) {
                SvgSoftwareLayerSetter.loadSvgImage(this, imageUrl, profileImageView);
            } else {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.exclamation_mark)
                        .error(R.drawable.exclamation_mark)
                        .into(profileImageView);
            }
        } else {
            profileImageView.setImageResource(R.drawable.circle_background); // fallback
        }

        // ----------- Load Username from Intent or Fallback to SharedPreferences -------------
        String userName = getIntent().getStringExtra("userName");
        if (userName == null || userName.isEmpty()) {
            userName = preferences.getString("userName", "Guest User");
        }

        Log.d("navdraw", "Username displayed: " + userName);
        TextView userNameText = findViewById(R.id.textUsername);
        userNameText.setText(userName);

        // -------------------- Setup Buttons --------------------
        FrameLayout profilebtn1 = findViewById(R.id.profilebtn1);
        FrameLayout privacybtn = findViewById(R.id.privacybtn);
        FrameLayout aboutbtn = findViewById(R.id.aboutbtn);
        FrameLayout helpbtn = findViewById(R.id.helpbtn);
        FrameLayout logoutbtn = findViewById(R.id.logoutbtn);

        // Privacy Button
        privacybtn.setOnClickListener(v -> {
            String url = "https://policies.google.com/privacy?hl=en-US";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        // Help Button
        helpbtn.setOnClickListener(v -> {
            String url = "https://dictionary.cambridge.org/dictionary/english/help";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        // Profile Settings Button
        profilebtn1.setOnClickListener(v -> {
            startActivity(new Intent(navdraw.this, updateProfile.class));
        });

        // About Section Button
        aboutbtn.setOnClickListener(v -> {
            startActivity(new Intent(navdraw.this, about.class));
        });

        // Uncomment below if you want logout functionality
        /*
        logoutbtn.setOnClickListener(v -> confirmLogout());
        */

        // Optional: Block access if not logged in
        /*
        if (!isUserLoggedIn()) {
            redirectToLogin();
            return;
        }
        */
    }

    // Uncomment if you're using logout
    /*
    private boolean isUserLoggedIn() {
        return preferences.getBoolean("isLoggedIn", false);
    }

    private void redirectToLogin() {
        startActivity(new Intent(navdraw.this, screen2.class));
        finish();
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("No", null)
                .show();
    }

    private void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        if (authDialog != null && authDialog.isShowing()) {
            authDialog.dismiss();
        }

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        redirectToLogin();
    }

    @Override
    public void onBackPressed() {
        if (!isUserLoggedIn()) {
            redirectToLogin();
        } else {
            super.onBackPressed();
        }
    }
    */
}
