package com.ensolvers.backend.mapper;

import com.ensolvers.backend.dto.CategoryResponse;
import com.ensolvers.backend.dto.NoteRequest;
import com.ensolvers.backend.dto.NoteResponse;
import com.ensolvers.backend.model.Category;
import com.ensolvers.backend.model.Note;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NoteMapperTest {

    private final NoteMapper mapper = Mappers.getMapper(NoteMapper.class);

    @Test
    void toResponse_shouldMapAllFields() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Title");
        note.setContent("Content");
        note.setArchived(true);
        LocalDateTime now = LocalDateTime.now();
        note.setCreatedAt(now);
        note.setUpdatedAt(now);

        NoteResponse response = mapper.toResponse(note);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Title");
        assertThat(response.getContent()).isEqualTo("Content");
        assertThat(response.isArchived()).isTrue();
        assertThat(response.getCreatedAt()).isEqualTo(now);
        assertThat(response.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void toResponseList_shouldMapList() {
        Note note = new Note();
        note.setId(1L);
        List<NoteResponse> responses = mapper.toResponseList(List.of(note));
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
    }

    @Test
    void toEntity_shouldMapRequest() {
        NoteRequest request = new NoteRequest();
        request.setTitle("New Title");
        request.setContent("New Content");

        Note note = mapper.toEntity(request);

        assertThat(note.getTitle()).isEqualTo("New Title");
        assertThat(note.getContent()).isEqualTo("New Content");
    }

    @Test
    void updateEntity_shouldUpdateFields() {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Old Title");

        NoteRequest request = new NoteRequest();
        request.setTitle("New Title");

        mapper.updateEntity(request, note);

        assertThat(note.getId()).isEqualTo(1L);
        assertThat(note.getTitle()).isEqualTo("New Title");
    }

    @Test
    void toCategoryResponse_shouldMapCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Work");

        CategoryResponse response = mapper.toCategoryResponse(category);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Work");
    }
}
