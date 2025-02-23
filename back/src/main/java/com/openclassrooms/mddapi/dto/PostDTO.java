package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime date;
    private Long authorId;
    private String authorUsername;
    private Long subjectId;
    private String subjectName;
    private List<CommentDTO> comments;
}