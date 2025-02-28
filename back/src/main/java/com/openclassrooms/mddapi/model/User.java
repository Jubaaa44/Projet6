package com.openclassrooms.mddapi.model;

import java.util.List;
import javax.persistence.*;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Data
@Table(name = "users") // "user" est un mot réservé dans SQL
@ApiModel(description = "Entité représentant un utilisateur de la plateforme")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Identifiant unique de l'utilisateur", example = "1", required = true, position = 0)
    private Long id;
    
    @Column(nullable = false, unique = true)
    @ApiModelProperty(notes = "Adresse email de l'utilisateur (unique)", example = "user@example.com", required = true, position = 1)
    private String email;
    
    @Column(nullable = false)
    @ApiModelProperty(notes = "Mot de passe de l'utilisateur (encodé)", example = "$2a$10$...", required = true, position = 2, hidden = true)
    private String password;
    
    @Column(name = "username", nullable = false, unique = true)
    @ApiModelProperty(notes = "Nom d'utilisateur (unique)", example = "johndoe", required = true, position = 3)
    private String username;
    
    // Relation avec les abonnements (subjects/topics)
    @ManyToMany
    @JoinTable(
        name = "user_subscriptions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @ApiModelProperty(notes = "Liste des sujets auxquels l'utilisateur est abonné", position = 4)
    private List<Subject> subscriptions;
    
    // Relation avec les articles créés par l'utilisateur
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "Liste des posts créés par l'utilisateur", position = 5)
    private List<Post> posts;
    
    // Relation avec les commentaires créés par l'utilisateur
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "Liste des commentaires créés par l'utilisateur", position = 6)
    private List<Comment> comments;
}