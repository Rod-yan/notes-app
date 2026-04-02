package com.ry.backend.service;

import com.ry.backend.dto.NoteRequest;
import com.ry.backend.dto.NoteResponse;
import com.ry.backend.exception.ResourceNotFoundException;
import com.ry.backend.mapper.NoteMapper;
import com.ry.backend.model.Category;
import com.ry.backend.model.Note;
import com.ry.backend.model.User;
import com.ry.backend.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private NoteService noteService;

    private Note note;
    private NoteResponse noteResponse;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "pass");

        note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("Content");
        note.setArchived(false);
        note.setUser(user);

        noteResponse = new NoteResponse();
        noteResponse.setId(1L);
        noteResponse.setTitle("Test Note");
        noteResponse.setContent("Content");
        noteResponse.setArchived(false);
    }

    @Test
    void getNotes_shouldReturnList() {
        when(noteRepository.findByArchivedAndCategoryNameAndUser(anyBoolean(), any(), eq(user))).thenReturn(List.of(note));
        when(noteMapper.toResponseList(any())).thenReturn(List.of(noteResponse));

        List<NoteResponse> result = noteService.getNotes(false, "Work", user);

        assertThat(result).hasSize(1);
        verify(noteRepository).findByArchivedAndCategoryNameAndUser(false, "Work", user);
    }

    @Test
    void createNote_shouldSaveAndReturn() {
        NoteRequest request = new NoteRequest();
        request.setTitle("New Note");
        request.setCategoryNames(Set.of("Work"));

        Category category = new Category();
        category.setName("Work");
        category.setUser(user);

        when(noteMapper.toEntity(request)).thenReturn(note);
        when(categoryService.getOrCreate("Work", user)).thenReturn(category);
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        when(noteMapper.toResponse(note)).thenReturn(noteResponse);

        NoteResponse result = noteService.createNote(request, user);

        assertThat(result.getTitle()).isEqualTo("Test Note");
        verify(noteRepository).save(any(Note.class));
        verify(categoryService).getOrCreate("Work", user);
    }

    @Test
    void updateNote_shouldUpdateExisting() {
        NoteRequest request = new NoteRequest();
        request.setTitle("Updated Title");
        request.setCategoryNames(Set.of("Work"));

        Category category = new Category();
        category.setName("Work");
        category.setUser(user);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(categoryService.getOrCreate("Work", user)).thenReturn(category);
        when(noteRepository.save(any(Note.class))).thenReturn(note);
        when(noteMapper.toResponse(note)).thenReturn(noteResponse);

        noteService.updateNote(1L, request, user);

        verify(noteMapper).updateEntity(request, note);
        verify(noteRepository).save(note);
    }

    @Test
    void updateNote_shouldThrow_whenNotFound() {
        NoteRequest request = new NoteRequest();
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.updateNote(1L, request, user))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Note not found with id: 1");
    }

    @Test
    void deleteNote_shouldCallRepository() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        noteService.deleteNote(1L, user);
        verify(noteRepository).delete(note);
    }

    @Test
    void archiveNote_shouldSetArchivedTrue() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(noteRepository.save(note)).thenReturn(note);
        when(noteMapper.toResponse(note)).thenReturn(noteResponse);

        noteService.archiveNote(1L, user);

        assertThat(note.isArchived()).isTrue();
        verify(noteRepository).save(note);
    }

    @Test
    void unarchiveNote_shouldSetArchivedFalse() {
        note.setArchived(true);
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(noteRepository.save(note)).thenReturn(note);
        when(noteMapper.toResponse(note)).thenReturn(noteResponse);

        noteService.unarchiveNote(1L, user);

        assertThat(note.isArchived()).isFalse();
        verify(noteRepository).save(note);
    }
}
