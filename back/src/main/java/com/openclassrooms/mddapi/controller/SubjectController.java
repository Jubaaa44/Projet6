package com.openclassrooms.mddapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.mapper.SubjectMapper;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.service.SubjectService;
import com.openclassrooms.mddapi.security.UserDetailsImpl;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Slf4j
@RestController
@RequestMapping("/api/subjects")
@CrossOrigin
@RequiredArgsConstructor
@Api(tags = "Subject Controller", description = "Opérations liées aux sujets/thématiques de discussion")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;

    @GetMapping
    @ApiOperation(value = "Récupérer tous les sujets", notes = "Obtient la liste de tous les sujets disponibles")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Récupération réussie des sujets")
    })
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        List<SubjectDTO> subjectDTOs = subjects.stream()
            .map(subjectMapper::toDto)
            .collect(Collectors.toList());
        
        log.debug("Récupération de {} sujets", subjectDTOs.size());
        return ResponseEntity.ok(subjectDTOs);
    }

    @PostMapping("/{subjectId}/subscribe")
    @ApiOperation(value = "S'abonner à un sujet", notes = "Permet à l'utilisateur connecté de s'abonner à un sujet spécifique")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Abonnement réussi"),
        @ApiResponse(code = 404, message = "Sujet non trouvé"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<?> subscribeToSubject(
            @ApiParam(value = "ID du sujet auquel s'abonner", required = true) 
            @PathVariable Long subjectId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        subjectService.subscribeUser(userDetails.getId(), subjectId);
        
        log.info("Utilisateur id={} abonné au sujet id={}", userDetails.getId(), subjectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{subjectId}/unsubscribe")
    @ApiOperation(value = "Se désabonner d'un sujet", notes = "Permet à l'utilisateur connecté de se désabonner d'un sujet spécifique")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Désabonnement réussi"),
        @ApiResponse(code = 404, message = "Sujet non trouvé"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<?> unsubscribeFromSubject(
            @ApiParam(value = "ID du sujet duquel se désabonner", required = true) 
            @PathVariable Long subjectId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
            
        subjectService.unsubscribeUser(userDetails.getId(), subjectId);
        
        log.info("Utilisateur id={} désabonné du sujet id={}", userDetails.getId(), subjectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{subjectId}")
    @ApiOperation(value = "Récupérer un sujet", notes = "Obtient les détails d'un sujet spécifique par son ID")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Récupération réussie du sujet"),
        @ApiResponse(code = 404, message = "Sujet non trouvé")
    })
    public ResponseEntity<SubjectDTO> getSubject(
            @ApiParam(value = "ID du sujet à récupérer", required = true) 
            @PathVariable Long subjectId) {
        Subject subject = subjectService.getSubjectById(subjectId);
        SubjectDTO subjectDTO = subjectMapper.toDto(subject);
        
        log.debug("Sujet récupéré : id={}", subjectId);
        return ResponseEntity.ok(subjectDTO);
    }
    
    @PostMapping
    @ApiOperation(value = "Créer un nouveau sujet", notes = "Permet la création d'un nouveau sujet de discussion")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Sujet créé avec succès"),
        @ApiResponse(code = 400, message = "Requête invalide"),
        @ApiResponse(code = 401, message = "Non autorisé - authentification requise")
    })
    public ResponseEntity<SubjectDTO> createSubject(
            @ApiParam(value = "Détails du sujet à créer", required = true) 
            @RequestBody SubjectDTO subjectDTO) {
        Subject subject = subjectMapper.toEntity(subjectDTO);
        Subject createdSubject = subjectService.createSubject(subject);
        SubjectDTO result = subjectMapper.toDto(createdSubject);
        
        log.info("Sujet créé avec succès : id={}, nom={}", result.getId(), result.getName());
        return ResponseEntity.ok(result);
    }
}