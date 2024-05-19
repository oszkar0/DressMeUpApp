package com.dressmeupapp.token;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "app_prefs";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static TokenManager tokenManager;
    private SharedPreferences prefs;
    public static void initialize(Context context) {
        tokenManager = new TokenManager();
        tokenManager.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static TokenManager getInstance() {
        return tokenManager;
    }

    private TokenManager(){
    }

    public void saveAccessToken(String token) {
        prefs.edit().putString(ACCESS_TOKEN_KEY, token).apply();
    }

    public void saveRefreshToken(String token) {
        prefs.edit().putString(REFRESH_TOKEN_KEY, token).apply();
    }

    public String getAccessToken() {
        return prefs.getString(ACCESS_TOKEN_KEY, null);
    }

    public String getRefreshToken() {
        return prefs.getString(REFRESH_TOKEN_KEY, null);
    }
}
