package com.dressmeupapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dressmeupapp.retrofit.entities.RegisterDto;
import com.dressmeupapp.retrofit.entities.Status;
import com.dressmeupapp.retrofit.interfaces.ApiService;
import com.dressmeupapp.retrofit.interfaces.RetrofitClient;
import com.dressmeupapp.retrofit.urls.Urls;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPage extends AppCompatActivity {
    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirmPassword);
        signupButton = findViewById(R.id.signupButton);

        setupTextWatchers();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    registerUser();
                }
            }
        });

        signupButton.setEnabled(false);
    }

    private void setupTextWatchers() {
        TextWatcher formTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signupButton.setEnabled(validateForm());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        usernameEditText.addTextChangedListener(formTextWatcher);
        emailEditText.addTextChangedListener(formTextWatcher);
        passwordEditText.addTextChangedListener(formTextWatcher);
        confirmPasswordEditText.addTextChangedListener(formTextWatcher);
    }

    public void goToWelcomePage() {
        Intent intent = new Intent(this, WelcomePage.class);
        startActivity(intent);
        finish();
    }

    private boolean validateForm() {
        String email = emailEditText.getText().toString().trim();
        return !usernameEditText.getText().toString().trim().isEmpty() &&
                !email.isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !passwordEditText.getText().toString().trim().isEmpty() &&
                !confirmPasswordEditText.getText().toString().trim().isEmpty();
    }


    private void registerUser() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!password.equals(confirmPassword)) {
            showAlertDialog("Error", "Passwords do not match.");
            return;
        }

        RegisterDto registerDto = new RegisterDto(username, password, email);
        ApiService api = RetrofitClient.getClient(this).create(ApiService.class);
        api.registerUser(registerDto).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("SUCCESS".equals(response.body().getStatus())) {
                        showAlertDialog("Success", "Registration successful!", true);
                    } else {
                        showAlertDialog("Failure", "This email is already in use.");
                    }
                } else {
                    showAlertDialog("Failure", "Registration failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                showAlertDialog("Error", "An error occurred: " + t.getMessage());
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        showAlertDialog(title, message, false);
    }

    private void showAlertDialog(String title, String message, boolean goToWelcomePage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpPage.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            if (goToWelcomePage) {
                goToWelcomePage();
            }
        });
        builder.show();
    }
}