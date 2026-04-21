package org.medilabo.screening.client;

import org.medilabo.screening.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "note-client", url = "http://localhost:8082/api/notes/patient")
public interface NoteClient {

    @GetMapping("/{patientId}")
    List<NoteDTO> getNotes(@PathVariable("patientId") Long patientId);
}
