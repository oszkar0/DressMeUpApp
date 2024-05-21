package com.dressmeupapp.retrofit.interfaces;

import com.dressmeupapp.retrofit.entities.LoginDto;
import com.dressmeupapp.retrofit.entities.Post;
import com.dressmeupapp.retrofit.entities.PostDto;
import com.dressmeupapp.retrofit.entities.RateDto;
import com.dressmeupapp.retrofit.entities.RateExistsResponse;
import com.dressmeupapp.retrofit.entities.RateResponse;
import com.dressmeupapp.retrofit.entities.RefreshResponse;
import com.dressmeupapp.retrofit.entities.RegisterDto;
import com.dressmeupapp.retrofit.entities.Status;
import com.dressmeupapp.retrofit.entities.Token;
import com.dressmeupapp.retrofit.entities.UpdateUserRequest;
import com.dressmeupapp.retrofit.entities.UpdateUserResponse;
import com.dressmeupapp.retrofit.entities.UserResponse;
import com.dressmeupapp.retrofit.urls.Urls;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @POST(Urls.CREATE_POST)
    Call<Status> createPost(@Body PostDto dto);

    @GET(Urls.CURRENT_USER)
    Call<UserResponse> getCurrentUser();

    @DELETE(Urls.DELETE_POST)
    Call<Status> deletePost(@Query("id") String id);

    @PUT(Urls.UPDATE_USER)
    Call<UpdateUserResponse> updateUser(@Body UpdateUserRequest updateUserRequest);

    @GET(Urls.GET_POST)
    Call<Post> getPostById(@Query("postId") String id);

    @GET(Urls.GET_POST_COMMENTS)
    Call<List<RateResponse>> getPostRates(@Query("postId") String postId);

    @GET(Urls.RATE_EXISTS)
    Call<RateExistsResponse> rateExists(@Query("postId") String postId);

    @POST(Urls.CREATE_RATE)
    Call<Status> createRate(@Body RateDto rateDto);

    @DELETE(Urls.DELETE_RATE)
    Call<Status> deleteRate(@Query("rateId") String rateId);
}
