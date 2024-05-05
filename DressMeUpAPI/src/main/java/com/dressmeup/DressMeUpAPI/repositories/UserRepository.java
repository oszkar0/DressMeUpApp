package com.dressmeup.DressMeUpAPI.repositories;

import com.dressmeup.DressMeUpAPI.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByNickname(String nickname);
}
