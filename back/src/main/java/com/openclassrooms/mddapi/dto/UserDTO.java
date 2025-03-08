package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Objet de transfert de données pour les utilisateurs")
public class UserDTO {
    @ApiModelProperty(notes = "Identifiant unique de l'utilisateur", example = "1")
    private Long id;
    
    @ApiModelProperty(notes = "Adresse email de l'utilisateur (unique)", example = "user@example.com", required = true)
    private String email;
    
    @ApiModelProperty(notes = "Nom d'utilisateur (unique)", example = "johndoe", required = true)
    private String username;
    
    @ApiModelProperty(notes = "Mot de passe de l'utilisateur", example = "password123", required = true, hidden = true)
    private String password;
    
    @ApiModelProperty(notes = "Liste des identifiants des sujets auxquels l'utilisateur est abonné", example = "[1, 2, 3]")
    private List<Long> subscriptionIds;
    
    @ApiModelProperty(notes = "Liste des noms des sujets auxquels l'utilisateur est abonné", example = "[\"Spring Boot\", \"Java\", \"Microservices\"]")
    private List<String> subscriptionNames;
    
    @ApiModelProperty(notes = "Nombre de posts créés par l'utilisateur", example = "15")
    private int postCount;
    
    @ApiModelProperty(notes = "Nombre de commentaires créés par l'utilisateur", example = "42")
    private int commentCount;
}