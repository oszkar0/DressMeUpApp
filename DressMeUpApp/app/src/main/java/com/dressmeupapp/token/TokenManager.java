package com.dressmeupapp.token;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    public static String getRefreshToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("refresh_token", null);
    }

    public static String getAccessToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("refresh_token", null);
    }

    public static void saveAccessToken(String token, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("access_token", token);
        editor.apply();
    }

    public static void saveRefreshToken(String token, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("refresh_token", token);
        editor.apply();
    }
}
