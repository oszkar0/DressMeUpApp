package com.dressmeupapp.retrofit.interfaces;

import com.dressmeupapp.retrofit.entities.LoginDto;
import com.dressmeupapp.retrofit.entities.Post;
import com.dressmeupapp.retrofit.entities.RefreshResponse;
import com.dressmeupapp.retrofit.entities.RegisterDto;
import com.dressmeupapp.retrofit.entities.Status;
import com.dressmeupapp.retrofit.entities.Token;
import com.dressmeupapp.retrofit.entities.UserResponse;
import com.dressmeupapp.retrofit.urls.Urls;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST(Urls.REGISTER)
    Call<Status> registerUser(@Body RegisterDto registerDto);

    @POST(Urls.LOGIN)
    Call<Token> loginUser(@Body LoginDto loginDto);

    @POST(Urls.REFRESH)
    Call<RefreshResponse> refreshToken(@Header("Authorization") String refreshToken);

    @GET(Urls.RADIUS)
    Call<List<Post>> getPosts(
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            @Query("radius") String radius
    );

    @GET(Urls.CURRENT_USER)
    Call<UserResponse> getCurrentUser();
}
