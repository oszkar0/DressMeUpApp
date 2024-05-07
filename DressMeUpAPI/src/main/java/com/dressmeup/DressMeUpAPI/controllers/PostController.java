package com.dressmeup.DressMeUpAPI.controllers;

import com.dressmeup.DressMeUpAPI.entities.PostDto;
import com.dressmeup.DressMeUpAPI.entities.User;
import com.dressmeup.DressMeUpAPI.services.PostService;
import com.dressmeup.DressMeUpAPI.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;
    @PostMapping
    public void CreatePost(@RequestBody PostDto dto)
    {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        postService.createPost(dto, user.getId());
    }
}
