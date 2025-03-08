package com.openclassrooms.mddapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
@Api(tags = "User Controller", description = "Opérations d'authentification et de gestion des utilisateurs")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final UserMapper userMapper;
    
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
        
        log.info("Utilisateur connecté avec succès : id={}, email={}", 
                 userDetails.getId(), userDetails.getEmail());
        
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
        
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        User savedUser = userServiceImpl.createUser(user);
        UserDTO responseDTO = userMapper.toDto(savedUser);
        
        log.info("Nouvel utilisateur inscrit : id={}, username={}", 
                 responseDTO.getId(), responseDTO.getUsername());
        
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
            
        User user = userServiceImpl.getUserById(userDetails.getId());
        UserDTO userDTO = userMapper.toDto(user);
        
        log.debug("Profil utilisateur récupéré : id={}", userDetails.getId());
        
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
            
        User userToUpdate = userMapper.toEntity(userDTO);
        
        // Si mot de passe présent, l'encoder
        if (userToUpdate.getPassword() != null && !userToUpdate.getPassword().isEmpty()) {
            userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        }
        
        User updatedUser = userServiceImpl.updateUser(userDetails.getId(), userToUpdate);
        UserDTO responseDTO = userMapper.toDto(updatedUser);
        
        log.info("Profil utilisateur mis à jour : id={}", userDetails.getId());
        
        return ResponseEntity.ok(responseDTO);
    }
}