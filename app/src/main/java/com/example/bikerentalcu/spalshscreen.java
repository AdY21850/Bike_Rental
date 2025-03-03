package com.example.bikerentalcu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class spalshscreen extends AppCompatActivity {
    TextView appname;

    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalshscreen);


        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF")); // Replace with your actual background color

        // Ensure the status bar text and icons are dark (for light backgrounds)
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        appname = findViewById(R.id.appname);
//        appname2 = findViewById(R.id.aapname2); // Assuming the second TextView is used for the second text
        lottie = findViewById(R.id.lottie);
        ImageView logo = findViewById(R.id.logo);
        // Start the typing animation
        animateText(appname, "why to buy , when you can rent it!");

        //        appname.animate().translationY(1400).setDuration(2000).setStartDelay(0);
        lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), screen2.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        }, 5000);

        // Method to animate text appearing one letter at a time

    }

    private void animateText(final TextView textView, final String text) {
        final Handler handler = new Handler();
        for (int i = 0; i < text.length(); i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView.setText(text.substring(0, index + 1));
                }
            }, 100 * i);
        }
    }
}