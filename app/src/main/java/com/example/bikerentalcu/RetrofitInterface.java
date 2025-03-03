package com.example.bikerentalcu;

import java.util.HashMap;

import okhttp3.MultipartBody;
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
    @POST("/api/v1/profile/update-profile")
    Call<Void> updateUserProfile(
            @Header("Authorization") String token, // Add Authorization header
            @Query("email") String email,
            @Body HashMap<String, String> updatedData
    );

//    @GET("/api/v1/profile/details")
//    Call<ResponseData> getUserDetails(
//            @Header("Authorization") String token // Add Authorization header
////            @Query("email") String email
//    );
    @GET("/api/v1/profile/details")
    Call<ResponseData> getUserProfile( @Header("Authorization") String token);

//    @PUT("/user/profile") // Change this path as necessary
//    Call<Void> updateUserProfile(@Body User user);

    // Upload profile picture
    @Multipart
    @POST("/api/v1/profile/display-picture")
    Call<Void> uploadProfilePicture(
            @Header("Authorization") String token, // Add Authorization header
            @Part MultipartBody.Part image
    );
}
