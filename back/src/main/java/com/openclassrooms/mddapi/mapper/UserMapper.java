package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.User;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    
    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        
        if (user.getSubscriptions() != null) {
            dto.setSubscriptionIds(user.getSubscriptions().stream()
                .map(subject -> subject.getId())
                .collect(Collectors.toList()));
            dto.setSubscriptionNames(user.getSubscriptions().stream()
                .map(subject -> subject.getName())
                .collect(Collectors.toList()));
        }
        
        if (user.getPosts() != null) {
            dto.setPostCount(user.getPosts().size());
        }
        
        if (user.getComments() != null) {
            dto.setCommentCount(user.getComments().size());
        }
        
        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(dto.getId());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        return user;
    }
}