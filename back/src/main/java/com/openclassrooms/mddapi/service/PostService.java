package com.openclassrooms.mddapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserRepository userRepository;

    public Post createPost(Post post) {
        post.setDate(LocalDateTime.now());
        return postRepository.save(post);
    }
    
    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post not found"));
    }
    
    public List<Post> getAllPosts() {
        try {
            System.out.println("Tentative de récupération de tous les posts");
            List<Post> posts = postRepository.findAll();
            System.out.println("Nombre de posts trouvés : " + posts.size());
            posts.forEach(post -> System.out.println("Post trouvé : " + post.getId() + " - " + post.getTitle()));
            return posts;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des posts : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    public List<Post> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthorId(authorId);
    }
    
    public List<Post> getPostsBySubject(Long subjectId) {
        return postRepository.findBySubjectId(subjectId);
    }

    public List<Post> getFeedForUser(Long userId) {
        return postRepository.findFeedByUserId(userId);
    }
    
    public Post updatePost(Long id, Post postDetails) {
        Post post = getPostById(id);
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());
        // Ne pas mettre à jour l'auteur ou la date
        return postRepository.save(post);
    }
    
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}