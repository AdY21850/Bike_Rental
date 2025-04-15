package com.example.bikerentalcu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set content view for this activity
        setContentView(R.layout.activity_settings);

        // Initialize FrameLayouts and ImageView properly after setContentView
        FrameLayout about = findViewById(R.id.aboutbtn);
        FrameLayout profilebtn = findViewById(R.id.profilebtn1);
        ImageView back2 = findViewById(R.id.back2);

        // Set onClickListener for About Button
        if (about != null) {
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(settings.this, about.class));
                }
            });
        } else {
            // Log or handle the case where the view is not found
            Log.e("SettingsActivity", "About button not found");
        }

        // Set onClickListener for Profile Button
        if (profilebtn != null) {
            profilebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(settings.this, updateProfile.class));
                }
            });
        } else {
            // Log or handle the case where the view is not found
            Log.e("SettingsActivity", "Profile button not found");
        }

        // Set onClickListener for Back Button
        if (back2 != null) {
            back2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(settings.this, navdraw.class));
                }
            });
        } else {
            // Log or handle the case where the view is not found
            Log.e("SettingsActivity", "Back button not found");
        }
    }
}
