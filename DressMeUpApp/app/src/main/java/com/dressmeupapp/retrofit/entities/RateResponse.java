package com.dressmeupapp.retrofit.entities;

public class RateResponse {
    private Long rateId;
    private Boolean positive;
    private String comment;
    private Long userId;
    private String username;

    public RateResponse(Long rateId, Boolean positive, String comment, Long userId, String username) {
        this.rateId = rateId;
        this.positive = positive;
        this.comment = comment;
        this.userId = userId;
        this.username = username;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    public Boolean getPositive() {
        return positive;
    }

    public void setPositive(Boolean positive) {
        this.positive = positive;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
