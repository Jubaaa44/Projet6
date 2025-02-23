package com.openclassrooms.mddapi.dto;

import lombok.Data;
import java.util.List;

@Data
public class SubjectDTO {
    private Long id;
    private String name;
    private List<Long> postIds;
    private List<Long> subscriberIds;
    private int subscriberCount;
    private int postCount;
}