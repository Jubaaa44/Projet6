package com.openclassrooms.mddapi.model;

import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "subjects")
public class Subject {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    // Articles liés à ce sujet
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Post> posts;
    
    // Utilisateurs abonnés à ce sujet
    @ManyToMany(mappedBy = "subscriptions")
    private List<User> subscribers;
}