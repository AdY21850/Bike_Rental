package com.example.bikerentalcu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class home extends Activity {

    private static final String TAG = "HomeActivity";

    private ViewPager2 viewPager2;
    private vpAdapter viewPagerAdapter;
    private ArrayList<HomeBannerviewpager> viewpageritemArrayList;
    private RetrofitInterface retrofitInterface;
    TextView textExplore, textLike, textCart, textProfile;

    private ProgressBar progressBar, progressbar;
    private RecyclerView recyclerView, searchRecyclerView;
    private ArrayList<Bike> bikeList;
    private SharedPreferences preferences;
    private SharedPreferences preferencess;
    private ArrayList<bikeModel> bikeList1;
    private ArrayList<bikeModel> originalBikeList = new ArrayList<>();
    private BikeAdapter bikeAdapter, searchBikeAdapter;
    private SearchView searchView;
    private Handler sliderHandler = new Handler();
    private String currentCategory = "";
    private List<bikeModel> filteredCategoryList = new ArrayList<>();
    private String  userName;
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

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        retrofitInterface = RetrofitClientInstance.getRetrofitInstance().create(RetrofitInterface.class);

        textExplore = findViewById(R.id.text_explore);
        textLike = findViewById(R.id.text_like);
        textCart = findViewById(R.id.text_cart);
        textProfile = findViewById(R.id.text_profile);

        initializeViews();
        setupNavigation();
        setupSearchView();
        setupViewPager();
        setupRecyclerView();
        setupCategoryClickListeners();

        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = preferences.getString("authToken", null);
        Log.d("Token Retrieved", "Token: " + token);
        if (token!=null) {
            fetchUserData(token); // ✅ CALLING the method!
        }

    }

    private void initializeViews() {
        searchView = findViewById(R.id.searchview);
        viewPager2 = findViewById(R.id.viewpagerslider);

        progressBar = findViewById(R.id.progressbar);
        progressbar = findViewById(R.id.progressbar2);
        recyclerView = findViewById(R.id.view_brands);
        searchRecyclerView = findViewById(R.id.search_view_recycler);
        searchRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void setupNavigation() {

        ImageView navdrawer = findViewById(R.id.to_the_drawer);
        navdrawer.setOnClickListener(v -> {
            updateNavSelection(textCart);

            Intent intent = new Intent(home.this, navdraw.class);
            intent.putExtra("userName", userName); // Pass userName
            startActivity(intent);
        });


        LinearLayout fabAdd = findViewById(R.id.add_bike);
        fabAdd.setOnClickListener(v -> {
            updateNavSelection(textCart);
            startActivity(new Intent(home.this, add_bike.class));
        });

        LinearLayout cart = findViewById(R.id.cart);
        cart.setOnClickListener(v -> {
            updateNavSelection(textLike);
            startActivity(new Intent(home.this, cart.class));
        });

        LinearLayout nav_explore = findViewById(R.id.nav_explore);
        nav_explore.setOnClickListener(v -> {
            updateNavSelection(textExplore);
            startActivity(new Intent(home.this, bikes_rented.class));
        });

        LinearLayout nav_profile = findViewById(R.id.nav_profile);
        nav_profile.setOnClickListener(v -> {
            updateNavSelection(textProfile);
            startActivity(new Intent(home.this, profile.class));
        });

        findViewById(R.id.notification).setOnClickListener(view -> {
            startActivity(new Intent(home.this, notifications.class));
        });
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

        progressBar.setVisibility(View.GONE);
        sliderHandler.postDelayed(sliderRunnable, 3000);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
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
        int columns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, columns));

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
                            String ownerUpi = (owner != null) ? owner.optString("upiId", "") : "";

                            bikeModel model = new bikeModel(name, Integer.parseInt(price), transmission, speed, mileage, ownerName, ownerEmail, ownerContact, imageUrl, ownerImageUrl, ownerUpi);
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

        // Hide banners
        viewPager2.setVisibility(showSearch ? View.GONE : View.VISIBLE);


        // Hide category cards
        findViewById(R.id.family_cars).setVisibility(showSearch ? View.GONE : View.VISIBLE);
        findViewById(R.id.cheapcars).setVisibility(showSearch ? View.GONE : View.VISIBLE);
        findViewById(R.id.sports_Bikes).setVisibility(showSearch ? View.GONE : View.VISIBLE);
        findViewById(R.id.Electrical_Bikes).setVisibility(showSearch ? View.GONE : View.VISIBLE);
        findViewById(R.id.heavy_bikes).setVisibility(showSearch ? View.GONE : View.VISIBLE);
        findViewById(R.id.street_bikes).setVisibility(showSearch ? View.GONE : View.VISIBLE);

        // Hide navigation bar items (optional - depends on your design)
//        findViewById(R.id.bottom_navigation).setVisibility(showSearch ? View.GONE : View.VISIBLE);

        // Hide progress bar if needed (optional)
        progressbar.setVisibility(showSearch ? View.GONE : View.VISIBLE);
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
        CardView[] cards = {
                findViewById(R.id.family_cars),
                findViewById(R.id.cheapcars),
                findViewById(R.id.sports_Bikes),
                findViewById(R.id.Electrical_Bikes),
                findViewById(R.id.heavy_bikes),
                findViewById(R.id.street_bikes)
        };

        String[] categories = {"Road Bikes", "Mountain", "Sports", "Electric", "Heavyweight", "Street"};

        for (int i = 0; i < cards.length; i++) {
            int finalI = i;
            cards[i].setOnClickListener(v -> {
                swapColors(cards[finalI]);
                currentCategory = categories[finalI];
                filterByCategory(currentCategory);
            });
        }
    }

    private void swapColors(CardView selectedCard) {
        resetCardViews(selectedCard);
        selectedCard.setCardBackgroundColor(Color.parseColor("#2B4C59"));
        ((TextView) selectedCard.getChildAt(0)).setTextColor(Color.WHITE);
    }

    private void resetCardViews(CardView selectedCard) {
        CardView[] cards = {
                findViewById(R.id.family_cars),
                findViewById(R.id.cheapcars),
                findViewById(R.id.sports_Bikes),
                findViewById(R.id.Electrical_Bikes),
                findViewById(R.id.heavy_bikes),
                findViewById(R.id.street_bikes)
        };

        for (CardView card : cards) {
            if (card != selectedCard) {
                card.setCardBackgroundColor(Color.WHITE);
                ((TextView) card.getChildAt(0)).setTextColor(Color.parseColor("#2B4C59"));
            }
        }
    }

    private void filterByCategory(String category) {
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
        if (searchRecyclerView.getVisibility() == View.VISIBLE) {
            toggleRecyclerView(false);
            searchView.setQuery("", false);
            searchView.clearFocus();
        } else if (!currentCategory.isEmpty()) {
            resetCardViews(null);
            currentCategory = "";
            bikeAdapter.updateList(originalBikeList);
            Toast.makeText(this, "Showing all bikes", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed(); // Exit app
        }
    }

    private void updateNavSelection(TextView selectedTextView) {
        textExplore.setVisibility(View.GONE);
        textLike.setVisibility(View.GONE);
        textCart.setVisibility(View.GONE);
        textProfile.setVisibility(View.GONE);
        selectedTextView.setVisibility(View.VISIBLE);
    }
    private void fetchUserData(String token) {
        Log.d(TAG, "Fetching profile with token: " + token);

        if (token == null) {
            Toast.makeText(this, "Authentication error", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<ResponseData> call = retrofitInterface.getUserProfile("Bearer " + token);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "User profile fetched successfully");
                    displayUserData(response.body().getUserDetails());
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage(), t);
                Toast.makeText(home.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayUserData(User userDetails) {
        if (userDetails == null) {
            Log.e(TAG, "User details are null");
            return;
        }

        Log.d(TAG, "Displaying user data: " + userDetails.getFullName());


        String userName = userDetails.getFullName();
//         to get and store the user id of the user.
//
//        to save the name of the user
        SharedPreferences sharedPreferencess = getSharedPreferences("myuser", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferencess.edit();
        editor.putString("userName", userName);
        editor.apply();

        String img = userDetails.getImage();
        Log.d("user image fetched from backend",img+"null");
        SharedPreferences.Editor imurl = preferences.edit();
        imurl.putString("userimage", img);
        imurl.apply();




    }
    private void handleErrorResponse(Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
            Log.e(TAG, "API Error: " + errorBody);
            Toast.makeText(this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception while reading error body", e);
        }
    }

}
