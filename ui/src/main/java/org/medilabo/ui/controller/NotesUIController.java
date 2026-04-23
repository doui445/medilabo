package org.medilabo.ui.controller;

import lombok.RequiredArgsConstructor;
import org.medilabo.ui.client.NotesClient;
import org.medilabo.ui.dto.NoteDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/note")
@RequiredArgsConstructor
public class NotesUIController {

    private final NotesClient notesClient;

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("note", notesClient.getNoteById(id));
        return "note/update";
    }

    @PostMapping("/add")
    public String addNote(@ModelAttribute("newNote") NoteDTO newNote, RedirectAttributes redirectAttributes) {
        if (newNote.content() == null || newNote.content().isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Doctor's Notes content is mandatory.");
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
        redirectAttributes.addFlashAttribute("successMessage", "Note added successfully!");
        return "redirect:/patient/details/" + newNote.patientId();
    }

    @PostMapping("/update/{id}")
    public String updateNote(@PathVariable("id") String id, @ModelAttribute("note") NoteDTO note, RedirectAttributes redirectAttributes) {
        if (note.content() == null || note.content().isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete note content.");
            return "note/update";
        }
        NoteDTO oldNote = notesClient.getNoteById(id);
        NoteDTO updatedNote = new NoteDTO(
                oldNote.id(),
                oldNote.patientId(),
                oldNote.patientName(),
                oldNote.date(),
                note.title(),
                note.content()
        );
        notesClient.updateNote(id, updatedNote);
        redirectAttributes.addFlashAttribute("successMessage", "Note updated successfully!");
        return "redirect:/patient/details/" + updatedNote.patientId();
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        notesClient.deleteNote(id);
        redirectAttributes.addFlashAttribute("successMessage", "Note deleted successfully!");
        return "redirect:/patient/details/" + notesClient.getNoteById(id).patientId();
    }
}
