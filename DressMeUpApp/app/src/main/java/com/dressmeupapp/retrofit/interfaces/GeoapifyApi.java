package com.dressmeupapp.retrofit.interfaces;

import com.dressmeupapp.retrofit.entities.GeoapifyResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoapifyApi {

    @GET("v1/geocode/reverse")
    Call<GeoapifyResponse> getLocationDetails(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("apiKey") String apiKey
    );
}