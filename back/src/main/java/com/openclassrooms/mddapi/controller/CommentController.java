package com.openclassrooms.mddapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.service.CommentService;
import com.openclassrooms.mddapi.security.UserDetailsImpl;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@CrossOrigin 
@RequiredArgsConstructor
@Api(tags = "Comment Controller", description = "Opérations liées aux commentaires")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}")
    @ApiOperation(value = "Ajouter un commentaire à un post", notes = "Ajoute un nouveau commentaire à un post existant")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Commentaire créé avec succès"),
        @ApiResponse(code = 400, message = "Requête invalide"),
        @ApiResponse(code = 404, message = "Post non trouvé"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<CommentDTO> addComment(
            @ApiParam(value = "ID du post auquel ajouter le commentaire", required = true) 
            @PathVariable Long postId, 
            
            @ApiParam(value = "Détails du commentaire à créer", required = true) 
            @RequestBody CommentDTO commentDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        
        // Utilisation du Builder pattern de Lombok
        CommentDTO newComment = CommentDTO.builder()
            .content(commentDTO.getContent())
            .authorId(userDetails.getId())
            .date(LocalDateTime.now())
            .postId(postId)
            .build();

        // Création du commentaire
        CommentDTO createdComment = commentService.createComment(postId, newComment);
        
        log.info("Commentaire créé avec succès : id={}", createdComment.getId());
        return ResponseEntity.ok(createdComment);
    }

    @GetMapping("/post/{postId}")
    @ApiOperation(value = "Récupérer les commentaires d'un post", notes = "Obtient tous les commentaires associés à un post spécifique")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Récupération réussie des commentaires"),
        @ApiResponse(code = 404, message = "Post non trouvé"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(
            @ApiParam(value = "ID du post dont on veut récupérer les commentaires", required = true) 
            @PathVariable Long postId) {
        List<CommentDTO> comments = commentService.getCommentsByPost(postId);
        log.debug("Récupération de {} commentaires pour le post id={}", comments.size(), postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}")
    @ApiOperation(value = "Mettre à jour un commentaire", notes = "Met à jour un commentaire existant (uniquement par son auteur)")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Commentaire mis à jour avec succès"),
        @ApiResponse(code = 400, message = "Non autorisé à modifier ce commentaire"),
        @ApiResponse(code = 404, message = "Commentaire non trouvé"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<?> updateComment(
            @ApiParam(value = "ID du commentaire à mettre à jour", required = true) 
            @PathVariable Long commentId, 
            
            @ApiParam(value = "Détails du commentaire mis à jour", required = true) 
            @RequestBody CommentDTO commentDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        CommentDTO existingComment = commentService.getCommentById(commentId);
        if (!existingComment.getAuthorId().equals(userDetails.getId())) {
            log.warn("Tentative non autorisée de modification du commentaire : id={}, userId={}", 
                     commentId, userDetails.getId());
            return ResponseEntity.badRequest().body("Not authorized to update this comment");
        }

        // Mise à jour avec le builder
        CommentDTO toUpdate = CommentDTO.builder()
            .id(commentId)
            .content(commentDTO.getContent())
            .authorId(existingComment.getAuthorId())
            .authorUsername(existingComment.getAuthorUsername())
            .date(existingComment.getDate())
            .postId(existingComment.getPostId())
            .build();

        CommentDTO updatedComment = commentService.updateComment(commentId, toUpdate);
        
        log.info("Commentaire mis à jour avec succès : id={}", updatedComment.getId());
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "Supprimer un commentaire", notes = "Supprime un commentaire existant (uniquement par son auteur)")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Commentaire supprimé avec succès"),
        @ApiResponse(code = 400, message = "Non autorisé à supprimer ce commentaire"),
        @ApiResponse(code = 404, message = "Commentaire non trouvé"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<?> deleteComment(
            @ApiParam(value = "ID du commentaire à supprimer", required = true) 
            @PathVariable Long commentId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        CommentDTO comment = commentService.getCommentById(commentId);
        if (!comment.getAuthorId().equals(userDetails.getId())) {
            log.warn("Tentative non autorisée de suppression du commentaire : id={}, userId={}", 
                     commentId, userDetails.getId());
            return ResponseEntity.badRequest().body("Not authorized to delete this comment");
        }
        
        commentService.deleteComment(commentId);
        log.info("Commentaire supprimé avec succès : id={}", commentId);
        return ResponseEntity.ok().build();
    }
}