package com.example.bikerentalcu;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import android.widget.ImageView;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class
profile extends Activity {

    private static final String TAG = "ProfileActivity";
    private static final String BASE_URL = "https://bikewale-wyxw.onrender.com"; // Replace with your actual backend URL

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    //
    private EditText nameField, emailField, phoneField, dobField, dlField;
    private FrameLayout updateButton;
    private ImageView profileImage; // For displaying user image

    private Uri imageUri; // To hold the selected image URI
    private ActivityResultLauncher<Uri> imagePickerLauncher;

    // Cookie manager class
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
            saveCookiesToSharedPreferences(cookies); // Save cookies to SharedPreferences
        }

        @NonNull
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> loadedCookies = new ArrayList<>();
            SharedPreferences sharedPreferences = context.getSharedPreferences("AppCookies", MODE_PRIVATE);

            // Load stored cookies
            for (String cookieName : sharedPreferences.getAll().keySet()) {
                String cookieValue = sharedPreferences.getString(cookieName, null);
                if (cookieValue != null) {
                    loadedCookies.add(new Cookie.Builder()
                            .name(cookieName)
                            .value(cookieValue)
                            .domain(url.host())
                            .build());
                }
            }
            return loadedCookies;
        }

        private void saveCookiesToSharedPreferences(List<Cookie> cookies) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("AppCookies", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (Cookie cookie : cookies) {
                editor.putString(cookie.name(), cookie.value());
            }
            editor.apply();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set status bar color
        getWindow().setStatusBarColor(Color.parseColor("#838383"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Initialize EditText fields
        nameField = findViewById(R.id.name1);
        emailField = findViewById(R.id.email1);
        phoneField = findViewById(R.id.phone1);
        dobField = findViewById(R.id.dateofbirth);
        dlField = findViewById(R.id.dl1);
        updateButton = findViewById(R.id.move_to_home);
        profileImage = findViewById(R.id.profilepicture); // Ensure you have this ImageView in your layout

        // Check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        // Initialize Retrofit with cookie management
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieManager(this)) // Pass context
                .addInterceptor(logging)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Fetch user data
        fetchUserData();

        // Set up image upload
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), (ActivityResultCallback<Uri>) result -> {
                    if (result != null) {
                        imageUri = result;
                        Glide.with(profile.this).load(imageUri).into(profileImage); // Display selected image
                    }
                });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch image picker
                imagePickerLauncher.launch(Uri.parse("image/*"));
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override

                public void onClick(View v) {
                    startActivity(new Intent(profile.this,home.class));
                }
            });
            }


    private ActivityResultLauncher<Uri> registerForActivityResult(ActivityResultContracts.GetContent getContent, ActivityResultCallback<Uri> uriActivityResultCallback) {
        return null;
    }

    private void fetchUserData() {
        String token = getIntent().getStringExtra("authtoken");
        if (token != null) {
            Call<ResponseData> call = retrofitInterface.getUserProfile("Bearer " + token);
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(@NonNull Call<ResponseData> call, @NonNull Response<ResponseData> response) {
                    Log.d(TAG, "Full Response: " + response);

                    if (response.isSuccessful() && response.body() != null) {
                        ResponseData userDetails = response.body();
                        displayUserData(userDetails.getUserDetails());
                    } else {
                        handleErrorResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Log.e(TAG, "Network Failure: " + t.getMessage(), t);
                    Toast.makeText(profile.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "Token is null");
            Toast.makeText(this, "Unable to retrieve token", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleErrorResponse(Response<?> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
            Log.e(TAG, "Error: " + errorBody);
            Toast.makeText(this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error reading error body: " + e.getMessage());
        }
    }

    private void displayUserData(User userDetails) {
        Log.d(TAG, "Displaying user data");

        // Get data
        String name = userDetails.getFullName();
        String email = userDetails.getEmail();
        String phone = userDetails.getContactNumber();
        String dlNo = userDetails.getDrivingLicenseNo();
        String imageUrl = userDetails.getImage().substring(0, userDetails.getImage().indexOf(' ')); // Handling image URL

        // Set data in views
        nameField.setText(name);
        emailField.setText(email);
        phoneField.setText(phone);
//        dobField.setText(userDetails.getDateOfBirth()); // Assuming this field exists in User class
        dlField.setText(dlNo);

        Log.d(TAG, "Image URL: " + imageUrl);
        // Load image using Glide
        Picasso.get()
                .load(imageUrl)
                .into(profileImage);
    }

    private void uploadImage() {
        if (imageUri != null) {
            String filePath = getRealPathFromURI(imageUri);
            File file = new File(filePath);

            if (file.exists()) {
                RequestBody requestFile = RequestBody.create(file, okhttp3.MediaType.parse("image/*"));
                MultipartBody.Part body = MultipartBody.Part.create(requestFile);

                Call<Void> call = retrofitInterface.uploadProfilePicture("Bearer " + getIntent().getStringExtra("authtoken"), body);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(profile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(profile.this, "Image upload failed: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(profile.this, "Image upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
