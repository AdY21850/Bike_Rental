package com.example.bikerentalcu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profile extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final String BASE_URL = "https://bikewale-wyxw.onrender.com";
    private SharedPreferences preferences;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private TextView nameField, emailField, phoneField, dobField, dlField;
    private TextView nameField2, username;
    private FrameLayout updateButton;
    private ImageView profileImage;

    // Made static properly
    TextView walletBalanceText;
    FrameLayout addMoneyButton;

    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        walletBalanceText = findViewById(R.id.rupees);
        addMoneyButton = findViewById(R.id.add_Money);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        fetchWalletBalance(userId);
        setupUI();
        setupRetrofit();
        checkPermissions();

        addMoneyButton.setOnClickListener(v -> {
            addMoneyToWallet(userId, 100); // Add ₹100
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = preferences.getString("authToken", null);
        Log.d("Token Retrieved", "Token: " + token);
        fetchUserData(token);
    }

    private void setupUI() {

        nameField = findViewById(R.id.name1);
        nameField2 = findViewById(R.id.name12);
        emailField = findViewById(R.id.email1);
        phoneField = findViewById(R.id.phone1);
        dobField = findViewById(R.id.dateofbirth);
        dlField = findViewById(R.id.dl1);
        updateButton = findViewById(R.id.move_to_home);
        profileImage = findViewById(R.id.profilepicture);
        username = findViewById(R.id.user_name);

        updateButton.setOnClickListener(v -> {
            Log.d(TAG, "Moving to home screen");
            startActivity(new Intent(profile.this, home.class));
        });
    }

    private void setupRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieManager(this))
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
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
                Toast.makeText(profile.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayUserData(User userDetails) {
        if (userDetails == null) {
            Log.e(TAG, "User details are null");
            return;
        }

        Log.d(TAG, "Displaying user data: " + userDetails.getFullName());

        nameField.setText(userDetails.getFullName());
        nameField2.setText(userDetails.getFullName());
        String nme = userDetails.getFullName();
        // to get and store the user id of the user.
        userId = getIntent().getStringExtra("userId");
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", "");
        //to save the name of the user
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", nme);
        editor.apply();

        String img = userDetails.getImage();
        Log.d("user image fetched from backend",img+"null");
        SharedPreferences.Editor imurl = preferences.edit();
        imurl.putString("userimage", img);
        imurl.apply();

        emailField.setText(userDetails.getEmail());
        phoneField.setText(userDetails.getContactNumber());

        // ✅ Updated to fetch date of birth from additionalDetail
        if (userDetails.getAdditionalDetail() != null) {
            AdditionalDetail additionalDetail = userDetails.getAdditionalDetail();

            // Display date of birth
            String dob = additionalDetail.getDateOfBirth();
            Log.d("date of birth fetched", dob + " fetched from additionalDetail");
            dobField.setText(dob != null ? dob : "N/A");

            // Display gender
            String gender = additionalDetail.getGender();
            Log.d("gender fetched", gender + " fetched from additionalDetail");
            // You can add another TextView for gender if needed, or display it in `dobField` or elsewhere.
            // Example of updating a gender field:
            // genderField.setText(gender != null && !gender.isEmpty() ? gender : "N/A");

            // Display about (this is optional, based on the received data)
            String about = additionalDetail.getAbout();
            Log.d("about fetched", about + " fetched from additionalDetail");
            // You can add a TextView for about if you want to display that as well.
            // Example of updating an about field:
            // aboutField.setText(about != null && !about.isEmpty() ? about : "No information available");

            // Handle bikes created (If you want to display bikes created by the user)
            List<Bike> bikesCreated = additionalDetail.getBikesCreated();
            if (bikesCreated != null && !bikesCreated.isEmpty()) {
                for (Bike bike : bikesCreated) {
                    // You can create a list or another UI element to show the bikes created by the user
                    Log.d("bikes created", "bikes will be displayed " + " registered with " + "registration number for the same");
                    // Example:
                    // bikeModelField.setText(bike.getBikemodel() + " - " + bike.getRegisteredBikeNo());
                }
            } else {
                Log.d("bikes created", "No bikes created by the user");
            }
        } else {
            Log.d("date of birth fetched", "additionalDetail is null");
            dobField.setText("N/A");
        }

        dlField.setText(userDetails.getDrivingLicenseNo());

        String emailPart = userDetails.getEmail().split("@")[0];
        username.setText(emailPart);

        String imageUrl = userDetails.getImage();
        Log.d(TAG, "Image URL: " + imageUrl);

        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            if (imageUrl.endsWith(".svg")) {
                SvgSoftwareLayerSetter.loadSvgImage(this, imageUrl, profileImage);
            } else {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.circle_background)
                        .error(R.drawable.circle_background)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImage);
            }
        } else {
            profileImage.setImageResource(R.drawable.circle_background);
        }
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

    private static class CookieManager implements CookieJar {
        private final List<Cookie> cookies = new ArrayList<>();
        private final Context context;

        CookieManager(Context context) {
            this.context = context;
        }

        @Override
        public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
            this.cookies.clear();
            this.cookies.addAll(cookies);
            saveCookiesToPreferences(cookies);
        }

        @NonNull
        @Override
        public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
            return cookies;
        }

        private void saveCookiesToPreferences(List<Cookie> cookies) {
            SharedPreferences prefs = context.getSharedPreferences("AppCookies", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            for (Cookie cookie : cookies) {
                editor.putString(cookie.name(), cookie.value());
            }
            editor.apply();
        }
    }

    private void fetchWalletBalance(String userId) {
        String url = "https://yourdomain.com/api/users/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject user = response.getJSONObject("userDetails");
                        double wallet = user.getDouble("walletBalance");
                        walletBalanceText.setText("Wallet: ₹" + wallet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(profile.this, "Failed to fetch wallet", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }

    //add the money to the wallet.
    private void addMoneyToWallet(String userId, int amount) {
        String url = "https://bikewale-wyxw.onrender.com/api/v1//update-wallet" + userId;

        JSONObject body = new JSONObject();
        try {
            body.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, body,
                response -> {
                    try {
                        double updatedBalance = response.getDouble("walletBalance");
                        walletBalanceText.setText("Wallet: ₹" + updatedBalance);
                        Toast.makeText(this, "Wallet updated!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error updating wallet", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

}
