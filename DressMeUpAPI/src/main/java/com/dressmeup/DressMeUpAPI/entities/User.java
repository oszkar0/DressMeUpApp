package com.dressmeup.DressMeUpAPI.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Setter
    @Getter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @Setter
    @Getter
    @Column(name = "PROFILE_PICTURE")
    private byte[] profilePicture;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_ROLES",
            joinColumns = @JoinColumn(name = "USERS_id"),
            inverseJoinColumns = @JoinColumn(name = "ROLES_id")
    )
    private Collection<Role> roles = new ArrayList<>();
}
