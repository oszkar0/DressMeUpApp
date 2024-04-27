package com.dressmeup.DressMeUpAPI.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "POSTS")
public class Post {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "TEXT", length = 200)
    private String text;

    @Getter
    @Setter
    @Column(name = "DATE")
    private Date date;

    @Getter
    @Setter
    @Column(name = "LATITUDE")
    private Double latitude;

    @Getter
    @Setter
    @Column(name = "LONGITUDE")
    private Double longitude;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Getter
    @Setter
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Rate> rates;
}
