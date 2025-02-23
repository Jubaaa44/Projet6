package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    
    public CommentDTO toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setDate(comment.getDate());
        
        if (comment.getAuthor() != null) {
            dto.setAuthorId(comment.getAuthor().getId());
            dto.setAuthorUsername(comment.getAuthor().getUsername());
        }
        
        if (comment.getPost() != null) {
            dto.setPostId(comment.getPost().getId());
        }
        
        return dto;
    }

    public Comment toEntity(CommentDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setContent(dto.getContent());
        comment.setDate(dto.getDate());
        return comment;
    }
}