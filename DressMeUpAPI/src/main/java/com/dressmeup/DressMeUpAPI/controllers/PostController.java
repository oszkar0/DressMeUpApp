package com.dressmeup.DressMeUpAPI.controllers;

import com.dressmeup.DressMeUpAPI.entities.Post;
import com.dressmeup.DressMeUpAPI.entities.PostDto;
import com.dressmeup.DressMeUpAPI.entities.User;
import com.dressmeup.DressMeUpAPI.services.PostService;
import com.dressmeup.DressMeUpAPI.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping
    public void createPost(@RequestBody PostDto dto) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        postService.createPost(dto, user.getId());
    }

    @PutMapping
    public void updatePost(@RequestBody PostDto dto){
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postService.getPostById(dto.id());

        if(!user.getId().equals(post.getUser().getId())) {
            throw new AccessDeniedException("Unauthorized access");
        }

        postService.updatePost(dto, user);
    }

    @DeleteMapping
    public void deletePost(@RequestParam("id") String postId){
        Long id = Long.parseLong(postId);
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postService.getPostById(id);


        if(!user.getId().equals(post.getUser().getId())) {
            throw new AccessDeniedException("Unauthorized access");
        }

        postService.deletePost(id);
    }
}
