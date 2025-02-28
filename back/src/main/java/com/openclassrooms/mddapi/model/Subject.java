package com.openclassrooms.mddapi.model;

import java.util.List;
import javax.persistence.*;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Data
@Table(name = "subjects")
@ApiModel(description = "Entité représentant un sujet/thématique de discussion")
public class Subject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Identifiant unique du sujet", example = "1", required = true, position = 0)
    private Long id;
    
    @Column(nullable = false, unique = true)
    @ApiModelProperty(notes = "Nom du sujet (unique)", example = "Spring Boot", required = true, position = 1)
    private String name;
    
    @Column(nullable = false, unique = false)
    @ApiModelProperty(notes = "Description du sujet", example = "Tout ce qui concerne le framework Spring Boot", required = true, position = 2)
    private String description;
    
    // Articles liés à ce sujet
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    @ApiModelProperty(notes = "Liste des posts associés à ce sujet", position = 3)
    private List<Post> posts;
    
    // Utilisateurs abonnés à ce sujet
    @ManyToMany(mappedBy = "subscriptions")
    @ApiModelProperty(notes = "Liste des utilisateurs abonnés à ce sujet", position = 4)
    private List<User> subscribers;
}