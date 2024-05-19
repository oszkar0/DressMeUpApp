package com.dressmeupapp.retrofit.entities;

import java.util.List;

public class UserResponse {
    private String username;
    private byte[] profilePicture;
    private List<Post> posts;

    // Gettery i Settery
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
