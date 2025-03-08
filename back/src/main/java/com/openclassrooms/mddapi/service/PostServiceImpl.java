package com.openclassrooms.mddapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.controller.UserController;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service gérant les opérations métier liées aux posts/articles.
 * Cette classe implémente les fonctionnalités CRUD pour les posts ainsi que
 * des méthodes spécifiques comme la récupération des posts par auteur, par sujet,
 * ou pour le fil d'actualité d'un utilisateur.
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {
    
    @Autowired
    private PostRepository postRepository;


    /**
     * Crée un nouveau post.
     * La date du post est automatiquement définie à la date/heure actuelle.
     * 
     * @param post L'entité Post à créer
     * @return Le post créé, incluant l'ID généré
     */
    public Post createPost(Post post) {
        post.setDate(LocalDateTime.now());
        return postRepository.save(post);
    }
    
    /**
     * Récupère un post par son identifiant.
     * 
     * @param id Identifiant du post
     * @return Le post correspondant
     * @throws RuntimeException Si le post n'existe pas
     */
    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }
    
    /**
     * Récupère tous les posts disponibles.
     * 
     * @return Liste de tous les posts
     */
    public List<Post> getAllPosts() {
        try {
            log.info("Tentative de récupération de tous les posts");
            List<Post> posts = postRepository.findAll();
            log.info("Nombre de posts trouvés : {}", posts.size());
            posts.forEach(post -> log.debug("Post trouvé : id={}, titre={}", post.getId(), post.getTitle()));
            return posts;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des posts : {}", e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Récupère tous les posts créés par un utilisateur spécifique.
     * 
     * @param authorId Identifiant de l'utilisateur/auteur
     * @return Liste des posts créés par l'auteur
     */
    public List<Post> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }
    
    /**
     * Récupère tous les posts associés à un sujet spécifique.
     * 
     * @param subjectId Identifiant du sujet
     * @return Liste des posts du sujet spécifié
     */
    public List<Post> getPostsBySubject(Long subjectId) {
        return postRepository.findBySubjectId(subjectId);
    }

    /**
     * Récupère le fil d'actualité personnalisé pour un utilisateur spécifique.
     * Le fil contient les posts des sujets auxquels l'utilisateur est abonné.
     * 
     * @param userId Identifiant de l'utilisateur
     * @return Liste des posts pour le fil d'actualité de l'utilisateur
     */
    public List<Post> getFeedForUser(Long userId) {
        return postRepository.findFeedByUserId(userId);
    }
    
    /**
     * Met à jour un post existant.
     * Seuls le titre et le contenu sont mis à jour, l'auteur et la date restent inchangés.
     * 
     * @param id Identifiant du post à mettre à jour
     * @param postDetails Nouvelles données du post
     * @return Le post mis à jour
     * @throws RuntimeException Si le post n'existe pas
     */
    public Post updatePost(Long id, Post postDetails) {
        Post post = getPostById(id);
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        // Ne pas mettre à jour l'auteur ou la date
        return postRepository.save(post);
    }
    
    /**
     * Supprime un post spécifique.
     * 
     * @param id Identifiant du post à supprimer
     */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}