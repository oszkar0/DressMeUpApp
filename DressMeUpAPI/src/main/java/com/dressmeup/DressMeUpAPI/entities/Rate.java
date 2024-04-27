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
}
