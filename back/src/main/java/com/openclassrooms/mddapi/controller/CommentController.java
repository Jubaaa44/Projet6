package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin 
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    // Ajouter un commentaire à un article
    @PostMapping("/post/{postId}")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody Comment comment) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        comment.setAuthor(userService.getUserById(userDetails.getId()));
        return ResponseEntity.ok(commentService.createComment(postId, comment));
    }

    // Récupérer les commentaires d'un article
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    // Modifier un commentaire
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody Comment commentDetails) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        // Vérifier que l'utilisateur est l'auteur
        Comment existingComment = commentService.getCommentById(commentId);
        if (!existingComment.getAuthor().getId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("Not authorized to update this comment");
        }
        return ResponseEntity.ok(commentService.updateComment(commentId, commentDetails));
    }

    // Supprimer un commentaire
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        // Vérifier que l'utilisateur est l'auteur
        Comment comment = commentService.getCommentById(commentId);
        if (!comment.getAuthor().getId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("Not authorized to delete this comment");
        }
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}