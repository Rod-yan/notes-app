package com.ry.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class NoteRequest {
    private static final int TITLE_MAX_SIZE = 100;

    @NotBlank(message = "Title is mandatory")
    @Size(max = TITLE_MAX_SIZE, message = "Title must be less than 100 characters")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    private boolean archived;
    private Set<String> categoryNames;
}
