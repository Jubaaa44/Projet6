package com.openclassrooms.mddapi.service;

import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.Post;
import java.util.List;

/**
 * Service gérant les opérations métier liées aux posts/articles.
 * Cette classe implémente les fonctionnalités CRUD pour les posts ainsi que
 * des méthodes spécifiques comme la récupération des posts par auteur, par sujet,
 * ou pour le fil d'actualité d'un utilisateur.
 */
@Service
public interface PostService {
    

    /**
     * Crée un nouveau post.
     * La date du post est automatiquement définie à la date/heure actuelle.
     * 
     * @param post L'entité Post à créer
     * @return Le post créé, incluant l'ID généré
     */
    public Post createPost(Post post);
    
    
    /**
     * Récupère un post par son identifiant.
     * 
     * @param id Identifiant du post
     * @return Le post correspondant
     * @throws RuntimeException Si le post n'existe pas
     */
    public Post getPostById(Long id);
    /**
     * Récupère tous les posts disponibles.
     * 
     * @return Liste de tous les posts
     */
    public List<Post> getAllPosts();
    /**
     * Récupère tous les posts créés par un utilisateur spécifique.
     * 
     * @param authorId Identifiant de l'utilisateur/auteur
     * @return Liste des posts créés par l'auteur
     */
    public List<Post> getPostsByAuthor(Long authorId);
    /**
     * Récupère tous les posts associés à un sujet spécifique.
     * 
     * @param subjectId Identifiant du sujet
     * @return Liste des posts du sujet spécifié
     */
    public List<Post> getPostsBySubject(Long subjectId);

    /**
     * Récupère le fil d'actualité personnalisé pour un utilisateur spécifique.
     * Le fil contient les posts des sujets auxquels l'utilisateur est abonné.
     * 
     * @param userId Identifiant de l'utilisateur
     * @return Liste des posts pour le fil d'actualité de l'utilisateur
     */
    public List<Post> getFeedForUser(Long userId);
    /**
     * Met à jour un post existant.
     * Seuls le titre et le contenu sont mis à jour, l'auteur et la date restent inchangés.
     * 
     * @param id Identifiant du post à mettre à jour
     * @param postDetails Nouvelles données du post
     * @return Le post mis à jour
     * @throws RuntimeException Si le post n'existe pas
     */
    public Post updatePost(Long id, Post postDetails);
    
    /**
     * Supprime un post spécifique.
     * 
     * @param id Identifiant du post à supprimer
     */
    public void deletePost(Long id);
}