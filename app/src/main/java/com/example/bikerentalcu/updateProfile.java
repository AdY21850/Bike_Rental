package com.example.bikerentalcu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.io.InputStream;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class updateProfile extends AppCompatActivity {

    private Uri imageUri;

    ImageView profilePicture;
    MaterialCardView editProfileBtn;
    TextView nameDisplay, usernameDisplay;
    TextView rupeesText, rupeesDesc, bookingCount, bookingText;
    EditText nameInput, emailInput, phoneInput, dobInput, dlInput;
    TextView updateBtn;
    ProgressDialog progressDialog;

    // Modern image picker launcher
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        Glide.with(this).load(imageUri).into(profilePicture);
                        Log.d("ImagePicker", "Selected image URI: " + imageUri.toString());
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);

        profilePicture = findViewById(R.id.profilepicture);
        editProfileBtn = findViewById(R.id.edit_profile);
        nameDisplay = findViewById(R.id.name12);
        usernameDisplay = findViewById(R.id.user_name);
        rupeesText = findViewById(R.id.rupees);
        rupeesDesc = findViewById(R.id.rupeesdesc);
        bookingCount = findViewById(R.id.bookingbox);
        bookingText = findViewById(R.id.booking);

        nameInput = findViewById(R.id.name1);
        emailInput = findViewById(R.id.email1);
        phoneInput = findViewById(R.id.phone1);
        dobInput = findViewById(R.id.dateofbirth);
        dlInput = findViewById(R.id.dl1);

        updateBtn = findViewById(R.id.update);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating profile...");
        progressDialog.setCancelable(false);

        updateBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String dob = dobInput.getText().toString().trim();
            String dl = dlInput.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            updateProfileToServer(name, email, phone, dob, dl, imageUri);
        });

        editProfileBtn.setOnClickListener(v -> openImagePicker());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private void updateProfileToServer(String fullName, String email, String contactNumber, String dob, String drivingLicenseNo, Uri imageUri) {
        progressDialog.show();

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = prefs.getString("authToken", null);
        if (token == null) {
            Toast.makeText(this, "User token not found", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }

        RetrofitInterface retrofitInterface = RetrofitClientInstance.getRetrofitInstance().create(RetrofitInterface.class);

        HashMap<String, String> updatedData = new HashMap<>();
        updatedData.put("fullName", fullName);
        updatedData.put("email", email);
        updatedData.put("contactNumber", contactNumber);
        updatedData.put("dateofBirth", dob);
        updatedData.put("drivingLicenseNo", drivingLicenseNo);

        retrofitInterface.updateUserProfile("Bearer " + token, email, updatedData)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(updateProfile.this, "Profile info updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(updateProfile.this, "Failed to update profile text", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(updateProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

        // Image upload (new logic using content resolver)
        if (imageUri != null) {
            try {
                Log.d("ImageUpload", "Starting image upload");

                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                if (inputStream == null) {
                    Toast.makeText(this, "Unable to open image stream", Toast.LENGTH_SHORT).show();
                    return;
                }

                byte[] imageBytes = new byte[inputStream.available()];
                inputStream.read(imageBytes);
                inputStream.close();

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
                MultipartBody.Part body = MultipartBody.Part.createFormData("displayPicture", "profile.jpg", requestFile);

                retrofitInterface.uploadProfilePicture("Bearer " + token, body)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(updateProfile.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                                    Log.d("ImageUpload", "Upload success");
                                } else {
                                    Toast.makeText(updateProfile.this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show();
                                    Log.e("ImageUpload", "Upload failed: " + response.code() + ", message: " + response.message());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(updateProfile.this, "Upload error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("ImageUpload", "Upload error", t);
                            }
                        });

            } catch (Exception e) {
                Toast.makeText(this, "Failed to process image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ImageUpload", "Exception during upload", e);
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}
