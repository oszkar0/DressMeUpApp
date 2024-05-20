package com.dressmeupapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dressmeupapp.retrofit.entities.Post;
import com.dressmeupapp.retrofit.entities.Status;
import com.dressmeupapp.retrofit.entities.UpdateUserRequest;
import com.dressmeupapp.retrofit.entities.UpdateUserResponse;
import com.dressmeupapp.retrofit.entities.UserResponse;
import com.dressmeupapp.retrofit.interfaces.ApiService;
import com.dressmeupapp.retrofit.interfaces.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private ImageView profilePicture;
    private TextView username;
    private Button editPasswordButton;
    private LinearLayout passwordEditLayout;
    private EditText oldPassword;
    private EditText newPassword;
    private Button acceptPasswordChange;
    private RecyclerView postsRecyclerView;
    private List<Post> posts;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.username);
        editPasswordButton = findViewById(R.id.edit_password_button);
        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        postsRecyclerView = findViewById(R.id.posts_recyclerview);
        editPasswordButton.setEnabled(false);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((Toolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainPage(v);
            }
        });

        setupTextWatchers();

        // Fetch user data
        fetchUserData();

        // Set up edit password functionality
        editPasswordButton.setOnClickListener(view -> {
            String oldPass = oldPassword.getText().toString();
            String newPass = newPassword.getText().toString();
            if (!oldPass.isEmpty() && !newPass.isEmpty()) {
                changePassword(oldPass, newPass);
            }
        });
    }

    private void fetchUserData() {
        RetrofitClient.getClient(this).create(ApiService.class).getCurrentUser().enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();
                    username.setText(user.getUsername());
                    posts = user.getPosts();
                    setupPostsRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
            }
        });
    }

    private void setupPostsRecyclerView() {
        UsersPostsAdapter adapter = new UsersPostsAdapter(posts, this::deletePost);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsRecyclerView.setAdapter(adapter);
    }

    private void deletePost(Post post) {
        RetrofitClient.getClient(this).create(ApiService.class)
                .deletePost(post.getPostId().toString()).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                fetchUserData();
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
            }
        });
    }

    private void changePassword(String oldPass, String newPass) {
        RetrofitClient.getClient(this).create(ApiService.class)
                .updateUser(new UpdateUserRequest(oldPass, newPass))
                .enqueue(new Callback<UpdateUserResponse>() {
                    @Override
                    public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                        UpdateUserResponse updateUserResponse = response.body();
                        if(updateUserResponse.getErrorMessage() == null) {
                            showAlertDialog(getResources().getString(R.string.signup_info_success),
                                    getResources().getString(R.string.password_change_success));
                        }
                        else if(updateUserResponse.getErrorMessage().equals("wrong password")){
                            showAlertDialog(getResources().getString(R.string.signup_info_failure),
                                    getResources().getString(R.string.password_change_fail));
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateUserResponse> call, Throwable t) {
                        showAlertDialog(getResources().getString(R.string.signup_info_failure),
                                getResources().getString(R.string.password_change_fail));
                    }
                });
    }

    public void goToMainPage(View view){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
        finish();
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
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

        oldPassword.addTextChangedListener(loginTextWatcher);
        newPassword.addTextChangedListener(loginTextWatcher);
    }


    private void checkInputs() {
        String oldPass = oldPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();
        editPasswordButton.setEnabled(!oldPass.isEmpty() && !newPass.isEmpty());
    }
}
