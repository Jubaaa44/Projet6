package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.model.Post;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

/**
 * Implémentation du mapper pour convertir entre les entités Post et leurs DTOs.
 */
@Component
public class PostMapperImpl implements PostMapper {
    
    private final CommentMapper commentMapper;
    
    /**
     * Constructeur avec injection de dépendance du CommentMapper.
     *
     * @param commentMapper Le mapper pour convertir les commentaires associés
     */
    public PostMapperImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }
    
    @Override
    public PostDTO toDto(Post post) {
        if (post == null) {
            return null;
        }
        
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setDate(post.getDate());
        
        if (post.getAuthor() != null) {
            dto.setAuthorId(post.getAuthor().getId());
            dto.setAuthorUsername(post.getAuthor().getUsername());
        }
        
        if (post.getSubject() != null) {
            dto.setSubjectId(post.getSubject().getId());
            dto.setSubjectName(post.getSubject().getName());
        }
        
        if (post.getComments() != null) {
            dto.setComments(post.getComments().stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    @Override
    public Post toEntity(PostDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Post post = new Post();
        post.setId(dto.getId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setDate(dto.getDate());
        return post;
    }
}