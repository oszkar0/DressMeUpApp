package com.dressmeupapp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dressmeupapp.R;
import com.dressmeupapp.retrofit.entities.Post;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostHolder> {

    private List<Post> data;

    public static class PostHolder extends RecyclerView.ViewHolder {
        public TextView itemId;
        public TextView username;
        public TextView comment;

        public PostHolder(View itemView) {
            super(itemView);
            itemId = itemView.findViewById(R.id.item_id);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    public PostListAdapter(List<Post> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posts_list_view_item, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post item = data.get(position);
        holder.itemId.setText(String.valueOf(item.getPostId()));
        holder.username.setText(item.getUsername());
        holder.comment.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Post> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }
}
