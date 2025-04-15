package com.example.bikerentalcu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class screen2 extends Activity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://bikewale-wyxw.onrender.com";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen2);

        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        findViewById(R.id.Login).setOnClickListener(view -> handleLoginDialog());
        findViewById(R.id.signup).setOnClickListener(view -> handleSignupDialog());
    }

    private void handleLoginDialog() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater().inflate(R.layout.activity_login, null);
        dialog.setContentView(view);
        dialog.show();

        FrameLayout loginBtn = view.findViewById(R.id.loginbtn);
        EditText emailEdit = view.findViewById(R.id.email1);
        EditText passwordEdit = view.findViewById(R.id.password1);

        loginBtn.setOnClickListener(v -> {
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(screen2.this, "Email and password cannot be empty", Toast.LENGTH_LONG).show();
                return;
            }

            new Thread(() -> {
                HashMap<String, String> map = new HashMap<>();
                map.put("email", email);
                map.put("password", password);

                Call<LoginResult> call = retrofitInterface.executeLogin(map);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            LoginResult loginResult = response.body();
                            String token = loginResult.getToken();
                            saveToken(token);
                            fetchUserProfile(token);
                        } else if (response.code() == 404) {
                            runOnUiThread(() -> Toast.makeText(screen2.this, "Wrong credentials", Toast.LENGTH_LONG).show());
                        } else {
                            runOnUiThread(() -> Toast.makeText(screen2.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        runOnUiThread(() -> {
//                            dialog.dismiss();
                            Toast.makeText(screen2.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                });
            }).start();
        });

        TextView signup = view.findViewById(R.id.singup);
        signup.setOnClickListener(v -> {
            dialog.dismiss();
            handleSignupDialog();
        });
    }

    private void fetchUserProfile(String token) {
        new Thread(() -> {
            Call<ResponseData> call = retrofitInterface.getUserProfile("Bearer " + token);
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    Log.d("API_RESPONSE", response.toString());

                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("API_RESPONSE_BODY", new Gson().toJson(response.body()));
                        ResponseData responseData = response.body();
                        if (responseData.getUserDetails() == null) {
                            Log.e("API_ERROR", "userDetails is null");
                            runOnUiThread(() -> Toast.makeText(screen2.this, "User details missing", Toast.LENGTH_LONG).show());
                            return;
                        }

                        User userProfile = responseData.getUserDetails();
                        Intent intent = new Intent(screen2.this, home.class);
                        intent.putExtra("userProfile", userProfile.toString());
                        intent.putExtra("authtoken", token);

                        runOnUiThread(() -> {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null && getCurrentFocus() != null) {
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            }
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        Log.e("API_ERROR", "Response failed: " + response.errorBody());
                        runOnUiThread(() -> Toast.makeText(screen2.this, "Failed to fetch profile. Please try again.", Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    Log.e("API_FAILURE", t.getMessage());
                    runOnUiThread(() -> Toast.makeText(screen2.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
        }).start();
    }

    private void saveToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("authToken", token);
        Log.d("token in shaered prefrences = ", "token == " + token);
        editor.apply();
    }

    private void handleSignupDialog() {
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater().inflate(R.layout.activity_register, null);
        dialog1.setContentView(view);
        dialog1.show();

        EditText fullName = view.findViewById(R.id.name1);
        EditText drivingLicenseNo = view.findViewById(R.id.dl1);
        EditText contactNumber = view.findViewById(R.id.phone1);
        EditText email = view.findViewById(R.id.email1);
        EditText password = view.findViewById(R.id.password1);
        EditText confirmPassword = view.findViewById(R.id.password3);
        FrameLayout signupBtn = view.findViewById(R.id.framelayout);

        signupBtn.setOnClickListener(v -> {
            String fullNameString = fullName.getText().toString();
            String drivingLicenseNoString = drivingLicenseNo.getText().toString();
            String phoneNoString = contactNumber.getText().toString();
            String emailString = email.getText().toString();
            String passwordString = password.getText().toString();
            String confirmPasswordString = confirmPassword.getText().toString();

            if (TextUtils.isEmpty(fullNameString) || TextUtils.isEmpty(drivingLicenseNoString) ||
                    TextUtils.isEmpty(phoneNoString) || TextUtils.isEmpty(emailString) ||
                    TextUtils.isEmpty(passwordString) || TextUtils.isEmpty(confirmPasswordString)) {
                Toast.makeText(screen2.this, "All fields must be filled", Toast.LENGTH_LONG).show();
                return;
            }

            if (!passwordString.equals(confirmPasswordString)) {
                Toast.makeText(screen2.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                return;
            }

            sendOtp(emailString, fullNameString, drivingLicenseNoString, phoneNoString, passwordString, confirmPasswordString, dialog1);
        });

        TextView signin = view.findViewById(R.id.singin);
        signin.setOnClickListener(v -> {
            dialog1.dismiss();
            handleLoginDialog();
        });
    }

    private void sendOtp(String email, String fullName, String drivingLicenseNo, String contactNumber, String password, String confirmPassword, Dialog dialog) {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);

        Call<Void> call = retrofitInterface.sendOtp(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    runOnUiThread(() -> {
                        dialog.dismiss();
                        handleOtpDialog(fullName, email, contactNumber, drivingLicenseNo, password, confirmPassword);
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(screen2.this, "user already exists", Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                runOnUiThread(() -> Toast.makeText(screen2.this, t.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void handleOtpDialog(String fullName, String email, String contactNumber, String drivingLicenseNo, String password, String confirmPassword) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.activity_otp);
        dialog.show();

        FrameLayout verifyBtn = dialog.findViewById(R.id.submit);
        EditText otpEdit = dialog.findViewById(R.id.editTextAddress2);

        if (otpEdit == null || verifyBtn == null) {
            Log.e("OTP Dialog", "EditText or Button is null! Check layout ID.");
            return;
        }

        verifyBtn.setOnClickListener(v -> {
            String otp = otpEdit.getText().toString().trim();

            if (TextUtils.isEmpty(otp)) {
                Toast.makeText(screen2.this, "OTP cannot be empty", Toast.LENGTH_LONG).show();
                return;
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("otp", otp);
            map.put("email", email);

            // Uncomment this if you want to verify OTP with backend
            /*
            Call<Void> verifyOtpCall = retrofitInterface.verifyOtp(map);
            verifyOtpCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            dialog.dismiss(); // Dismiss OTP dialog after successful verification
                            registerUser(fullName, email, contactNumber, drivingLicenseNo, password, confirmPassword, otp);
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(screen2.this, "Invalid OTP", Toast.LENGTH_LONG).show());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    runOnUiThread(() -> Toast.makeText(screen2.this, t.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
            */

            // Skipping OTP verification step for now, directly registering:
            dialog.dismiss(); // Dismiss OTP dialog before proceeding
            registerUser(fullName, email, contactNumber, drivingLicenseNo, password, confirmPassword, otp);
        });
    }

    private void registerUser(String fullName, String email, String contactNumber, String drivingLicenseNo, String password, String confirmPassword, String otp) {
        Log.d("RegisterDebug", "Password: " + password);
        Log.d("RegisterDebug", "Confirm Password: " + confirmPassword);

        HashMap<String, String> map = new HashMap<>();
        map.put("fullName", fullName);
        map.put("email", email);
        map.put("contactNumber", contactNumber);
        map.put("drivingLicenseNo", drivingLicenseNo);
        map.put("password", password);
        map.put("confirmPassword", confirmPassword);
        map.put("otp", otp);
        Log.d("otp bheji hui", otp);

        new Thread(() -> {
            Call<Void> call = retrofitInterface.executableSignup(map);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            Toast.makeText(screen2.this, "Registration successful", Toast.LENGTH_LONG).show();
                            handleLoginDialog();
                        });
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                            Log.e("RegisterError", "Code: " + response.code() + " | Error: " + errorBody);
                            runOnUiThread(() -> Toast.makeText(screen2.this, "Error: " + errorBody, Toast.LENGTH_LONG).show());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("RegisterError", "Request failed: " + t.getMessage());
                    runOnUiThread(() -> Toast.makeText(screen2.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
        }).start();
    }
}
