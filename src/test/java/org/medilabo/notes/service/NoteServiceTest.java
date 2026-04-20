package org.medilabo.notes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.notes.controller.exception.ResourceNotFoundException;
import org.medilabo.notes.model.Note;
import org.medilabo.notes.model.dto.NoteDTO;
import org.medilabo.notes.repository.NoteRepository;
import org.medilabo.notes.service.mapper.NoteMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteService noteService;

    private Note note;

    private NoteDTO noteDTO;

    @BeforeEach
    void setUp() {
        note = Note.builder()
                .id("1")
                .patientId(1L)
                .patientName("test")
                .date(LocalDate.now())
                .title("Test")
                .content("test test test")
                .build();

        noteDTO = new NoteDTO(
                null,
                note.getPatientId(),
                note.getPatientName(),
                note.getDate(),
                note.getTitle(),
                note.getContent());
    }

    @Test
    @DisplayName("getNoteById should return a note when note exists")
    void testGetNoteByIdSuccess() {
        given(noteRepository.findById("1")).willReturn(Optional.of(note));
        given(noteMapper.noteToDto(note)).willReturn(noteDTO);

        NoteDTO result = noteService.getNoteById("1");

        assertThat(result).isNotNull();
        assertThat(result.patientName()).isEqualTo("test");
        verify(noteRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("getNoteById should throw not found exception when note does not exists")
    void testGetNoteByIdNotFound() {
        given(noteRepository.findById("2")).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> noteService.getNoteById("2"));

        assertThat(exception.getMessage()).isEqualTo("Note not found with ID: 2");
        verifyNoInteractions(noteMapper);
    }

    @Test
    @DisplayName("saveNote should save and return the note")
    void testSaveNoteSuccess() {
        given(noteMapper.dtoToNote(noteDTO)).willReturn(note);
        given(noteRepository.save(note)).willReturn(note);
        given(noteMapper.noteToDto(note)).willReturn(noteDTO);

        NoteDTO result = noteService.saveNote(noteDTO);

        assertThat(result).isNotNull();
        assertThat(result.patientName()).isEqualTo("test");
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    @DisplayName("saveNote should throw illegal argument exception when id is already set")
    void testSaveNoteIllegalArgument() {
        NoteDTO invalidNoteDTO = new NoteDTO("1", 1L, "Zoe", LocalDate.now(), "new test",  "new test");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> noteService.saveNote(invalidNoteDTO));

        assertThat(exception.getMessage()).isEqualTo("New note cannot have an ID");
        verifyNoInteractions(noteMapper);
    }

    @Test
    @DisplayName("updateNote should update and return the note")
    void testUpdateNoteSuccess() {
        given(noteRepository.findById("1")).willReturn(Optional.of(note));
        given(noteRepository.save(note)).willReturn(note);
        given(noteMapper.noteToDto(note)).willReturn(noteDTO);

        NoteDTO result = noteService.updateNote("1", noteDTO);

        assertThat(result).isNotNull();
        assertThat(result.patientName()).isEqualTo("test");
        verify(noteRepository, times(1)).save(note);
    }

    @Test
    @DisplayName("updateNote should throw not found exception when note does not exist")
    void testUpdateNoteNotFound() {
        given(noteRepository.findById("1")).willReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> noteService.updateNote("1", noteDTO));

        assertThat(exception.getMessage()).isEqualTo("Note not found with ID: 1");
        verify(noteRepository, times(0)).save(any());
        verifyNoInteractions(noteMapper);
    }

    @Test
    @DisplayName("deleteNoteById should delete the note")
    void testDeleteNoteByIdSuccess() {
        given(noteRepository.existsById("1")).willReturn(true);

        noteService.deleteNoteById("1");

        verify(noteRepository, times(1)).deleteById("1");
    }

    @Test
    @DisplayName("deleteNoteById should throw not found exception when note does not exist")
    void testDeleteNoteByIdNotFound() {
        given(noteRepository.existsById("1")).willReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> noteService.deleteNoteById("1"));

        assertThat(exception.getMessage()).isEqualTo("Note not found with ID: 1");
        verify(noteRepository, times(0)).deleteById("1");
    }
}