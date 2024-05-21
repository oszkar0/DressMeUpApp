package com.dressmeupapp.retrofit.entities;

public class RateDto {
    private Long postId;
    private Boolean positiveRate;
    private String comment;

    public RateDto(Long postId, Boolean positiveRate, String comment) {
        this.postId = postId;
        this.positiveRate = positiveRate;
        this.comment = comment;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Boolean getPositiveRate() {
        return positiveRate;
    }

    public void setPositiveRate(Boolean positiveRate) {
        this.positiveRate = positiveRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
