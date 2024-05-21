package com.dressmeupapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dressmeupapp.retrofit.entities.Post;
import com.dressmeupapp.retrofit.entities.RateDto;
import com.dressmeupapp.retrofit.entities.RateExistsResponse;
import com.dressmeupapp.retrofit.entities.RateResponse;
import com.dressmeupapp.retrofit.entities.Status;
import com.dressmeupapp.retrofit.entities.UserResponse;
import com.dressmeupapp.retrofit.interfaces.ApiService;
import com.dressmeupapp.retrofit.interfaces.RetrofitClient;
import com.dressmeupapp.token.TokenManager;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostDetailsActivity extends AppCompatActivity {

    private TextView username;
    private TextView comment;
    private TextView date;

    private ImageButton deletePostButton;
    private Button addCommentButton;
    private RecyclerView ratesRecyclerView;
    private EditText commentText;
    private RatesAdapter ratesAdapter;
    private ImageButton thumbUp;
    private ImageButton thumbDown;
    private Boolean positive = true;
    private Long id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        deletePostButton = findViewById(R.id.post_delete_button);
        addCommentButton = findViewById(R.id.add_comment_button);
        ratesRecyclerView = findViewById(R.id.comments);
        commentText = findViewById(R.id.comment_text);
        username = findViewById(R.id.post_creator);
        comment = findViewById(R.id.post_details_text);
        date = findViewById(R.id.post_date);

        ratesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ratesAdapter = new RatesAdapter();
        ratesRecyclerView.setAdapter(ratesAdapter);

        thumbUp = findViewById(R.id.thumb_up);
        thumbDown = findViewById(R.id.thumb_down);
        thumbUp.getBackground().setAlpha(255);
        thumbDown.getBackground().setAlpha(50);

        thumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positive = true;
                thumbUp.getBackground().setAlpha(255);
                thumbDown.getBackground().setAlpha(50);
            }
        });


        thumbDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positive = false;
                thumbDown.getBackground().setAlpha(255);
                thumbUp.getBackground().setAlpha(50);
            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddCommentButtonClick();
            }
        });

        deletePostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeletePostButtonClick();
            }
        });

        Long postId = getIntent().getLongExtra("post_id", -1L);
        id = postId;
        ApiService service = RetrofitClient.getClient(this).create(ApiService.class);
        Call<Post> postCall = service.getPostById(String.valueOf(postId));

        postCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, retrofit2.Response<Post> response) {
                if (response.isSuccessful()) {
                    Post post = response.body();
                    comment.setText(post.getText());
                    username.setText(post.getUsername());

                    Instant instant = null;
                    ZoneId originalZoneId = null;
                    ZonedDateTime zonedDateTimeInOriginalZone = null;
                    ZoneId localZoneId = null;
                    ZonedDateTime zonedDateTimeInLocalZone = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        instant = post.getDate().toInstant();
                        originalZoneId = ZoneId.of("Europe/Paris");
                        zonedDateTimeInOriginalZone = instant.atZone(originalZoneId);
                        localZoneId = ZoneId.systemDefault();
                        zonedDateTimeInLocalZone = zonedDateTimeInOriginalZone.withZoneSameInstant(localZoneId);
                        LocalDateTime localDateTimeInLocalZone = zonedDateTimeInLocalZone.toLocalDateTime();
                        date.setText(localDateTimeInLocalZone.toString());
                    } else {
                        date.setText(post.getDate().toString());
                    }

                    if (post.getUserId().equals(TokenManager.getInstance().getLoggedUserId())){
                        deletePostButton.setVisibility(View.VISIBLE);
                    } else {
                        deletePostButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
            }
        });

        refreshComments();
        checkRateExists();
    }

    private void checkRateExists() {
        ApiService service = RetrofitClient.getClient(this).create(ApiService.class);
        Call<RateExistsResponse> rateExistsCall = service.rateExists(String.valueOf(id));
        rateExistsCall.enqueue(new Callback<RateExistsResponse>() {
            @Override
            public void onResponse(Call<RateExistsResponse> call, retrofit2.Response<RateExistsResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getRateExists()) {
                        commentText.setFocusable(false);
                        addCommentButton.setEnabled(false);
                    } else {
                        commentText.setFocusableInTouchMode(true);
                        addCommentButton.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<RateExistsResponse> call, Throwable t) {
            }
        });
    }

    public void onDeletePostButtonClick() {
        Retrofit retrofit = RetrofitClient.getClient(this);
        ApiService service = retrofit.create(ApiService.class);
        Call<Status> call = service.deletePost(String.valueOf(id));
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, retrofit2.Response<Status> response) {
                if (response.isSuccessful()) {
                    if(response.body().getStatus().equals("SUCCESS")) {
                        Intent intent = new Intent(PostDetailsActivity.this, MainPage.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
            }
        });
    }

    private void refreshComments(){
        ApiService service = RetrofitClient.getClient(this).create(ApiService.class);
        Call<List<RateResponse>> ratesCall = service.getPostRates(String.valueOf(id));

        ratesCall.enqueue(new Callback<List<RateResponse>>() {
            @Override
            public void onResponse(Call<List<RateResponse>> call, retrofit2.Response<List<RateResponse>> response) {
                if (response.isSuccessful()) {
                    ratesAdapter.setComments(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<RateResponse>> call, Throwable t) {
            }
        });

    }

    public void onAddCommentButtonClick() {
        String comment = commentText.getText().toString().trim();

        if (!comment.isEmpty()) {
            Retrofit retrofit = RetrofitClient.getClient(this);
            ApiService service = retrofit.create(ApiService.class);
            Call<Status> call = service.createRate(new RateDto(id, positive, comment));

            call.enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, retrofit2.Response<Status> response) {
                    if (response.isSuccessful()) {
                        commentText.setText("");
                        commentText.setActivated(false);
                        refreshComments();
                        checkRateExists();
                    }
                }

                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                }
            });
        }
    }

    private void deleteComment(Long commentId) {
        Retrofit retrofit = RetrofitClient.getClient(this);
        ApiService service = retrofit.create(ApiService.class);
        Call<Status> call = service.deleteRate(commentId.toString());

        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, retrofit2.Response<Status> response) {
                if (response.isSuccessful()) {
                    refreshComments();
                    checkRateExists();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
            }
        });
    }


    private class RatesAdapter extends RecyclerView.Adapter<RatesAdapter.RateViewHolder> {

        private List<RateResponse> rates;

        public void setComments(List<RateResponse> rates) {
            this.rates = rates;
            notifyDataSetChanged();
        }

        @Override
        public RateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_view_item, parent, false);
            return new RateViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RateViewHolder holder, int position) {
            RateResponse rate = rates.get(position);
            holder.bind(rate);
        }

        @Override
        public int getItemCount() {
            return rates != null ? rates.size() : 0;
        }

        class RateViewHolder extends RecyclerView.ViewHolder {

            TextView commentText;
            TextView username;
            TextView date;
            ImageView rateImage;
            ImageButton deleteCommentButton;

            RateViewHolder(View itemView) {
                super(itemView);
                commentText = itemView.findViewById(R.id.rate_text);
                username = itemView.findViewById(R.id.rate_username);
                rateImage = itemView.findViewById(R.id.rate_rate);
                deleteCommentButton = itemView.findViewById(R.id.delete_rate_button);
            }

            void bind(final RateResponse rate) {
                Long userId = TokenManager.getInstance().getLoggedUserId();
                commentText.setText(rate.getComment());
                username.setText(rate.getUsername());

                if(rate.getPositive()) {
                    rateImage.setImageResource(R.drawable.baseline_thumb_up);
                } else {
                    rateImage.setImageResource(R.drawable.baseline_thumb_down);
                }
                rateImage.setImageTintList(
                        ColorStateList.valueOf(getResources().getColor(com.google.android.material.R.color.design_default_color_primary)));

                if (rate.getUserId().equals(userId)) {
                    deleteCommentButton.setVisibility(View.VISIBLE);
                    deleteCommentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteComment(rate.getRateId());
                        }
                    });
                } else {
                    deleteCommentButton.setVisibility(View.GONE);
                }
            }

        }
    }
}

