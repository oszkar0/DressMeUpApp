package com.dressmeupapp.retrofit.interfaces;

import com.dressmeupapp.retrofit.entities.RefreshResponse;
import com.dressmeupapp.token.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class AuthInterceptor implements Interceptor {
    private ApiService apiService;

    public AuthInterceptor(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = TokenManager.getInstance().getAccessToken();

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        Response response = chain.proceed(request);

        if (response.code() == 401) {
            response.close();
            String refreshToken = TokenManager.getInstance().getRefreshToken();
            Call<RefreshResponse> refreshCall = apiService.refreshToken("Bearer " + refreshToken);
            retrofit2.Response<RefreshResponse> refreshResponse = refreshCall.execute();

            if (refreshResponse.isSuccessful()) {
                RefreshResponse newToken = refreshResponse.body();
                TokenManager.getInstance().saveAccessToken(newToken.getAccess_token());

                return chain.proceed(
                        request.newBuilder()
                                .header("Authorization", "Bearer " + newToken.getAccess_token())
                                .build()
                );
            } else {
                return response; //TODO: logout
            }
        }
        return response;
    }
}