package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.model.Subject;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class SubjectMapper {
    
    public SubjectDTO toDto(Subject subject) {
        if (subject == null) {
            return null;
        }
        
        SubjectDTO dto = new SubjectDTO();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        
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

    public Subject toEntity(SubjectDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Subject subject = new Subject();
        subject.setId(dto.getId());
        subject.setName(dto.getName());
        return subject;
    }
}