package com.example.bikerentalcu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class otp extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF")); // Replace with your actual background color

        // Ensure the status bar text and icons are dark (for light backgrounds)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}