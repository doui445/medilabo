package org.medilabo.notes.service;

import lombok.RequiredArgsConstructor;
import org.medilabo.notes.controller.exception.ResourceNotFoundException;
import org.medilabo.notes.model.Note;
import org.medilabo.notes.model.dto.NoteDTO;
import org.medilabo.notes.repository.NoteRepository;
import org.medilabo.notes.service.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public List<NoteDTO> getNotes(Long patientId) {
        return noteRepository.findByPatientId(patientId).stream().map(noteMapper::noteToDto).toList();
    }

    public NoteDTO getNoteById(String id) {
        return noteMapper
                .noteToDto(noteRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Note not found with ID: " + id)));
    }

    public NoteDTO saveNote(NoteDTO noteDTO) {
        // Check id is null
        if (noteDTO.id() != null) {
            throw new IllegalArgumentException("New note cannot have an ID");
        }

        // Convert noteDTO to Note
        Note note = noteMapper.dtoToNote(noteDTO);

        // Save and return
        return  noteMapper.noteToDto(noteRepository.save(note));
    }

    public NoteDTO updateNote(String id, NoteDTO noteDTO) {
        // Check note exists
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with ID: " + id));

        // Update note
        noteMapper.updateNoteFromDto(noteDTO, note);

        // Save note and return
        return noteMapper.noteToDto(noteRepository.save(note));
    }

    public void deleteNoteById(String id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note not found with ID: " + id);
        }
        noteRepository.deleteById(id);
    }
}
