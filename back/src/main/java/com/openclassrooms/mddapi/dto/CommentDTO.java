package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(description = "Objet de transfert de données pour les commentaires")
public class CommentDTO {
    @ApiModelProperty(notes = "Identifiant unique du commentaire", example = "1")
    private Long id;
    
    @ApiModelProperty(notes = "Contenu du commentaire", example = "Excellent article !", required = true)
    private String content;
    
    @ApiModelProperty(notes = "Date de création du commentaire", example = "2023-04-25T15:42:30")
    private LocalDateTime date;
    
    @ApiModelProperty(notes = "Identifiant de l'auteur du commentaire", example = "42")
    private Long authorId;
    
    @ApiModelProperty(notes = "Nom d'utilisateur de l'auteur", example = "johndoe")
    private String authorUsername;
    
    @ApiModelProperty(notes = "Identifiant du post auquel appartient le commentaire", example = "123")
    private Long postId;
}