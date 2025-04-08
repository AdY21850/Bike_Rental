package com.example.bikerentalcu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class home extends Activity {

    private static final String TAG = "HomeActivity";

    private ViewPager2 viewPager2;
    private vpAdapter viewPagerAdapter;
    private ArrayList<HomeBannerviewpager> viewpageritemArrayList;
    private DotsIndicator dotsIndicator;
    private ProgressBar progressBar, progressbar;
    private RecyclerView recyclerView, searchRecyclerView;
    private ArrayList<Bike> bikeList;
    private ArrayList<bikeModel> bikeList1;
    private BikeAdapter bikeAdapter, searchBikeAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setStatusBarColor(Color.parseColor("#252B45"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        initializeViews();
        setupNavigation();
        setupSearchView();
        setupViewPager();
        setupRecyclerView();
        setupCategoryClickListeners();
    }
    private void setupViewPager() {
        viewpageritemArrayList = new ArrayList<>();
        String[] imageUrls = {
                "https://res.cloudinary.com/dfmbnrvif/image/upload/v1743380194/banner_home_image_hldh3m.jpg",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/v1743380193/banner_image_home_jaaube.jpg",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/v1743380184/img_1_cpbpfr.jpg"
        };
        for (String url : imageUrls) {
            viewpageritemArrayList.add(new HomeBannerviewpager(url));
        }
        viewPagerAdapter = new vpAdapter(viewpageritemArrayList);
        viewPager2.setAdapter(viewPagerAdapter);
        dotsIndicator.setViewPager2(viewPager2);
        progressBar.setVisibility(View.GONE);
    }
    private void initializeViews() {
        searchView = findViewById(R.id.searchview);
        viewPager2 = findViewById(R.id.viewpagerslider);
        dotsIndicator = findViewById(R.id.dots_indicator);
        progressBar = findViewById(R.id.progressbar);
        progressbar = findViewById(R.id.progressbar2);
        recyclerView = findViewById(R.id.view_brands);
        searchRecyclerView = findViewById(R.id.search_view_recycler);
    }

    private void setupNavigation() {
        findViewById(R.id.to_the_drawer).setOnClickListener(view ->
                startActivity(new Intent(home.this, navdraw.class)));

        findViewById(R.id.cart1).setOnClickListener(view ->
                startActivity(new Intent(home.this, cart.class)));
    }

    private void setupSearchView() {
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    toggleRecyclerView(false);
                } else {
                    filterList(newText);
                }
                return true;
            }
        });
    }

//    private void setupViewPager() {
//        viewpageritemArrayList = new ArrayList<>();
//        viewPagerAdapter = new vpAdapter(viewpageritemArrayList);
//        viewPager2.setAdapter(viewPagerAdapter);
//        dotsIndicator.setViewPager2(viewPager2);
//        progressBar.setVisibility(View.VISIBLE);
//
//        String bannerUrl = "https://your-api.com/api/get-banner-images";
//
//        JsonArrayRequest bannerRequest = new JsonArrayRequest(Request.Method.GET, bannerUrl, null,
//                response -> {
//                    try {
//                        for (int i = 0; i < response.length(); i++) {
//                            JSONObject obj = response.getJSONObject(i);
//                            String imageUrl = obj.getString("imageUrl");
//                            viewpageritemArrayList.add(new HomeBannerviewpager(imageUrl));
//                        }
//                        viewPagerAdapter.notifyDataSetChanged();
//                        progressBar.setVisibility(View.GONE);
//                    } catch (JSONException e) {
//                        Log.e(TAG, "Banner JSON error: " + e.getMessage());
//                        progressBar.setVisibility(View.GONE);
//                    }
//                },
//                error -> {
//                    Log.e(TAG, "Banner fetch error: " + error.getMessage());
//                    Toast.makeText(home.this, "Failed to load banners", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                });
//
//        VolleySingleton.getInstance(this).addToRequestQueue(bannerRequest);
//    }

    private void setupRecyclerView() {
        int numberOfColumns = 2;
        int spacingInDp = 0; // you can change this to 30 or whatever you like
        int spacingInPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                spacingInDp,
                getResources().getDisplayMetrics()
        );

        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(numberOfColumns, spacingInPx, true));

        bikeList = new ArrayList<>();
        bikeList1 = new ArrayList<>();

        bikeAdapter = new BikeAdapter(this, bikeList1);
        searchBikeAdapter = new BikeAdapter(this, new ArrayList<>());

        recyclerView.setAdapter(bikeAdapter);
        searchRecyclerView.setAdapter(searchBikeAdapter);

        searchRecyclerView.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);

        fetchBikeData();
    }


    private void fetchBikeData() {
        String url = "https://bikewale-wyxw.onrender.com/api/v1/bike/allbikes";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject bikeObject = dataArray.getJSONObject(i);

                            String name = bikeObject.getString("bikemodel");
                            String price = bikeObject.getString("rentprice");
                            String transmission = bikeObject.optString("typeofbike", "N/A");
                            String speed = bikeObject.optString("topSpeed", "N/A");
                            String mileage = bikeObject.optString("mileage", "N/A");
                            String imageUrl = bikeObject.getString("bikepic");

                            JSONObject owner = bikeObject.optJSONObject("profile");
                            String ownerName = (owner != null) ? owner.optString("name", "Unknown") : "Unknown";
                            String ownerEmail = (owner != null) ? owner.optString("email", "Unknown") : "Unknown";
                            String ownerContact = (owner != null) ? owner.optString("contactNumber", "Unknown") : "Unknown";
                            String ownerImageUrl = (owner != null) ? owner.optString("profileImage", "") : "";
                            Log.d("owner image url -->",ownerImageUrl);

                            bikeList.add(new Bike(name, price, transmission, speed, mileage, imageUrl, ownerName, ownerEmail, ownerContact, ownerImageUrl));

                            int intPrice = Integer.parseInt(price);
                            bikeList1.add(new bikeModel(name, intPrice, transmission, speed, mileage, ownerName, ownerEmail, ownerContact, imageUrl, ownerImageUrl));
                        }

                        bikeAdapter.notifyDataSetChanged();
                        progressbar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    }
                },
                error -> Log.e(TAG, "Bike fetch error: " + error.toString())
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    private void filterList(String text) {
        List<bikeModel> searchResults = new ArrayList<>();
        for (bikeModel bike : bikeList1) {
            if (bike.getName().toLowerCase().contains(text.toLowerCase()) ||
                    bike.getTransmission().toLowerCase().contains(text.toLowerCase()) ||
                    bike.getOwnerName().toLowerCase().contains(text.toLowerCase())) {
                searchResults.add(bike);
            }
        }

        if (searchResults.isEmpty()) {
            Toast.makeText(this, "No bikes found", Toast.LENGTH_SHORT).show();
            toggleRecyclerView(false);
        } else {
            searchBikeAdapter.updateList(searchResults);
            toggleRecyclerView(true);
        }
    }

    private void toggleRecyclerView(boolean showSearch) {
        searchRecyclerView.setVisibility(showSearch ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(showSearch ? View.GONE : View.VISIBLE);
    }

    private void setupCategoryClickListeners() {
        CardView[] cardViews = {
                findViewById(R.id.family_cars),
                findViewById(R.id.cheapcars),
                findViewById(R.id.sports_Bikes),
                findViewById(R.id.Electrical_Bikes),
                findViewById(R.id.heavy_bikes),
                findViewById(R.id.street_bikes)
        };

        String[] categories = {
                "Road Bikes",
                "Mountain Bikes",
                "Sports Bikes",
                "Electric Bikes",
                "Heavyweight",
                "Street Bikes"
        };

        for (int i = 0; i < cardViews.length; i++) {
            int index = i;
            cardViews[i].setOnClickListener(v -> {
                swapColors(cardViews[index]);
                Toast.makeText(home.this, "Selected: " + categories[index], Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void swapColors(CardView selectedCard) {
        resetCardViews(selectedCard);
        selectedCard.setCardBackgroundColor(Color.parseColor("#2B4C59"));
        ((TextView) selectedCard.getChildAt(0)).setTextColor(Color.WHITE);
    }

    private void resetCardViews(CardView selectedCard) {
        CardView[] cardViews = {
                findViewById(R.id.family_cars),
                findViewById(R.id.cheapcars),
                findViewById(R.id.sports_Bikes),
                findViewById(R.id.Electrical_Bikes),
                findViewById(R.id.heavy_bikes),
                findViewById(R.id.street_bikes)
        };

        for (CardView cardView : cardViews) {
            if (cardView != selectedCard) {
                cardView.setCardBackgroundColor(Color.WHITE);
                ((TextView) cardView.getChildAt(0)).setTextColor(Color.parseColor("#2B4C59"));
            }
        }
    }
}
