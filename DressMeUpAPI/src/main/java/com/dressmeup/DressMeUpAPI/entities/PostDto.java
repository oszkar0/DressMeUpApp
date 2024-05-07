package com.dressmeup.DressMeUpAPI.entities;

public record PostDto(String text, Double latitude, Double longitude, byte[] picture, long id) {
}
