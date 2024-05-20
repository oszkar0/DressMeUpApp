package com.dressmeupapp;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dressmeupapp.retrofit.entities.GeoapifyResponse;
import com.dressmeupapp.retrofit.entities.PostDto;
import com.dressmeupapp.retrofit.entities.Status;
import com.dressmeupapp.retrofit.interfaces.ApiService;
import com.dressmeupapp.retrofit.interfaces.GeoapifyApi;
import com.dressmeupapp.retrofit.interfaces.RetrofitClient;
import com.dressmeupapp.retrofit.interfaces.RetrofitLoginClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddPostActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.geoapify.com/";
    private static final String API_KEY = "6af58c41d3a94226ab05085ddce90cf2";
    private LocationManager locationManager;
    private Location location;
    private GeoapifyApi geoapifyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geoapifyApi = retrofit.create(GeoapifyApi.class);

        location = getLastKnownLocation();
        getLocationDetails(location.getLatitude(), location.getLongitude());
        Button addPostButton = findViewById(R.id.add_post_button);
        addPostButton.setEnabled(false);
        setupTextWatchers();
    }

    private Location getLastKnownLocation() {
        Location warsawLocation = new Location("Warsaw");
        warsawLocation.setLatitude(52.2297);
        warsawLocation.setLongitude(21.0122);

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return warsawLocation;
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation == null ? warsawLocation : bestLocation;
    }

    public void goToMainPage(View view){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
        finish();
    }
    public void confirmPost(View view){
        EditText text = findViewById(R.id.edit_text);
        PostDto dto = new PostDto(text.getText().toString(), location.getLatitude(), location.getLongitude(), null, 0);

        ApiService api = RetrofitClient.getClient(this).create(ApiService.class);
        api.createPost(dto).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("SUCCESS".equals(response.body().getStatus())) {
                                goToMainPage(view);
                    } else {
                        showAlertDialog(getResources().getString(R.string.signup_info_failure),
                                getResources().getString(R.string.create_post_failure_details));
                    }
                } else {
                    showAlertDialog(getResources().getString(R.string.signup_info_failure), getResources().getString(R.string.create_post_failure_details));
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                showAlertDialog("Error", "An error occurred: " + t.getMessage());
            }
        });
    }

    private void checkInputs() {
        String text = ((EditText)findViewById(R.id.edit_text)).getText().toString().trim();
        Button addPostButton = findViewById(R.id.add_post_button);
        addPostButton.setEnabled(!text.isEmpty());
    }

    private void setupTextWatchers() {
        TextWatcher loginTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        ((EditText)findViewById(R.id.edit_text)).addTextChangedListener(loginTextWatcher);
    }
    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddPostActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void getLocationDetails(double latitude, double longitude) {
        Call<GeoapifyResponse> call = geoapifyApi.getLocationDetails(latitude, longitude, API_KEY);

        call.enqueue(new Callback<GeoapifyResponse>() {
            @Override
            public void onResponse(Call<GeoapifyResponse> call, Response<GeoapifyResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GeoapifyResponse geoapifyResponse = response.body();
                    if (!geoapifyResponse.getFeatures().isEmpty()) {
                        GeoapifyResponse.Properties properties = geoapifyResponse.getFeatures().get(0).getProperties();
                        String city = properties.getCity();
                        String country = properties.getCountry();
                        TextView text = findViewById(R.id.locationText);
                        text.setText((city == null ? "" : (city + ", ")) + country);
                    }
                }
            }

            @Override
            public void onFailure(Call<GeoapifyResponse> call, Throwable t) {
            }
        });
    }
}