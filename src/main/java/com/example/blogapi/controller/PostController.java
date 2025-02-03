package com.example.secureblog.controller;

import com.example.secureblog.model.Post;
import com.example.secureblog.model.User;
import com.example.secureblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.getPostById(id, userDetails));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Post createPost(@Valid @RequestBody Post post, @AuthenticationPrincipal User user) {
        return postService.createPost(post.getTitle(), post.getContent(), user.getId());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody Post post,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.updatePost(id, post, userDetails));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal User user) {
        postService.deletePost(id, user.getId());
        return ResponseEntity.ok().build();
    }
}
