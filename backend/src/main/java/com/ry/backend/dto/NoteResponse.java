package com.ensolvers.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class NoteResponse {
    private Long id;
    private String title;
    private String content;
    private boolean archived;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<CategoryResponse> categories;
}
