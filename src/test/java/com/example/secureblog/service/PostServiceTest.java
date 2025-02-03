package com.example.secureblog.service;

import com.example.secureblog.model.Post;
import com.example.secureblog.model.User;
import com.example.secureblog.repository.PostRepository;
import com.example.secureblog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    private User testUser;
    private Post testPost;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.getRoles().add("ROLE_USER");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setContent("This is a test content.");
        testPost.setAuthor(testUser);

        userDetails = new org.springframework.security.core.userdetails.User(
            testUser.getUsername(),
            testUser.getPassword(),
            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void shouldCreatePostSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        Post savedPost = postService.createPost("Test Post", "This is a test content.", 1L);

        assertNotNull(savedPost);
        assertEquals("Test Post", savedPost.getTitle());
        assertEquals("This is a test content.", savedPost.getContent());
        assertEquals(1L, savedPost.getAuthor().getId());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void shouldGetAllPosts() {
        when(postRepository.findAll()).thenReturn(Arrays.asList(testPost));

        List<Post> posts = postService.getAllPosts();

        assertFalse(posts.isEmpty());
        assertEquals(1, posts.size());
        assertEquals("Test Post", posts.get(0).getTitle());
    }

    @Test
    void shouldGetPostsByUser() {
        when(postRepository.findByAuthorId(1L)).thenReturn(Arrays.asList(testPost));

        List<Post> posts = postService.getPostsByUser(1L);

        assertFalse(posts.isEmpty());
        assertEquals(1, posts.size());
        assertEquals("Test Post", posts.get(0).getTitle());
    }

    @Test
    void shouldGetPostByIdSuccessfully() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        Post post = postService.getPostById(1L, userDetails);

        assertNotNull(post);
        assertEquals("Test Post", post.getTitle());
        assertEquals("This is a test content.", post.getContent());
    }

    @Test
    void shouldUpdatePostSuccessfully() {
        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Title");
        updatedPost.setContent("Updated content");

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        Post result = postService.updatePost(1L, updatedPost, userDetails);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated content", result.getContent());
    }

    @Test
    void shouldDeletePostSuccessfully() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        doNothing().when(postRepository).delete(testPost);

        postService.deletePost(1L, 1L);

        verify(postRepository).delete(testPost);
    }
}
