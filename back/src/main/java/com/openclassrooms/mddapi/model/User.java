package com.openclassrooms.mddapi.model;

import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users") // "user" est un mot réservé dans SQL
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    // Relation avec les abonnements (subjects/topics)
    @ManyToMany
    @JoinTable(
        name = "user_subscriptions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subscriptions;
    
    // Relation avec les articles créés par l'utilisateur
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;
    
    // Relation avec les commentaires créés par l'utilisateur
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments;
}