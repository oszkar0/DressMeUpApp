package com.dressmeup.DressMeUpAPI.services;

import com.dressmeup.DressMeUpAPI.entities.Rate;
import com.dressmeup.DressMeUpAPI.entities.RateDto;
import com.dressmeup.DressMeUpAPI.entities.User;
import com.dressmeup.DressMeUpAPI.entities.Post;
import com.dressmeup.DressMeUpAPI.repositories.PostRepository;
import com.dressmeup.DressMeUpAPI.repositories.RateRepository;
import com.dressmeup.DressMeUpAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RateService {
    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public void createRate(RateDto rateDto, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(rateDto.postId());

        if(user.isPresent() && post.isPresent()){
            Rate rate = new Rate(rateDto.positiveRate(), rateDto.comment(), post.get(), user.get());
            rateRepository.save(rate);
        }
    }

    public List<Rate> getRatesByUserIdAndPostId(Long userId, Long postId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);

        if(user.isPresent() && post.isPresent()){
            return rateRepository.findByUserIdAndPostId(user.get().getId(), post.get().getId());
        }

        return null;
    }

    public boolean rateExists(Long userId, Long postId){
        List<Rate> rates = getRatesByUserIdAndPostId(userId, postId);
        return !(rates == null || rates.isEmpty());
    }

    public Rate getRateById(Long id){
        Optional<Rate> rate = rateRepository.findById(id);

        if(rate.isPresent()) {
            return rate.get();
        }

        return null;
    }

    public void deleteRateById(Long id){
        rateRepository.deleteById(id);
    }
}
