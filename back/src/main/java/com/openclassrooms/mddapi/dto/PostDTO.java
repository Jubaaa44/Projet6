package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(description = "Objet de transfert de données pour les posts/articles")
public class PostDTO {
    @ApiModelProperty(notes = "Identifiant unique du post", example = "1")
    private Long id;
    
    @ApiModelProperty(notes = "Titre du post", example = "Introduction à Spring Boot", required = true)
    private String title;
    
    @ApiModelProperty(notes = "Contenu du post", example = "Spring Boot est un framework qui simplifie...", required = true)
    private String content;
    
    @ApiModelProperty(notes = "Date de création du post", example = "2023-04-25T14:32:25")
    private LocalDateTime date;
    
    @ApiModelProperty(notes = "Identifiant de l'auteur du post", example = "42")
    private Long authorId;
    
    @ApiModelProperty(notes = "Nom d'utilisateur de l'auteur", example = "johndoe")
    private String authorUsername;
    
    @ApiModelProperty(notes = "Identifiant du sujet auquel appartient le post", example = "5")
    private Long subjectId;
    
    @ApiModelProperty(notes = "Nom du sujet auquel appartient le post", example = "Spring Boot")
    private String subjectName;
    
    @ApiModelProperty(notes = "Liste des commentaires associés au post")
    private List<CommentDTO> comments;
}