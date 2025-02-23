package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime date;
    private Long authorId;
    private String authorUsername;
    private Long postId;
}