package com.dressmeup.DressMeUpAPI.controllers;

import com.dressmeup.DressMeUpAPI.entities.Post;
import com.dressmeup.DressMeUpAPI.entities.User;
import com.dressmeup.DressMeUpAPI.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok().body("OK!!");
    }

    @GetMapping()
    public ResponseEntity<UserResponse> getByUsername(@RequestParam("username") String username)
    {
        var user = userService.getByUsername(username);

        List<Post> posts = user.getPosts();

        var postResponse = posts.stream().map(post -> new PostController.PostResponse(post.getId(), post.getUser().getId(), post.getUser().getUsername(),
                post.getUser().getProfilePicture(), post.getText(), post.getPostPicture(), post.getLongitude(), post.getLatitude(),
                post.getDate())).toList();
        var userResponse = new UserResponse(user.getId(), user.getUsername(), user.getProfilePicture(), postResponse);

        return ResponseEntity.ok().body(userResponse);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<UserResponse> getCurrentUser()
    {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Post> posts = user.getPosts();

        var postResponse = posts.stream()
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .map(post -> new PostController.PostResponse(post.getId(), post.getUser().getId(), post.getUser().getUsername(),
                post.getUser().getProfilePicture(), post.getText(), post.getPostPicture(), post.getLongitude(), post.getLatitude(),
                post.getDate())).toList();
        var userResponse = new UserResponse(user.getId(), user.getUsername(), user.getProfilePicture(), postResponse);

        return ResponseEntity.ok().body(userResponse);
    }

    @PutMapping()
    public ResponseEntity<UpdateUserResponse> updateUser(@RequestBody updateUserRequest data)
    {
        if(data.profilePicture() == null && data.oldPassword() == null && data.newPassword() == null)
        {
            return ResponseEntity.badRequest().body(new UpdateUserResponse(false, "empty request"));
        }

        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(data.profilePicture() != null) {
            user.setProfilePicture(data.profilePicture());
            userService.save(user);
            return ResponseEntity.ok().body(new UpdateUserResponse(true, null));
        }
        if(data.oldPassword() != null && data.newPassword() != null && encoder.matches(data.oldPassword(), user.getPassword()))
        {
            user.setPassword(encoder.encode(data.newPassword()));
            userService.save(user);
            return ResponseEntity.ok().body(new UpdateUserResponse(true, null));
        }

        return ResponseEntity.badRequest().body(new UpdateUserResponse(false, "wrong password"));
    }
    public record UpdateUserResponse(Boolean result, String ErrorMessage) {}
    public record UserResponse(Long userId, String username, byte[] profilePicture, List<PostController.PostResponse> posts) { }
    public record updateUserRequest(byte[] profilePicture, String oldPassword, String newPassword) {}
}
