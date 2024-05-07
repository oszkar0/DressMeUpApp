package com.dressmeup.DressMeUpAPI.services;

import com.dressmeup.DressMeUpAPI.entities.Post;
import com.dressmeup.DressMeUpAPI.entities.PostDto;
import com.dressmeup.DressMeUpAPI.entities.User;
import com.dressmeup.DressMeUpAPI.repositories.PostRepository;
import com.dressmeup.DressMeUpAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Post createPost(PostDto request, Long userId)
    {
        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            Post post = new Post(request.text(), convertToDate(LocalDateTime.now()), request.latitude(), request.longitude(), request.picture(), user.get());

            postRepository.save(post);

            return post;
        }

        return null;
    }

    public Post updatePost(PostDto request, User user)
    {
        Optional<Post> findPost = postRepository.findById(request.id());

        if(findPost.isPresent()) {
            Post post = findPost.get();

            post.setText(request.text());
            post.setPostPicture(request.picture());
            postRepository.save(post);
            return post;
        }
        return null;
    }

    public void deletePost(Long id)
    {
        Optional<Post> findPost = postRepository.findById(id);

        if(findPost.isPresent()) {
            Post post = findPost.get();

            postRepository.delete(post);
        }
    }

    public List<Post> getPosts(double longitude, double latitude, double radius)
    {
        return null;
    }

    public List<Post> getPosts(Long userId)
    {
        return postRepository.findByUserId(userId);
    }

    public Date convertToDate(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }
}
