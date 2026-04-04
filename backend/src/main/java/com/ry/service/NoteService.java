package com.ry.service;

import com.ry.dto.NoteRequest;
import com.ry.dto.NoteResponse;
import com.ry.exception.ResourceNotFoundException;
import com.ry.mapper.NoteMapper;
import com.ry.model.Category;
import com.ry.model.Note;
import com.ry.model.User;
import com.ry.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private static final String NOT_FOUND_MSG = "Note not found with id: ";

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final CategoryService categoryService;

    public List<NoteResponse> getNotes(boolean archived, String categoryName, User user) {
        List<Note> notes = noteRepository.findByArchivedAndCategoryNameAndUser(archived, categoryName, user);
        return noteMapper.toResponseList(notes);
    }

    @Transactional
    public NoteResponse createNote(NoteRequest request, User user) {
        Note note = noteMapper.toEntity(request);
        note.setUser(user);
        updateCategories(note, request.getCategoryNames(), user);
        return noteMapper.toResponse(noteRepository.save(note));
    }

    @Transactional
    public NoteResponse updateNote(Long id, NoteRequest request, User user) {
        Note note = noteRepository.findById(id)
                .filter(n -> n.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));
        noteMapper.updateEntity(request, note);
        updateCategories(note, request.getCategoryNames(), user);
        return noteMapper.toResponse(noteRepository.save(note));
    }

    public void deleteNote(Long id, User user) {
        Note note = noteRepository.findById(id)
                .filter(n -> n.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));
        noteRepository.delete(note);
    }

    @Transactional
    public NoteResponse archiveNote(Long id, User user) {
        Note note = noteRepository.findById(id)
                .filter(n -> n.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));
        note.setArchived(true);
        return noteMapper.toResponse(noteRepository.save(note));
    }

    @Transactional
    public NoteResponse unarchiveNote(Long id, User user) {
        Note note = noteRepository.findById(id)
                .filter(n -> n.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_MSG + id));
        note.setArchived(false);
        return noteMapper.toResponse(noteRepository.save(note));
    }

    private void updateCategories(Note note, Set<String> categoryNames, User user) {
        if (categoryNames != null) {
            Set<Category> categories = categoryNames.stream()
                    .map(name -> categoryService.getOrCreate(name, user))
                    .collect(Collectors.toSet());
            note.setCategories(categories);
        }
    }
}
