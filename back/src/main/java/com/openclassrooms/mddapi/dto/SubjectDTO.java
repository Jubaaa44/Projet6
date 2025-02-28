package com.openclassrooms.mddapi.dto;

import lombok.Data;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(description = "Objet de transfert de données pour les sujets/thématiques")
public class SubjectDTO {
    @ApiModelProperty(notes = "Identifiant unique du sujet", example = "1")
    private Long id;
    
    @ApiModelProperty(notes = "Nom du sujet (unique)", example = "Spring Boot", required = true)
    private String name;
    
    @ApiModelProperty(notes = "Description du sujet", example = "Tout ce qui concerne le framework Spring Boot", required = true)
    private String description;
    
    @ApiModelProperty(notes = "Liste des identifiants des posts associés à ce sujet", example = "[1, 2, 3]")
    private List<Long> postIds;
    
    @ApiModelProperty(notes = "Liste des identifiants des utilisateurs abonnés à ce sujet", example = "[42, 43, 44]")
    private List<Long> subscriberIds;
    
    @ApiModelProperty(notes = "Nombre d'utilisateurs abonnés à ce sujet", example = "156")
    private int subscriberCount;
    
    @ApiModelProperty(notes = "Nombre de posts associés à ce sujet", example = "24")
    private int postCount;
}