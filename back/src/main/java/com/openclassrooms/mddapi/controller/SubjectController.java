package com.openclassrooms.mddapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.service.SubjectService;
import com.openclassrooms.mddapi.service.UserService;
import com.openclassrooms.mddapi.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @PostMapping("/{subjectId}/subscribe")
    public ResponseEntity<?> subscribeToSubject(@PathVariable Long subjectId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        subjectService.subscribeUser(userDetails.getId(), subjectId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{subjectId}/unsubscribe")
    public ResponseEntity<?> unsubscribeFromSubject(@PathVariable Long subjectId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        subjectService.unsubscribeUser(userDetails.getId(), subjectId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<?> getSubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(subjectService.getSubjectById(subjectId));
    }
}