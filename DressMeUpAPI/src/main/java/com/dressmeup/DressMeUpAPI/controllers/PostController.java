package com.dressmeup.DressMeUpAPI.controllers;

import com.dressmeup.DressMeUpAPI.entities.*;
import com.dressmeup.DressMeUpAPI.repositories.RateRepository;
import com.dressmeup.DressMeUpAPI.services.PostService;
import com.dressmeup.DressMeUpAPI.services.RateService;
import com.dressmeup.DressMeUpAPI.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private RateService rateService;

    @PostMapping
    public ResponseEntity<Status> createPost(@RequestBody PostDto dto) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        postService.createPost(dto, user.getId());
        return ResponseEntity.ok().body(Status.success());
    }

    @PutMapping
    public ResponseEntity<Status> updatePost(@RequestBody PostDto dto){
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postService.getPostById(dto.id());

        if(!user.getId().equals(post.getUser().getId())) {
            throw new AccessDeniedException("Unauthorized access");
        }

        postService.updatePost(dto, user);
        return ResponseEntity.ok().body(Status.success());
    }

    @GetMapping
    public ResponseEntity<PostResponse> getPost(@RequestParam("postId") String postId) {
        Post post = postService.getPostById(Long.parseLong(postId));

        if(post == null) {
            return ResponseEntity.badRequest().body(null);
        }

        PostResponse postResponse = new PostResponse(post.getId(), post.getUser().getId(), post.getUser().getUsername(),
                post.getUser().getProfilePicture(), post.getText(), post.getPostPicture(), post.getLongitude(), post.getLatitude(),
                post.getDate());

        return ResponseEntity.ok().body(postResponse);
    }

    @GetMapping("/inRadius")
    public ResponseEntity<List<PostResponse>> getPostsByRadius(@RequestParam("latitude") String latitude,
                                                               @RequestParam("longitude") String longitude,
                                                               @RequestParam("radius") String radius)
    {
        Double lat = Double.parseDouble(latitude);
        Double lon = Double.parseDouble(longitude);
        Double rad = Double.parseDouble(radius);
        var posts = postService.getPosts(lon, lat, rad).stream()
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .map(post -> new PostResponse(post.getId(), null, post.getUser().getUsername(),null,
                post.getText(), null, post.getLongitude(), post.getLatitude(),
                post.getDate())).toList();
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PostResponse>> getPostsByUserId(@RequestParam("userId") String userId)
    {
        var posts = postService.getPosts(Long.parseLong(userId)).stream()
                .sorted(Comparator.comparing(Post::getDate).reversed())
                .map(post -> new PostResponse(post.getId(), post.getUser().getId(), post.getUser().getUsername(),
                post.getUser().getProfilePicture(), post.getText(), post.getPostPicture(), post.getLongitude(), post.getLatitude(),
                post.getDate())).toList();
        return ResponseEntity.ok().body(posts);
    }

    @DeleteMapping
    public ResponseEntity<Status> deletePost(@RequestParam("id") String postId){
        Long id = Long.parseLong(postId);
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Post post = postService.getPostById(id);


        if(!user.getId().equals(post.getUser().getId())) {
            throw new AccessDeniedException("Unauthorized access");
        }

        postService.deletePost(id);
        return ResponseEntity.ok().body(Status.success());
    }

    @PostMapping("/rates")
    public ResponseEntity<Status> createRate(@RequestBody RateDto rateDto){
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(rateService.rateExists(user.getId(), rateDto.postId())) {
            return ResponseEntity.badRequest().body(Status.failure());
        }

        rateService.createRate(rateDto, user.getId());

        return ResponseEntity.ok().body(Status.success());
    }


    @GetMapping("/rates/rate-exists")
    public ResponseEntity<RateExistsResponse> checkRateExists(@RequestParam("postId") String postId) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Boolean rateExists = rateService.rateExists(user.getId(), Long.parseLong(postId));

        return ResponseEntity.ok(new RateExistsResponse(rateExists));
    }

    @DeleteMapping("/rates")
    public ResponseEntity<Status> deleteRate(@RequestParam("rateId") String rateId) {
        User user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Long rateIdLong = Long.parseLong(rateId);
        Rate rate = rateService.getRateById(rateIdLong);

        if(rate == null || !user.getId().equals(rate.getUser().getId())) {
            throw new AccessDeniedException("Access denied");
        }

        rateService.deleteRateById(rateIdLong);
        return ResponseEntity.ok().body(Status.success());
    }

    @GetMapping("/rates")
    public ResponseEntity<List<RateResponse>> getPostRates(@RequestParam("postId") String postId) {
        Post post = postService.getPostById(Long.parseLong(postId));

        if(post == null) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Rate> rates = rateService.getRatesByPostId(post.getId());

        List<RateResponse> rateResponses =
                rates
                        .stream()
                        .map(r -> {return new RateResponse(r.getId(), r.getPositiveRate(), r.getComment(), r.getUser().getId(), r.getUser().getUsername());})
                        .toList();


        return ResponseEntity.ok().body(rateResponses);
    }

    public record RateExistsResponse(Boolean rateExists){}
    public record RateResponse(Long rateId, Boolean positive, String comment, Long userId, String username){}
    public record PostResponse(Long postId, Long userId, String username, byte[] userProfilePicture, String text,
                               byte[] postPicture, double longitude, double latitude, Date date){}
}
