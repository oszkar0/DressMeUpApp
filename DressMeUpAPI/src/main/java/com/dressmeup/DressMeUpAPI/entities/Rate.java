package com.dressmeup.DressMeUpAPI.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RATES")
public class Rate {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "POSITIVE_RATE", nullable = false)
    //if true - like, if false - dislike
    private Boolean positiveRate;

    @Getter
    @Setter
    @Column(name = "COMMENT", length = 100, nullable = true)
    private String comment;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Rate() {
    }

    public Rate(Boolean positiveRate, String comment, Post post, User user) {
        this.positiveRate = positiveRate;
        this.comment = comment;
        this.post = post;
        this.user = user;
    }
}
