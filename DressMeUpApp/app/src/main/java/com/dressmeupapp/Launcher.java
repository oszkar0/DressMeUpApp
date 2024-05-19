package com.dressmeupapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dressmeupapp.token.TokenManager;

public class Launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TokenManager.initialize(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_launcher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String refreshToken = TokenManager.getInstance().getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            Intent intent = new Intent(this, WelcomePage.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainPage.class);
            startActivity(intent);
        }
    }
}