package com.dressmeupapp.retrofit.entities;

import java.util.Arrays;
import java.util.Objects;

public class PostDto {
    private String text;
    private Double latitude;
    private Double longitude;
    private byte[] picture;
    private long id;

    // Konstruktor
    public PostDto(String text, Double latitude, Double longitude, byte[] picture, long id) {
        this.text = text;
        this.latitude = latitude;
        this.longitude = longitude;
        this.picture = picture;
        this.id = id;
    }

    // Gettery i settery
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}