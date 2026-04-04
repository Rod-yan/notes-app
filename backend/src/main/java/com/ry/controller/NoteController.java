package com.ry.controller;

import com.ry.dto.NoteRequest;
import com.ry.dto.NoteResponse;
import com.ry.model.User;
import com.ry.repository.UserRepository;
import com.ry.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(NoteController.API_PATH)
@RequiredArgsConstructor
public class NoteController {
    public static final String API_PATH = "/api/notes";

    private final NoteService noteService;
    private final UserRepository userRepository;

    @GetMapping
    public List<NoteResponse> getNotes(
            @RequestParam(defaultValue = "false") boolean archived,
            @RequestParam(required = false) String category,
            Authentication authentication) {
        User user = getUser(authentication);
        return noteService.getNotes(archived, category, user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse createNote(@Valid @RequestBody NoteRequest request, Authentication authentication) {
        User user = getUser(authentication);
        return noteService.createNote(request, user);
    }

    @PutMapping("/{id}")
    public NoteResponse updateNote(@PathVariable Long id, @Valid @RequestBody NoteRequest request, Authentication authentication) {
        User user = getUser(authentication);
        return noteService.updateNote(id, request, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long id, Authentication authentication) {
        User user = getUser(authentication);
        noteService.deleteNote(id, user);
    }

    @PostMapping("/{id}/archive")
    public NoteResponse archiveNote(@PathVariable Long id, Authentication authentication) {
        User user = getUser(authentication);
        return noteService.archiveNote(id, user);
    }

    @PostMapping("/{id}/unarchive")
    public NoteResponse unarchiveNote(@PathVariable Long id, Authentication authentication) {
        User user = getUser(authentication);
        return noteService.unarchiveNote(id, user);
    }

    private User getUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
