package com.example.bikerentalcu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private ArrayList<bikeModel> originalBikeList = new ArrayList<>();
    private BikeAdapter bikeAdapter, searchBikeAdapter;
    private SearchView searchView;
    private Handler sliderHandler = new Handler();

    private String currentCategory = "";
    private List<bikeModel> filteredCategoryList = new ArrayList<>();

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager2.getCurrentItem();
            int nextItem = (currentItem + 1) % viewpageritemArrayList.size();
            viewPager2.setCurrentItem(nextItem, true);
            sliderHandler.postDelayed(this, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        initializeViews();
        setupNavigation();
        setupSearchView();
        setupViewPager();
        setupRecyclerView();
        setupCategoryClickListeners();

        FloatingActionButton fabAdd = findViewById(R.id.add_bike);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(home.this, "FAB clicked!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(home.this, add_bike.class);
                startActivity(intent);
            }
        });

    }

    private void initializeViews() {
        searchView = findViewById(R.id.searchview);
        viewPager2 = findViewById(R.id.viewpagerslider);
        dotsIndicator = findViewById(R.id.dots_indicator);
        progressBar = findViewById(R.id.progressbar);
        progressbar = findViewById(R.id.progressbar2);
        recyclerView = findViewById(R.id.view_brands);
        searchRecyclerView = findViewById(R.id.search_view_recycler);
        searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setupNavigation() {
        findViewById(R.id.to_the_drawer).setOnClickListener(view ->
                startActivity(new Intent(home.this, navdraw.class)));

        findViewById(R.id.cart1).setOnClickListener(view ->
                startActivity(new Intent(home.this, cart.class)));
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
        sliderHandler.postDelayed(sliderRunnable, 3000);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    private void setupSearchView() {
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

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

    private void setupRecyclerView() {
        int numberOfColumns = 2;
        int spacingInDp = 0;
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
                            String ownerName = (owner != null) ? owner.optString("fullName", "Unknown") : "Unknown";
                            String ownerEmail = (owner != null) ? owner.optString("email", "Unknown") : "Unknown";
                            String ownerContact = (owner != null) ? owner.optString("contactNumber", "Unknown") : "Unknown";
                            String ownerImageUrl = (owner != null) ? owner.optString("upiId", "") : "";
                            String ownerupi = (owner != null) ? owner.optString("upiId", "") : "";

                            Bike bike = new Bike(name, price, transmission, speed, mileage, imageUrl, ownerName, ownerEmail, ownerContact, ownerImageUrl, ownerupi);
                            bikeList.add(bike);

                            bikeModel model = new bikeModel(name, Integer.parseInt(price), transmission, speed, mileage, ownerName, ownerEmail, ownerContact, imageUrl, ownerImageUrl, ownerupi);
                            bikeList1.add(model);
                            originalBikeList.add(model);
                        }

                        bikeAdapter.updateList(originalBikeList);
                        progressbar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    }
                },
                error -> Log.e(TAG, "Bike fetch error: " + error.toString())
        );

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void toggleRecyclerView(boolean showSearch) {
        searchRecyclerView.setVisibility(showSearch ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(showSearch ? View.GONE : View.VISIBLE);
    }

    private void filterList(String text) {
        List<bikeModel> baseList = currentCategory.isEmpty() ? originalBikeList : filteredCategoryList;
        List<bikeModel> searchResults = new ArrayList<>();

        for (bikeModel bike : baseList) {
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
                "Mountain",
                "Sports",
                "Electric",
                "Heavyweight",
                "Street"
        };

        for (int i = 0; i < cardViews.length; i++) {
            int index = i;
            cardViews[i].setOnClickListener(v -> {
                swapColors(cardViews[index]);
                Toast.makeText(home.this, "Selected: " + categories[index], Toast.LENGTH_SHORT).show();
                filterByCategory(categories[index]);
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
            if (selectedCard == null || cardView != selectedCard) {
                cardView.setCardBackgroundColor(Color.WHITE);
                ((TextView) cardView.getChildAt(0)).setTextColor(Color.parseColor("#2B4C59"));
            }
        }
    }

    private void filterByCategory(String category) {
        currentCategory = category;
        filteredCategoryList.clear();

        for (bikeModel bike : originalBikeList) {
            if (bike.getTransmission().equalsIgnoreCase(category)) {
                filteredCategoryList.add(bike);
            }
        }

        if (filteredCategoryList.isEmpty()) {
            Toast.makeText(this, "No bikes found in " + category, Toast.LENGTH_SHORT).show();
        }

        bikeAdapter.updateList(filteredCategoryList);
    }

    @Override
    public void onBackPressed() {
        if (searchRecyclerView.getVisibility() == View.VISIBLE || recyclerView.getVisibility() == View.GONE) {
            searchView.setQuery("", false);
            searchView.clearFocus();

            recyclerView.setVisibility(View.VISIBLE);
            searchRecyclerView.setVisibility(View.GONE);

            if (!currentCategory.isEmpty()) {
                bikeAdapter.updateList(filteredCategoryList);
            } else {
                bikeAdapter.updateList(originalBikeList);
            }

        } else {
            super.onBackPressed();
        }
    }
}
