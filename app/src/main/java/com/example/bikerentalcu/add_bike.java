package com.example.bikerentalcu;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class add_bike extends AppCompatActivity {

    private Uri imageUri;
    private ImageView uploadedImageView;
    private Spinner categorySpinner, transmissionSpinner;
    private EditText bikeName, bikeNo, price, mileage;
    private TextView addBtn;
    private Button uploadBtn;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    Glide.with(this).load(imageUri).into(uploadedImageView);
                    Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Image selection cancelled or failed", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openImagePicker();
                } else {
                    Toast.makeText(this, "Permission denied to access media", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);

        uploadedImageView = findViewById(R.id.uploadedImageView);
        categorySpinner = findViewById(R.id.category1);
        transmissionSpinner = findViewById(R.id.Transmission1);
        bikeName = findViewById(R.id.bikename1);
        bikeNo = findViewById(R.id.bikeno1);
        price = findViewById(R.id.rentalprice1);
        mileage = findViewById(R.id.mileage1);
        uploadBtn = findViewById(R.id.uploadImageButton);
        addBtn = findViewById(R.id.Add);

        setupSpinners();

        uploadBtn.setOnClickListener(v -> checkPermissionAndOpenPicker());
        addBtn.setOnClickListener(v -> validateAndSubmit());
    }

    private void setupSpinners() {
        String[] categories = {"Road Bikes", "Mountain", "Sports", "Electric", "Heavyweight", "Street"};
        String[] transmissions = {"Automatic", "Manual", "Semi-Automatic"};

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<String> transmissionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transmissions);
        transmissionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transmissionSpinner.setAdapter(transmissionAdapter);
    }

    private void checkPermissionAndOpenPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                openImagePicker();
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                openImagePicker();
            }
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void validateAndSubmit() {
        String name = bikeName.getText().toString().trim();
        String regNo = bikeNo.getText().toString().trim();
        String rentPrice = price.getText().toString().trim();
        String mil = mileage.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        String transmission = transmissionSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(regNo) || TextUtils.isEmpty(rentPrice)
                || TextUtils.isEmpty(mil) || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        sendDataToBackend(name, category, regNo, rentPrice, mil, transmission, imageUri);
    }

    private void sendDataToBackend(String name, String category, String regNo, String rentPrice, String mileage, String transmission, Uri imageUri) {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = prefs.getString("authToken", "");

        File file = new File(FileUtils.getPath(this, imageUri));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("bikepic", file.getName(), reqFile);

        // Convert category to JSONArray for tag
        JSONArray tagsArray = new JSONArray();
        tagsArray.put("Top 1"); // Add the desired tag

        // Convert the tags array to a JSON string
        RequestBody tagBody = RequestBody.create(
                MediaType.parse("application/json"),
                tagsArray.toString() // Send the tag as an array
        );

        // Other bike details
        RequestBody typeOfBikeBody = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody bikeModelBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody registeredNoBody = RequestBody.create(MediaType.parse("text/plain"), regNo);
        RequestBody priceBody = RequestBody.create(MediaType.parse("text/plain"), rentPrice);
        RequestBody aboutBody = RequestBody.create(MediaType.parse("text/plain"),
                "Engine – N/A\nMileage – " + mileage + " kmpl\nTransmission – " + transmission);

        RetrofitInterface retrofitInterface = RetrofitClientInstance.getRetrofitInstance().create(RetrofitInterface.class);
        Call<Void> call = retrofitInterface.addBike(
                "Bearer " + token,
                typeOfBikeBody,
                bikeModelBody,
                aboutBody,
                registeredNoBody,
                priceBody,
                imagePart,
                tagBody // Pass the tags array as a JSON string
        );

        Log.d("AddBikeRequest", "Sending data to backend...");
        Log.d("AddBikeRequest", "TypeOfBike: " + category);
        Log.d("AddBikeRequest", "BikeModel: " + name);
        Log.d("AddBikeRequest", "RegisteredNo: " + regNo);
        Log.d("AddBikeRequest", "Token: Bearer " + token);
        Log.d("AddBikeRequest", "Image Path: " + file.getAbsolutePath());
        Log.d("AddBikeRequest", "Tag JSON: " + tagsArray.toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(add_bike.this, "Bike added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(add_bike.this, "Failed to add bike: " + response.message(), Toast.LENGTH_LONG).show();
                    try {
                        Log.e("AddBikeErrorBody", response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("AddBikeError", "Failed to read error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(add_bike.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("AddBikeFailure", "Throwable: " + t.getMessage(), t);
            }
        });
    }
}
