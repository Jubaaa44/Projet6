package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.UserDetailsImpl;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin 
public class CommentController {

    @Autowired
    private CommentService commentService;
    
    @Autowired
    private UserService userService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        
        commentDTO.setAuthorId(userDetails.getId());
        return ResponseEntity.ok(commentService.createComment(postId, commentDTO));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO commentDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        CommentDTO existingComment = commentService.getCommentById(commentId);
        if (!existingComment.getAuthorId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("Not authorized to update this comment");
        }

        return ResponseEntity.ok(commentService.updateComment(commentId, commentDTO));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        CommentDTO comment = commentService.getCommentById(commentId);
        if (!comment.getAuthorId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body("Not authorized to delete this comment");
        }
        
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}