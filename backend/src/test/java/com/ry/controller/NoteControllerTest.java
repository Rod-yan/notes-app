package com.ry.controller;

import com.ry.dto.NoteRequest;
import com.ry.dto.NoteResponse;
import com.ry.model.User;
import com.ry.repository.UserRepository;
import com.ry.service.NoteService;
import com.ry.service.CustomUserDetailsService;
import com.ry.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "pass");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
    }

    @Test
    @WithMockUser(username = "user")
    void getNotes_shouldReturnList() throws Exception {
        NoteResponse response = new NoteResponse();
        response.setId(1L);
        when(noteService.getNotes(anyBoolean(), any(), eq(user))).thenReturn(List.of(response));

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(username = "user")
    void createNote_shouldReturnCreated() throws Exception {
        NoteRequest request = new NoteRequest();
        request.setTitle("New Note");
        request.setContent("New Content");
        NoteResponse response = new NoteResponse();
        response.setId(1L);
        when(noteService.createNote(any(NoteRequest.class), eq(user))).thenReturn(response);

        mockMvc.perform(post("/api/notes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "user")
    void updateNote_shouldReturnOk() throws Exception {
        NoteRequest request = new NoteRequest();
        request.setTitle("Updated");
        request.setContent("Updated Content");
        NoteResponse response = new NoteResponse();
        response.setId(1L);
        when(noteService.updateNote(eq(1L), any(NoteRequest.class), eq(user))).thenReturn(response);

        mockMvc.perform(put("/api/notes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "user")
    void deleteNote_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/notes/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user")
    void archiveNote_shouldReturnOk() throws Exception {
        NoteResponse response = new NoteResponse();
        response.setId(1L);
        response.setArchived(true);
        when(noteService.archiveNote(eq(1L), eq(user))).thenReturn(response);

        mockMvc.perform(post("/api/notes/1/archive")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.archived").value(true));
    }

    @Test
    @WithMockUser(username = "user")
    void unarchiveNote_shouldReturnOk() throws Exception {
        NoteResponse response = new NoteResponse();
        response.setId(1L);
        response.setArchived(false);
        when(noteService.unarchiveNote(eq(1L), eq(user))).thenReturn(response);

        mockMvc.perform(post("/api/notes/1/unarchive")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.archived").value(false));
    }
}
