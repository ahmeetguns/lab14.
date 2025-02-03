package com.example.secureblog.service;

import com.example.secureblog.model.Post;
import com.example.secureblog.model.User;
import com.example.secureblog.repository.PostRepository;
import com.example.secureblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByAuthorId(userId);
    }

    public Post getPostById(Long id, UserDetails userDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!post.getAuthor().getUsername().equals(userDetails.getUsername()) && 
            !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You don't have permission to access this post");
        }
        
        return post;
    }

    @Transactional
    public Post createPost(String title, String content, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(user);
        
        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long id, Post updatedPost, UserDetails userDetails) {
        Post post = getPostById(id, userDetails);
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
                
        if (!post.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("You don't have permission to delete this post");
        }
        
        postRepository.delete(post);
    }
}
