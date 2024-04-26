package com.dressmeup.DressMeUpAPI.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dressmeup.DressMeUpAPI.entities.User;
import com.dressmeup.DressMeUpAPI.repositories.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            User user = new User();
            user.setEmail("example@example.com");
            user.setNickname("exampleUser");
            String encodedPassword = passwordEncoder.encode("password123");
            user.setPassword(encodedPassword);
            userRepository.save(user);
        };
    }
}