package com.openclassrooms.mddapi.security;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(description = "Réponse contenant le JWT et les informations de l'utilisateur authentifié")
public class JwtResponse {
    
    @ApiModelProperty(notes = "Token JWT à utiliser pour les requêtes authentifiées", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String token;
    
    @ApiModelProperty(notes = "Type du token", example = "Bearer", required = true)
    private String type = "Bearer";
    
    @ApiModelProperty(notes = "Identifiant de l'utilisateur", example = "1", required = true)
    private Long id;
    
    @ApiModelProperty(notes = "Nom d'utilisateur", example = "johndoe", required = true)
    private String username;
    
    @ApiModelProperty(notes = "Email de l'utilisateur", example = "user@example.com", required = true)
    private String email;

    public JwtResponse(String token, Long id, String username, String email) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
    }
}