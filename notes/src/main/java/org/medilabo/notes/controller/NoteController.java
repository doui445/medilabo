package org.medilabo.notes.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.medilabo.notes.model.dto.NoteDTO;
import org.medilabo.notes.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    /**
     * Create - Add a new note
     *
     * @param note An object NoteDTO
     * @return A ResponseEntity containing the NoteDTO object saved
     */
    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteDTO note) {
        System.out.println("################################## aaaaaaaaaaaaaaaaaaaaa");
        System.out.println("Creating note: " + note.patientId());
        NoteDTO savedNote = noteService.saveNote(note);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedNote.id()).toUri();
        return ResponseEntity.created(location).body(savedNote);
    }

    /**
     * Read - Get all notes from a patient
     * @param patientId The id of the patient
     * @return - A ResponseEntity containing a List object of NoteDTO fulfilled
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NoteDTO>> getNotes(@PathVariable Long patientId) {
        return ResponseEntity.ok(noteService.getNotes(patientId));
    }

    /**
     * Read - Get one note
     * @param id The id of the note
     * @return A ResponseEntity containing a NoteDTO object fulfilled
     */
    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable String id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    /**
     * Update - Update an existing note
     * @param id - The id of the note to update
     * @param noteDetails - The note object updated
     * @return A ResponseEntity containing the note object updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable String id, @Valid @RequestBody NoteDTO noteDetails) {
        return ResponseEntity.ok(noteService.updateNote(id, noteDetails));
    }

    /**
     * Delete - Delete a note
     * @param id - The id of the note to delete
     * @return An empty ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.noContent().build();
    }
}
