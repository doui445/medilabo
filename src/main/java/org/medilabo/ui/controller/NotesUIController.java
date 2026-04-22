package org.medilabo.ui.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.medilabo.ui.client.NotesClient;
import org.medilabo.ui.dto.NoteDTO;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/note")
@RequiredArgsConstructor
public class NotesUIController {

    private final NotesClient notesClient;

    @PostMapping("/add")
    public String addNote(@Valid @ModelAttribute("newNote") NoteDTO newNote, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/patient/details/" + newNote.patientId();
        }

        NoteDTO noteToSave = new NoteDTO(
                null,
                newNote.patientId(),
                newNote.patientName(),
                LocalDate.now(),
                newNote.title(),
                newNote.content()
        );

        notesClient.createNote(noteToSave);
        return "redirect:/patient/details/" + newNote.patientId();
    }
}
