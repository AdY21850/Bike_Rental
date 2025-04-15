package com.example.bikerentalcu;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitInterface {

    // LOGIN AND SIGNUP AUTHENTICATION:
    @POST("/api/v1/auth/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/api/v1/auth/signup")
    Call<Void> executableSignup(@Body HashMap<String, String> map);

    // OTP AUTHENTICATION:
    @POST("/api/v1/auth/sendotp")
    Call<Void> sendOtp(@Body HashMap<String, String> data); // For sending OTP

    // PROFILE OPERATIONS:
    @PUT("/api/v1/profile/update-profile")
    Call<Void> updateUserProfile(
            @Header("Authorization") String token, // Add Authorization header
            @Query("email") String email,
            @Body HashMap<String, String> updatedData
    );

    @GET("/api/v1/profile/details")
    Call<ResponseData> getUserProfile(@Header("Authorization") String token);

    // Upload profile picture
    @Multipart
    @PUT("/api/v1/profile/display-picture")
    Call<Void> uploadProfilePicture(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
            // optionally add headers manually if needed
    );
    @Multipart
    @POST("/api/v1/bike/addbikes")
    Call<Void> addBike(
            @Header("Authorization") String token,
            @Part("typeofbike") RequestBody typeofbike,
            @Part("bikemodel") RequestBody bikemodel,
            @Part("about") RequestBody about,
            @Part("registeredBikeNo") RequestBody registeredBikeNo,
            @Part("rentprice") RequestBody rentprice,
            @Part MultipartBody.Part bikepic,
            @Part("tag") RequestBody tag
    );



}
