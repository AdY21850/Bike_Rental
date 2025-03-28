package com.example.bikerentalcu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class navdraw extends AppCompatActivity {

    private AlertDialog authDialog; // Reference for the auth dialog
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navdraw);

        getWindow().setStatusBarColor(Color.parseColor("#000000"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Check if the user is logged in, redirect if not
//        if (!isUserLoggedIn()) {
//            redirectToLogin();
//            return;
//        }

        //----------------------setting onclick Listeners -----------------------------------------
        FrameLayout profilebtn = findViewById(R.id.profilebtn);
        FrameLayout settingsbtn = findViewById(R.id.Settingsbtn);
        FrameLayout notificationbtn = findViewById(R.id.Notificationbtn);
        FrameLayout privacybtn = findViewById(R.id.privacybtn);
        FrameLayout helpbtn = findViewById(R.id.helpbtn);
        FrameLayout logoutbtn = findViewById(R.id.logoutbtn); // Logout button

        //------------------------------profilebtn------------------------------------------------
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(navdraw.this, profile.class));
            }
        });

        //--------------------------------------settingsbtn----------------------------------
        settingsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(navdraw.this, settings.class));
            }
        });

        //----------------------------------notificationbtn--------------------------------
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(navdraw.this, notifications.class));
            }
        });

        //-----------------------------------privacybtn--------------------------------
        privacybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://policies.google.com/privacy?hl=en-US";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        //------------------------------------helpbtn--------------------------------
        helpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://dictionary.cambridge.org/dictionary/english/help";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        //------------------------------------logoutbtn--------------------------------
//        logoutbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                confirmLogout(); // Call method to confirm logout
//            }
//        });
//    }
//
//    private boolean isUserLoggedIn() {
//        return preferences.getBoolean("isLoggedIn", false); // Replace with your actual logic
//    }
//
//    private void redirectToLogin() {
//        startActivity(new Intent(navdraw.this, screen2.class)); // Replace with your login activity
//        finish(); // Close this activity
//    }
//
//    private void confirmLogout() {
//        new AlertDialog.Builder(this)
//                .setTitle("Logout")
//                .setMessage("Are you sure you want to logout?")
//                .setPositiveButton("Yes", (dialog, which) -> logout())
//                .setNegativeButton("No", null)
//                .show();
//    }
//
//    private void logout() {
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear(); // Clear all saved data
//        editor.apply(); // Apply changes
//
//        // Close the login/signup dialog (if open)
//        if (authDialog != null && authDialog.isShowing()) {
//            authDialog.dismiss(); // Assuming authDialog is your dialog reference
//        }
//
//        // Show a message to indicate successful logout
//        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
//
//        // Redirect to the login screen
//        redirectToLogin();
//    }
//
//    @Override
//    public void onBackPressed() {
//        // Disable back button navigation to prevent returning to the previous screen after logout
//        if (!isUserLoggedIn()) {
//            redirectToLogin();
//        } else {
//            super.onBackPressed();
//        }
    }
}
