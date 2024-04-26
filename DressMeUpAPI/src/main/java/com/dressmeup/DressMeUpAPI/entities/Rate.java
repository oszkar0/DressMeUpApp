package com.dressmeup.DressMeUpAPI.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Rate {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "LIKE", nullable = false)
    //if true - like, if false - dislike
    private Boolean like;

    @Getter
    @Setter
    @Column(name = "COMMENT", length = 100, nullable = true)
    private String comment;

    @Getter
    @Setter
    @ManyToOne
    private Post post;
}