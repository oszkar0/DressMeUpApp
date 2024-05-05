package com.dressmeup.DressMeUpAPI.services;

import com.dressmeup.DressMeUpAPI.entities.User;
import com.dressmeup.DressMeUpAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getByNickname(String nickname)
    {
        return userRepository.findByNickname(nickname);
    }

    public User save(User user)
    {
        return userRepository.save(user);
    }
}
