package com.openclassrooms.mddapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Data
@Table(name = "posts")
@ApiModel(description = "Entité représentant un article/post dans la plateforme")
public class Post {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Identifiant unique du post", example = "1", required = true, position = 0)
    private Long id;
    
    @Column(nullable = false)
    @ApiModelProperty(notes = "Titre du post", example = "Comment utiliser Spring Boot", required = true, position = 1)
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    @ApiModelProperty(notes = "Contenu du post", example = "Spring Boot est un framework qui simplifie...", required = true, position = 2)
    private String content;
    
    @Column(nullable = false)
    @ApiModelProperty(notes = "Date de création du post", example = "2023-04-25T14:32:25", required = true, position = 3)
    private LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnoreProperties({"posts", "comments", "subscriptions"})
    @ApiModelProperty(notes = "Auteur du post", required = true, position = 4)
    private User author;
    
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonIgnoreProperties("posts")
    @ApiModelProperty(notes = "Sujet auquel appartient le post", required = true, position = 5)
    private Subject subject;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("post")
    @ApiModelProperty(notes = "Liste des commentaires associés au post", position = 6)
    private List<Comment> comments;
}