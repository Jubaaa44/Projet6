package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.model.Subject;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

/**
 * Implémentation du mapper pour convertir entre les entités Subject et leurs DTOs.
 */
@Component
public class SubjectMapperImpl implements SubjectMapper {
    
    /**
     * Convertit une entité Subject en SubjectDTO.
     * Inclut les identifiants des posts associés et des abonnés ainsi que leurs compteurs.
     *
     * @param subject L'entité Subject à convertir
     * @return Le SubjectDTO correspondant, ou null si l'entité d'entrée est null
     */
    @Override
    public SubjectDTO toDto(Subject subject) {
        if (subject == null) {
            return null;
        }
        
        SubjectDTO dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setDescription(subject.getDescription());
        
        if (subject.getPosts() != null) {
            dto.setPostIds(subject.getPosts().stream()
                .map(post -> post.getId())
                .collect(Collectors.toList()));
            dto.setPostCount(subject.getPosts().size());
        }
        
        if (subject.getSubscribers() != null) {
            dto.setSubscriberIds(subject.getSubscribers().stream()
                .map(user -> user.getId())
                .collect(Collectors.toList()));
            dto.setSubscriberCount(subject.getSubscribers().size());
        }
        
        return dto;
    }

    /**
     * Convertit un SubjectDTO en entité Subject.
     * Note: Cette méthode ne remplit pas les associations (posts, subscribers),
     * elles doivent être gérées dans la couche service.
     *
     * @param dto Le SubjectDTO à convertir
     * @return L'entité Subject correspondante, ou null si le DTO d'entrée est null
     */
    @Override
    public Subject toEntity(SubjectDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Subject subject = new Subject();
        subject.setId(dto.getId());
        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());
        return subject;
    }
}