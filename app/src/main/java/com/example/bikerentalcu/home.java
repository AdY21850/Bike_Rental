package com.example.bikerentalcu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

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
    private ArrayList<bikeModel> bikeList;
    private BikeAdapter bikeAdapter, searchBikeAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setStatusBarColor(Color.parseColor("#252B45"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        Log.d(TAG, "onCreate: Activity started");

        initializeViews();
        setupNavigation();
        setupSearchView();
        setupViewPager();
        setupRecyclerView();
        setupCategoryClickListeners();
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
        findViewById(R.id.to_the_drawer).setOnClickListener(view -> {
            Log.d(TAG, "Navigation drawer clicked");
            startActivity(new Intent(home.this, navdraw.class));
        });

        findViewById(R.id.cart1).setOnClickListener(view -> {
            Log.d(TAG, "Cart clicked");
            startActivity(new Intent(home.this, cart.class));
        });
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

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bikeList = new ArrayList<>();
        addBikeData();

        bikeAdapter = new BikeAdapter(this, new ArrayList<>(bikeList));
        searchBikeAdapter = new BikeAdapter(this, new ArrayList<>());

        recyclerView.setAdapter(bikeAdapter);
        searchRecyclerView.setAdapter(searchBikeAdapter);

        searchRecyclerView.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
    }

    private void addBikeData() {
        bikeList.add(new bikeModel("Apache", 400, "Manual", "240 km/h", "30 kmpl", "Pawan Pandey", "papajikimail2@gmail.com", "9555883490",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/v1743380193/apache_iellgk.jpg",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/v1743386623/images_yvnq0s.jpg"));

        bikeList.add(new bikeModel("Activa", 120, "Automatic", "110 km/h", "35 kmpl", "Jane Smith", "janesmith@example.com", "+987654321",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/v1743380194/activa_ytjdao.jpg",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/v1743386618/images_n1dbos.jpg"));

        bikeList.add(new bikeModel("Royal Enfield", 200, "Manual", "120 km/h", "30 kmpl", "John Doe", "johndoe@example.com", "+123456789",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/v1743380182/royalenfield_libvj0.jpg",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/v1743386524/gsvbejvulurft3bhfoh0.jpg"));
        bikeList.add(new bikeModel(
                "Apache", 400, "Manual", "240 km/h", "30 kmpl",
                "pawan pandey", "papajikimail2@gmail.com", "9555883490",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743380193/apache_iellgk.jpg?_s=public-apps",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743386623/images_yvnq0s.jpg?_s=public-apps"
        ));
        bikeList.add(new bikeModel(
                "Activa", 120, "Automatic", "110 km/h", "35 kmpl",
                "Jane Smith", "janesmith@example.com", "+987654321",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743380194/activa_ytjdao.jpg?_s=public-apps",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743386618/images_n1dbos.jpg?_s=public-apps"
        ));
        bikeList.add(new bikeModel(
                "Royal Enfield", 200, "Manual", "120 km/h", "30 kmpl",
                "John Doe", "johndoe@example.com", "+123456789",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743380182/royalenfield_libvj0.jpg?_s=public-apps",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743386524/gsvbejvulurft3bhfoh0.jpg?_s=public-apps"
        ));
        bikeList.add(new bikeModel(
                "Duke", 400, "Semi-Automatic", "450 km/h", "10 kmpl",
                "bablu badmosh", "papajikimail2@gmail.com", "9555883490",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743380186/duke_zazboo.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742665112/iaozlnauip5g9fkm5wsr.jpg?_s=public-apps"
        ));
        bikeList.add(new bikeModel(
                "suzuki", 180, "Manual", "150 km/h", "30 kmpl",
                "chitranjan tripathi", "papajikimail2@gmail.com", "9555883490",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743380186/diodiorightfrontthreequarter_nelsos.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661925/sydney_sweeney_xneyx7.jpg?_s=public-sapp\""
        ));
        bikeList.add(new bikeModel(
                "harleyXhonda", 280, "Manual", "120 km/h", "30 kmpl",
                "Munna Bhaiya", "papajikimail2@gmail.com", "9555883490",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743380185/harleyxhonda_tbfcie.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661928/will_attenborough_m5mo2b.jpg?_s=public-apps"
        ));
        bikeList.add(new bikeModel(
                "bmwR20", 4000, "Manual", "290 km/h", "20 kmpl",
                "Ram Bahadur", "papajikimail2@gmail.com", "9555883490",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743597010/bmw_r20_concept-removebg-preview_yfpgso.jpg?_s=public-apps",
                "https://res.cloudinary.com/dorc5p2jg/image/upload/fl_preserve_transparency/v1742661922/resume_img_1_mhlikw.jpg?_s=public-apps"
        ));
        bikeList.add(new bikeModel(
                "pulsar", 100, "Automatic", "190 km/h", "30 kmpl",
                "Dinanath Shastree", "papajikimail2@gmail.com", "9555883490",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743380183/pulsarrs200rsleftfrontthreequarter_yphm8k.jpg?_s=public-apps",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743386605/images_m2e6ti.jpg?_s=public-apps"
        ));
        bikeList.add(new bikeModel(
                "splendor", 150, "Manual | semi-Atuo", "120 km/h", "30 kmpl",
                "billu chamar", "papajikimail2@gmil.com", "9555883490",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743386852/hero-select-model-blue-black-1706531445236_edhupn.jpg?_s=public-apps",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743386590/images_mfeomn.jpg?_s=public-apps"
        ));
        bikeList.add(new bikeModel(
                "gt 650", 320, "Manual", "320 km/h", "30 kmpl",
                "Parashuram Sharma", "papajikimail2@gmail.com", "9555883490",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743386806/royal-enfield-select-model-british-racing-green-1668419802695_rufcsy.jpg?_s=public-apps",
                "https://res.cloudinary.com/dfmbnrvif/image/upload/fl_preserve_transparency/v1743386563/images_yq8mnv.jpg?_s=public-apps"
        ));
    }

    private void filterList(String text) {
        List<bikeModel> searchResults = new ArrayList<>();
        for (bikeModel bike : bikeList) {
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
        if (showSearch) {
            searchRecyclerView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            searchRecyclerView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
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
        ((TextView) selectedCard.getChildAt(0)).setTextColor(Color.parseColor("#FFFFFF"));
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
