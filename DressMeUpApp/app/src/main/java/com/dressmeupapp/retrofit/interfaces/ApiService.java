package com.dressmeupapp.retrofit.interfaces;

import com.dressmeupapp.retrofit.entities.LoginDto;
import com.dressmeupapp.retrofit.entities.RegisterDto;
import com.dressmeupapp.retrofit.entities.Status;
import com.dressmeupapp.retrofit.entities.Token;
import com.dressmeupapp.retrofit.urls.Urls;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {
    @POST(Urls.REGISTER)
    Call<Status> registerUser(@Body RegisterDto registerDto);

    @POST(Urls.LOGIN)
    Call<Token> loginUser(@Body LoginDto loginDto);

}
