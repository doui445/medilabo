package org.medilabo.screening.client;

import org.medilabo.screening.dto.NoteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "note-client", url = "${notes.api.url}")
public interface NoteClient {

    @GetMapping("/patient/{patientId}")
    List<NoteDTO> getNotes(@PathVariable("patientId") Long patientId);
}
