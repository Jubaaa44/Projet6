package com.openclassrooms.mddapi.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Data
@Table(name = "comments")
@ApiModel(description = "Entité représentant un commentaire sur un post")
public class Comment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Identifiant unique du commentaire", example = "1", required = true, position = 0)
    private Long id;
    
    @Column(nullable = false)
    @ApiModelProperty(notes = "Contenu du commentaire", example = "Super article, merci pour le partage !", required = true, position = 1)
    private String content;
    
    @Column(nullable = false)
    @ApiModelProperty(notes = "Date de création du commentaire", example = "2023-04-25T15:42:30", required = true, position = 2)
    private LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @ApiModelProperty(notes = "Auteur du commentaire", required = true, position = 3)
    private User author;
    
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @ApiModelProperty(notes = "Post auquel est associé le commentaire", required = true, position = 4)
    private Post post;
}