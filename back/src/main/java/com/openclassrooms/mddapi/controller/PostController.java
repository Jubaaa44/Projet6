package com.openclassrooms.mddapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.mapper.PostMapperImpl;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.service.PostService;
import com.openclassrooms.mddapi.service.SubjectService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.UserDetailsImpl;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@CrossOrigin
@RequiredArgsConstructor
@Api(tags = "Post Controller", description = "Opérations liées aux posts/articles")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final SubjectService subjectService;
    private final PostMapperImpl postMapperImpl;

    @GetMapping("/feed")
    @ApiOperation(value = "Récupérer le fil d'actualité", notes = "Obtient le fil d'actualité personnalisé pour l'utilisateur connecté")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Récupération réussie du fil d'actualité"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<List<PostDTO>> getFeed() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        
        List<Post> posts = postService.getFeedForUser(userDetails.getId());
        List<PostDTO> postDTOs = posts.stream()
            .map(postMapperImpl::toDto)
            .collect(Collectors.toList());
        
        log.debug("Récupération de {} posts pour le feed de l'utilisateur id={}", 
                  postDTOs.size(), userDetails.getId());
        return ResponseEntity.ok(postDTOs);
    }

    @PostMapping
    @ApiOperation(value = "Créer un post", notes = "Crée un nouveau post par l'utilisateur connecté")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Post créé avec succès"),
        @ApiResponse(code = 400, message = "Requête invalide"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise"),
        @ApiResponse(code = 404, message = "Sujet non trouvé")
    })
    public ResponseEntity<PostDTO> createPost(
            @ApiParam(value = "Détails du post à créer", required = true) 
            @RequestBody PostDTO postDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        
        Post post = postMapperImpl.toEntity(postDTO);
        post.setAuthor(userService.getUserById(userDetails.getId()));
        
        if (postDTO.getSubjectId() != null) {
            Subject subject = subjectService.getSubjectById(postDTO.getSubjectId());
            post.setSubject(subject);
        }
        
        Post savedPost = postService.createPost(post);
        PostDTO result = postMapperImpl.toDto(savedPost);
        
        log.info("Post créé avec succès : id={}", result.getId());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "Récupérer un post", notes = "Obtient les détails d'un post spécifique par son ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Récupération réussie du post"),
        @ApiResponse(code = 404, message = "Post non trouvé")
    })
    public ResponseEntity<PostDTO> getPost(
            @ApiParam(value = "ID du post à récupérer", required = true) 
            @PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        PostDTO postDTO = postMapperImpl.toDto(post);
        
        log.debug("Post récupéré : id={}", postId);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping("/subject/{subjectId}")
    @ApiOperation(value = "Récupérer les posts par sujet", notes = "Obtient tous les posts associés à un sujet spécifique")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Récupération réussie des posts"),
        @ApiResponse(code = 404, message = "Sujet non trouvé")
    })
    public ResponseEntity<List<PostDTO>> getPostsBySubject(
            @ApiParam(value = "ID du sujet dont on veut récupérer les posts", required = true) 
            @PathVariable Long subjectId) {
        List<Post> posts = postService.getPostsBySubject(subjectId);
        List<PostDTO> postDTOs = posts.stream()
            .map(postMapperImpl::toDto)
            .collect(Collectors.toList());
        
        log.debug("Récupération de {} posts pour le sujet id={}", postDTOs.size(), subjectId);
        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/user/{userId}")
    @ApiOperation(value = "Récupérer les posts par utilisateur", notes = "Obtient tous les posts créés par un utilisateur spécifique")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Récupération réussie des posts"),
        @ApiResponse(code = 404, message = "Utilisateur non trouvé")
    })
    public ResponseEntity<List<PostDTO>> getPostsByUser(
            @ApiParam(value = "ID de l'utilisateur dont on veut récupérer les posts", required = true) 
            @PathVariable Long userId) {
        List<Post> posts = postService.getPostsByAuthor(userId);
        List<PostDTO> postDTOs = posts.stream()
            .map(postMapperImpl::toDto)
            .collect(Collectors.toList());
        
        log.debug("Récupération de {} posts pour l'utilisateur id={}", postDTOs.size(), userId);
        return ResponseEntity.ok(postDTOs);
    }

    @PutMapping("/{postId}")
    @ApiOperation(value = "Mettre à jour un post", notes = "Met à jour un post existant (uniquement par son auteur)")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Post mis à jour avec succès"),
        @ApiResponse(code = 400, message = "Non autorisé à modifier ce post"),
        @ApiResponse(code = 404, message = "Post non trouvé"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<?> updatePost(
            @ApiParam(value = "ID du post à mettre à jour", required = true) 
            @PathVariable Long postId, 
            
            @ApiParam(value = "Détails du post mis à jour", required = true) 
            @RequestBody PostDTO postDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        Post existingPost = postService.getPostById(postId);
        if (!existingPost.getAuthor().getId().equals(userDetails.getId())) {
            log.warn("Tentative non autorisée de modification du post : id={}, userId={}", 
                     postId, userDetails.getId());
            return ResponseEntity.badRequest().body("Not authorized to update this post");
        }

        Post post = postMapperImpl.toEntity(postDTO);
        Post updatedPost = postService.updatePost(postId, post);
        PostDTO result = postMapperImpl.toDto(updatedPost);
        
        log.info("Post mis à jour avec succès : id={}", result.getId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{postId}")
    @ApiOperation(value = "Supprimer un post", notes = "Supprime un post existant (uniquement par son auteur)")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Post supprimé avec succès"),
        @ApiResponse(code = 400, message = "Non autorisé à supprimer ce post"),
        @ApiResponse(code = 404, message = "Post non trouvé"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<?> deletePost(
            @ApiParam(value = "ID du post à supprimer", required = true) 
            @PathVariable Long postId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        Post post = postService.getPostById(postId);
        if (!post.getAuthor().getId().equals(userDetails.getId())) {
            log.warn("Tentative non autorisée de suppression du post : id={}, userId={}", 
                     postId, userDetails.getId());
            return ResponseEntity.badRequest().body("Not authorized to delete this post");
        }
        
        postService.deletePost(postId);
        log.info("Post supprimé avec succès : id={}", postId);
        return ResponseEntity.ok().build();
    }
}