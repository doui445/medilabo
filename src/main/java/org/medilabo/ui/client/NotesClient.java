package org.medilabo.ui.client;

import org.medilabo.ui.dto.NoteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class NotesClient {
    
    private final RestClient restClient;
    
    public NotesClient(@Value("${notes.api.url}") String notesApiUrl) {
        this.restClient = RestClient.builder().baseUrl(notesApiUrl).build();
    }

    public NoteDTO getNoteById(String id) {
        return restClient.get()
                .uri("/{id}", id)
                .retrieve()
                .body(NoteDTO.class);
    }

    public List<NoteDTO> getNotes(Long patientId) {
        NoteDTO[] notes = restClient.get()
                .uri("/patient/{patientId}",  patientId)
                .retrieve()
                .body(NoteDTO[].class);

        return notes != null ? List.of(notes) : List.of();
    }

    public void createNote(NoteDTO note) {
        restClient.post()
                .uri("")
                .body(note)
                .retrieve()
                .body(NoteDTO.class);
    }

    public void updateNote(String id, NoteDTO note) {
        restClient.put()
                .uri("/{id}", id)
                .body(note)
                .retrieve()
                .body(NoteDTO.class);
    }

    public void deleteNote(String id) {
        restClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
