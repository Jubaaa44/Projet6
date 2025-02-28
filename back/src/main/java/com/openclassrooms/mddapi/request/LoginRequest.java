package com.openclassrooms.mddapi.request;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(description = "Requête pour l'authentification utilisateur")
public class LoginRequest {
    
    @ApiModelProperty(notes = "Email de l'utilisateur", example = "user@example.com", required = true)
    private String email;
    
    @ApiModelProperty(notes = "Mot de passe de l'utilisateur", example = "password123", required = true)
    private String password;
}