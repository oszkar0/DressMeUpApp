package com.dressmeup.DressMeUpAPI.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="USERS")
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @Column(name = "EMAIL", length = 50)
    private String email;

    @Setter
    @Getter
    @Column(name = "NICKNAME", length = 20)
    private String nickname;

    @Setter
    @Getter
    @Column(name = "PASSWORD", length = 80) //length of BCrypt encoded password
    private String password;
}
