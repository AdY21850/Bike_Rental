package com.example.bikerentalcu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class home extends Activity {

    private static final String TAG = "HomeActivity"; // Tag for logging
    private static final String SELECTED_COLOR = "#2B4C59"; // Color for the selected state
    private static final String DEFAULT_COLOR = "#FFFFFF"; // Default color for the background (white)

    ViewPager2 viewPager2;
    vpAdapter viewPagerAdapter;
    ArrayList<HomeBannerviewpager> viewpageritemArrayList;
    DotsIndicator dotsIndicator;
    ProgressBar progressBar;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: Activity started");

        //------------------------on click listeners to drawer and cart-------------------------------------------------
        //--------------------------drawer----------------------------------------------------------------------
        ImageView navdraw = findViewById(R.id.to_the_drawer);
        if (navdraw != null) {
            navdraw.setOnClickListener(view -> {
                Log.d(TAG, "onCreate: Navigation drawer clicked");
                startActivity(new Intent(home.this, navdraw.class));
            });
        } else {
            Log.e(TAG, "onCreate: Navigation drawer ImageView is null");
        }

        //-----------------------cart -----------------------------------
        ImageView cartimage = findViewById(R.id.cart1);
        if (cartimage != null) {
            cartimage.setOnClickListener(view -> {
                Log.d(TAG, "onCreate: Cart clicked");
                startActivity(new Intent(home.this, cart.class));
            });
        } else {
            Log.e(TAG, "onCreate: Cart ImageView is null");
        }

        //----------------------------------------------ViewPager banner---------------------------------------------------
        viewPager2 = findViewById(R.id.viewpagerslider);
        dotsIndicator = findViewById(R.id.dots_indicator);
        progressBar = findViewById(R.id.progressbar);

        if (viewPager2 != null) {
            Log.d(TAG, "onCreate: ViewPager2 initialized");
        } else {
            Log.e(TAG, "onCreate: ViewPager2 is null");
        }

        if (dotsIndicator != null) {
            Log.d(TAG, "onCreate: DotsIndicator initialized");
        } else {
            Log.e(TAG, "onCreate: DotsIndicator is null");
        }

        if (progressBar != null) {
            Log.d(TAG, "onCreate: ProgressBar initialized");
        } else {
            Log.e(TAG, "onCreate: ProgressBar is null");
        }

        viewpageritemArrayList = new ArrayList<>();

        // Adding static images from the drawable folder
        try {
            viewpageritemArrayList.add(new HomeBannerviewpager(R.drawable.banner_home_image));
            viewpageritemArrayList.add(new HomeBannerviewpager(R.drawable.banner_image_home));
            viewpageritemArrayList.add(new HomeBannerviewpager(R.drawable.banner_bike_rental));
            viewpageritemArrayList.add(new HomeBannerviewpager(R.drawable.rental_bike));
            viewpageritemArrayList.add(new HomeBannerviewpager(R.drawable.rental_poster));
            Log.d(TAG, "onCreate: Static images added to the ViewPager");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error adding static images", e);
        }

        // Show ProgressBar while loading images
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "onCreate: ProgressBar set to VISIBLE");
        }

        // Fetch dynamic images from backend and add them to the list
        loadImagesFromBackend();

        // Set up the ViewPager2 adapter
        viewPagerAdapter = new vpAdapter(viewpageritemArrayList);
        viewPager2.setAdapter(viewPagerAdapter);
        Log.d(TAG, "onCreate: Adapter set to ViewPager2");

        // Bind the DotsIndicator to ViewPager2
        dotsIndicator.setViewPager2(viewPager2);
        Log.d(TAG, "onCreate: DotsIndicator bound to ViewPager2");

        // Set ViewPager2 visibility to VISIBLE after setting the adapter
        viewPager2.setVisibility(View.VISIBLE);
        dotsIndicator.setVisibility(View.VISIBLE); // Make DotsIndicator visible too
        Log.d(TAG, "onCreate: ViewPager2 and DotsIndicator visibility set to VISIBLE");

        // Hide ProgressBar after the adapter is set
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            Log.d(TAG, "onCreate: ProgressBar set to GONE");
        }
        //-----------------------------------------recyclerview--------------------
        progressbar = findViewById(R.id.progressbar2);
        ArrayList<contactModel> arrdetails = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.view_brands);


        // Set the GridLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Log.d(TAG, "onCreate: RecyclerView initialized and layout manager set");

        // Add bike details to the ArrayList
        try {
            arrdetails.add(new contactModel(R.drawable.duke, "Duke", 400));
            arrdetails.add(new contactModel(R.drawable.royalenfield, "royalenfield", 300));
            arrdetails.add(new contactModel(R.drawable.scooty, "scooter", 100));
            arrdetails.add(new contactModel(R.drawable.pulsar, "pulsar", 250));
            arrdetails.add(new contactModel(R.drawable.activa, "activa", 150));
            arrdetails.add(new contactModel(R.drawable.supermeteor, "supermeteor", 350));
            arrdetails.add(new contactModel(R.drawable.rider, "rider", 300));
            arrdetails.add(new contactModel(R.drawable.threequater, "threequater", 120));
            arrdetails.add(new contactModel(R.drawable.razur, "razur", 60));
            arrdetails.add(new contactModel(R.drawable.harleyxhonda, "harleyxhonda", 500));
            arrdetails.add(new contactModel(R.drawable.pulsaradvance, "pulsaradvance", 450));
            arrdetails.add(new contactModel(R.drawable.apache, "apache", 350));
            Log.d(TAG, "onCreate: Bike details added to the ArrayList");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error adding bike details to ArrayList", e);
        }

        // Set RecyclerBikedetailsAdapter to RecyclerView
        try {
            recyclerBikedetailsAdapter recyclerBikedetailsAdapter = new recyclerBikedetailsAdapter(this, arrdetails);
            recyclerView.setAdapter(recyclerBikedetailsAdapter);
            Log.d(TAG, "onCreate: RecyclerBikedetailsAdapter set to RecyclerView");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error setting adapter to RecyclerView", e);
        }

        // Add spacing between grid items
     // Change this to the desired spacing in pixels (10 reduces vertical spacing)


        // Hide ProgressBar after loading is complete
        progressbar.setVisibility(View.GONE);
        Log.d(TAG, "onCreate: ProgressBar set to GONE");


        //-------------------------------------category functionality-------------------
        setupClickListeners();
    }

    private void loadImagesFromBackend() {
        try {
            // Example URLs, replace with actual image URLs fetched from your backend
            // viewpageritemArrayList.add(new HomeBannerviewpager("https://example.com/images/banner1.jpg"));
            // viewpageritemArrayList.add(new HomeBannerviewpager("https://example.com/images/banner2.jpg"));
            Log.d(TAG, "loadImagesFromBackend: Dynamic images added from backend");
        } catch (Exception e) {
            Log.e(TAG, "loadImagesFromBackend: Error fetching images from backend", e);
        }
    }

    private static final String SELECTED_FRAME_COLOR = "#2B4C59";
    private static final String DEFAULT_FRAME_COLOR = "#FFFFFF"; // Default color when reset
    private static final String DEFAULT_TEXT_COLOR = "#2B4C59";  // Text color when reset
    private static final String SELECTED_TEXT_COLOR = "#FFFFFF"; // White color for selected text

    private void setupClickListeners() {
        // Array of FrameLayouts and their corresponding TextViews
        FrameLayout[] frameLayouts = {
                findViewById(R.id.family_cars),
                findViewById(R.id.cheapcars),
                findViewById(R.id.sports_Bikes),
                findViewById(R.id.Electical_Bikes),
                findViewById(R.id.heavy_bikes),
                findViewById(R.id.street_bikes)
        };

        // Array of category names
        String[] categories = {
                "Road Bikes",
                "Mountain Bikes",
                "Sports Bikes",
                "Electric Bikes",
                "Heavyweight",
                "Street Bikes"
        };

        for (int i = 0; i < frameLayouts.length; i++) {
            int index = i; // Required for the inner class
            frameLayouts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Swap colors for the selected FrameLayout and its TextView
                    swapColors((FrameLayout) v);

                    // Show the selected category
                    Toast.makeText(home.this, "Selected: " + categories[index], Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void swapColors(FrameLayout selectedFrame) {
        TextView selectedTextView = (TextView) selectedFrame.getChildAt(0); // Assuming the TextView is the first child

        // Set the selected FrameLayout background and text color
        selectedFrame.setBackgroundColor(Color.parseColor(SELECTED_FRAME_COLOR));
        selectedTextView.setTextColor(Color.parseColor(SELECTED_TEXT_COLOR));

        // Reset other FrameLayouts
        resetFrameLayouts(selectedFrame);
    }

    private void resetFrameLayouts(FrameLayout selectedFrame) {
        FrameLayout[] frameLayouts = {
                findViewById(R.id.family_cars),
                findViewById(R.id.cheapcars),
                findViewById(R.id.sports_Bikes),
                findViewById(R.id.Electical_Bikes),
                findViewById(R.id.heavy_bikes),
                findViewById(R.id.street_bikes)
        };

        for (FrameLayout frameLayout : frameLayouts) {
            if (frameLayout != selectedFrame) { // Don't reset the selected frame
                frameLayout.setBackgroundResource(0); // Remove any existing drawable resource
                frameLayout.setBackgroundColor(Color.parseColor(DEFAULT_FRAME_COLOR)); // Set default color
                ((TextView) frameLayout.getChildAt(0)).setTextColor(Color.parseColor(DEFAULT_TEXT_COLOR)); // Reset text color
            }
        }
    }

}

