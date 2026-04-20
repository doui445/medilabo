package org.medilabo.notes.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.medilabo.notes.controller.exception.ResourceNotFoundException;
import org.medilabo.notes.model.Note;
import org.medilabo.notes.model.dto.NoteDTO;
import org.medilabo.notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NoteService noteService;

    NoteDTO noteDTO;

    @BeforeEach
    void setUp() {
        noteDTO = new NoteDTO(
                "1",
                1L,
                "John West",
                LocalDate.now(),
                "Tests results",
                "blablabla");
    }

    @Test
    @DisplayName("Create Note - Success")
    void givenNoteDTO_whenCreateNote_thenReturnCreatedNote() throws Exception {
        given(noteService.saveNote(any(NoteDTO.class))).willReturn(noteDTO);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientName").value("John West"))
                .andExpect(jsonPath("$.content").value("blablabla"));
    }

    @Test
    @DisplayName("Get All Patient Notes - Success")
    void givenPatient_whenGetNotes_thenReturnPatientNoteList() throws Exception {
        given(noteService.getNotes(1L)).willReturn(List.of(noteDTO));

        mockMvc.perform(get("/api/notes/patient/" + noteDTO.patientId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].patientName").value("John West"));
    }

    @Test
    @DisplayName("Get Note By Id - Success")
    void givenNoteId_whenGetNoteById_thenReturnNote() throws Exception {
        given(noteService.getNoteById(noteDTO.id())).willReturn(noteDTO);

        mockMvc.perform(get("/api/notes/" + noteDTO.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(noteDTO.id()))
                .andExpect(jsonPath("$.patientName").value("John West"));
    }

    @Test
    @DisplayName("Get Note By Id - Not Found")
    void givenNonExistingNoteId_whenGetNoteById_thenReturnNotFound() throws Exception {
        given(noteService.getNoteById(noteDTO.id())).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/api/notes/" + noteDTO.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update Note - Success")
    void givenNote_whenUpdateNote_thenReturnUpdatedNote() throws Exception {
        given(noteService.updateNote(any(String.class), any(NoteDTO.class))).willReturn(noteDTO);

        mockMvc.perform(put("/api/notes/" + noteDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(noteDTO.id()))
                .andExpect(jsonPath("$.patientName").value("John West"));
    }

    @Test
    @DisplayName("Update Note - Not Found")
    void givenNonExistingNoteId_whenUpdateNote_thenReturnNotFound() throws Exception {
        given(noteService.updateNote(any(String.class), any(NoteDTO.class))).willThrow(ResourceNotFoundException.class);

        mockMvc.perform(put("/api/notes/" + noteDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete Note - Success")
    void givenNoteId_whenDeleteNote_thenReturnNoContent() throws Exception {
        willDoNothing().given(noteService).deleteNoteById(noteDTO.id());

        mockMvc.perform(delete("/api/notes/" + noteDTO.id()))
                .andExpect(status().isNoContent());

        verify(noteService, times(1)).deleteNoteById(noteDTO.id());
    }
}