package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.request.LoginRequest;
import com.openclassrooms.mddapi.security.JwtResponse;
import com.openclassrooms.mddapi.security.JwtTokenProvider;
import com.openclassrooms.mddapi.security.UserDetailsImpl;
import com.openclassrooms.mddapi.service.UserServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Api(tags = "User Controller", description = "Opérations d'authentification et de gestion des utilisateurs")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserMapper userMapper;

    
    @PostMapping("/login")
    @ApiOperation(value = "Connecter un utilisateur", notes = "Authentifie un utilisateur et retourne un token JWT")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Authentification réussie"),
        @ApiResponse(code = 401, message = "Identifiants invalides")
    })
    public ResponseEntity<JwtResponse> authenticateUser(
            @ApiParam(value = "Informations de connexion", required = true) 
            @RequestBody LoginRequest loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        return ResponseEntity.ok(new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail()
        ));
    }
    
    @PostMapping("/register")
    @ApiOperation(value = "Inscrire un nouvel utilisateur", notes = "Crée un nouveau compte utilisateur")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Utilisateur créé avec succès"),
        @ApiResponse(code = 400, message = "Email ou nom d'utilisateur déjà utilisé")
    })
    public ResponseEntity<UserDTO> registerUser(
            @ApiParam(value = "Détails du nouvel utilisateur", required = true) 
            @RequestBody UserDTO userDTO) {
        // Utilisation du mapper pour convertir le DTO en entité
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Appel au service
        User savedUser = userServiceImpl.createUser(user);
        
        // Reconversion en DTO pour la réponse
        UserDTO responseDTO = userMapper.toDto(savedUser);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/profile")
    @ApiOperation(value = "Récupérer le profil utilisateur", notes = "Obtient les détails du profil de l'utilisateur connecté")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Récupération réussie du profil"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<UserDTO> getUserProfile() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        // Récupération de l'entité
        User user = userServiceImpl.getUserById(userDetails.getId());
        
        // Conversion en DTO pour la réponse
        UserDTO userDTO = userMapper.toDto(user);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/profile")
    @ApiOperation(value = "Mettre à jour le profil utilisateur", notes = "Met à jour les informations du profil de l'utilisateur connecté")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Profil mis à jour avec succès"),
        @ApiResponse(code = 400, message = "Données de profil invalides"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<UserDTO> updateUserProfile(
            @ApiParam(value = "Détails du profil mis à jour", required = true) 
            @RequestBody UserDTO userDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        // Conversion du DTO reçu en entité
        User userToUpdate = userMapper.toEntity(userDTO);
        
        // Mise à jour via le service
        User updatedUser = userServiceImpl.updateUser(userDetails.getId(), userToUpdate);
        
        // Reconversion en DTO pour la réponse
        UserDTO responseDTO = userMapper.toDto(updatedUser);
        return ResponseEntity.ok(responseDTO);
    }
}