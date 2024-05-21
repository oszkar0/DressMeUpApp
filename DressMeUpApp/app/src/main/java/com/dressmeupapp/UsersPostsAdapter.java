package com.dressmeupapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dressmeupapp.R;
import com.dressmeupapp.retrofit.entities.Post;

import java.util.List;

public class UsersPostsAdapter extends RecyclerView.Adapter<UsersPostsAdapter.UsersPostViewHolder> {

    private List<Post> posts;
    private PostDeleteCallback deleteCallback;
    Context context;

    public UsersPostsAdapter(List<Post> posts, PostDeleteCallback deleteCallback, Context context) {
        this.posts = posts;
        this.deleteCallback = deleteCallback;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_posts_list_view_item, parent, false);
        return new UsersPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersPostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.text.setText(post.getText());
        holder.deleteButton.setOnClickListener(v -> deleteCallback.onDelete(post));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Post clickedPost = posts.get(holder.getAdapterPosition());
                Long postId = clickedPost.getPostId();
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("post_id", postId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface PostDeleteCallback {
        void onDelete(Post post);
    }

    static class UsersPostViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageButton deleteButton;

        public UsersPostViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.post_text);
            deleteButton = itemView.findViewById(R.id.delete_post_button);
        }
    }
}
