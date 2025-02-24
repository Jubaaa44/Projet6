package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.service.PostService;
import com.openclassrooms.mddapi.service.SubjectService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.UserDetailsImpl;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin
public class PostController {

    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private PostMapper postMapper;

    @GetMapping("/feed")
    public ResponseEntity<List<PostDTO>> getFeed() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        List<Post> posts = postService.getFeedForUser(userDetails.getId());
        return ResponseEntity.ok(posts.stream()
            .map(postMapper::toDto)
            .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        
        // Mapping du DTO vers l'entité
        Post post = postMapper.toEntity(postDTO);
        
        // Association de l'auteur
        post.setAuthor(userService.getUserById(userDetails.getId()));
        
        // Association du sujet - C'EST ICI LA CORRECTION PRINCIPALE
        if (postDTO.getSubjectId() != null) {
            Subject subject = subjectService.getSubjectById(postDTO.getSubjectId());
            post.setSubject(subject);
        }
        
        // Sauvegarde du post
        Post savedPost = postService.createPost(post);
        
        // Conversion en DTO pour la réponse
        return ResponseEntity.ok(postMapper.toDto(savedPost));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPost(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(postMapper.toDto(post));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<PostDTO>> getPostsBySubject(@PathVariable Long subjectId) {
        List<Post> posts = postService.getPostsBySubject(subjectId);
        return ResponseEntity.ok(posts.stream()
            .map(postMapper::toDto)
            .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByAuthor(userId);
        return ResponseEntity.ok(posts.stream()
            .map(postMapper::toDto)
            .collect(Collectors.toList()));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostDTO postDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        Post existingPost = postService.getPostById(postId);
        if (!existingPost.getAuthor().getId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("Not authorized to update this post");
        }

        Post post = postMapper.toEntity(postDTO);
        // Conserver le sujet et l'auteur lors d'une mise à jour
        post.setAuthor(existingPost.getAuthor());
        post.setSubject(existingPost.getSubject());
        
        Post updatedPost = postService.updatePost(postId, post);
        return ResponseEntity.ok(postMapper.toDto(updatedPost));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        Post post = postService.getPostById(postId);
        if (!post.getAuthor().getId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("Not authorized to delete this post");
        }
        
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}