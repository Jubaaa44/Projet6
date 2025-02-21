package com.openclassrooms.mddapi.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.service.PostService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin  
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    // Ajouter cet endpoint pour récupérer tous les posts
    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        try {
            List<Post> posts = postService.getAllPosts();
            System.out.println("Posts récupérés avec succès, nombre : " + posts.size());
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            System.err.println("Erreur dans le controller : " + e.getMessage());
            return ResponseEntity.status(500).body("Erreur lors de la récupération des posts : " + e.getMessage());
        }
    }

    // Récupérer le fil d'actualité (posts des sujets suivis)
    @GetMapping("/feed")
    public ResponseEntity<?> getFeed() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        return ResponseEntity.ok(postService.getFeedForUser(userDetails.getId()));
    }

    // Créer un nouvel article
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        post.setAuthor(userService.getUserById(userDetails.getId()));
        return ResponseEntity.ok(postService.createPost(post));
    }

    // Récupérer un article spécifique
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    // Récupérer tous les articles d'un sujet
    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<?> getPostsBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(postService.getPostsBySubject(subjectId));
    }

    // Récupérer tous les articles d'un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByAuthor(userId));
    }

    // Modifier un article
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody Post postDetails) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        // Vérifier que l'utilisateur est l'auteur
        Post existingPost = postService.getPostById(postId);
        if (!existingPost.getAuthor().getId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("Not authorized to update this post");
        }
        return ResponseEntity.ok(postService.updatePost(postId, postDetails));
    }

    // Supprimer un article
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        // Vérifier que l'utilisateur est l'auteur
        Post post = postService.getPostById(postId);
        if (!post.getAuthor().getId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("Not authorized to delete this post");
        }
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}