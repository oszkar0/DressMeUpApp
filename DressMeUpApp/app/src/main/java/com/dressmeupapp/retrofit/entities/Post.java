package com.dressmeupapp.retrofit.entities;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Post {
    private Long postId;
    private Long userId;
    private String username;
    private byte[] userProfilePicture;
    private String text;
    private byte[] postPicture;
    private double longitude;
    private double latitude;
    private Date date;

    public Post(Long postId, Long userId, String username, byte[] userProfilePicture,
                        String text, byte[] postPicture, double longitude, double latitude, Date date) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.userProfilePicture = userProfilePicture;
        this.text = text;
        this.postPicture = postPicture;
        this.longitude = longitude;
        this.latitude = latitude;
        this.date = date;
    }

    public Post() {
    }

    public Post(Long postId, String username, String text) {
        this.postId = postId;
        this.username = username;
        this.text = text;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getUserProfilePicture() {
        return userProfilePicture;
    }

    public void setUserProfilePicture(byte[] userProfilePicture) {
        this.userProfilePicture = userProfilePicture;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public byte[] getPostPicture() {
        return postPicture;
    }

    public void setPostPicture(byte[] postPicture) {
        this.postPicture = postPicture;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

