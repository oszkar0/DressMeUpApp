package com.dressmeup.DressMeUpAPI.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ROLES")
public class Role {
    public static final String ROLE_USER = "ROLE_USER";

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "ROLE_NAME")
    private String name;

    public Role(String name)
    {
        this.name = name;
    }


}
