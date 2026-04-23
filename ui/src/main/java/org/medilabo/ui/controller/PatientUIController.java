package org.medilabo.ui.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.medilabo.ui.client.NotesClient;
import org.medilabo.ui.client.PatientClient;
import org.medilabo.ui.client.ScreeningClient;
import org.medilabo.ui.dto.NoteDTO;
import org.medilabo.ui.dto.PatientDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientUIController {

    private final PatientClient patientClient;
    private final NotesClient notesClient;
    private final ScreeningClient screeningClient;

    @GetMapping("/list")
    public String showPatientsPage(Model model) {
        model.addAttribute("patients", patientClient.getAllPatients());
        return "patient/list";
    }

    @GetMapping("/details/{id}")
    public String showPatientDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("patient", patientClient.getPatientById(id));
        model.addAttribute("screening", screeningClient.screenPatient(id));
        List<NoteDTO> notes = notesClient.getNotes(id);
        System.out.println("-----> NOMBRE DE NOTES RÉCUPÉRÉES : " + notes.size());
        model.addAttribute("notes", notes);
        model.addAttribute("newNote", new NoteDTO(null, id, "", null, "", ""));
        return "patient/details";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new PatientDTO(null, "", "", null, "", "", ""));
        return "patient/add";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        PatientDTO patient = patientClient.getPatientById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("id", id);
        return "patient/update";
    }

    @PostMapping("/validate")
    public String validatePatient(@Valid @ModelAttribute("patient") PatientDTO patient, BindingResult result) {
        if (!result.hasErrors()) {
            patientClient.createPatient(patient);
            return "redirect:/patient/list";
        }
        return "patient/add";
    }

    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable("id") Long id, @Valid @ModelAttribute("patient") PatientDTO patient, BindingResult result) {
        if (!result.hasErrors()) {
            patientClient.updatePatient(id, patient);
            return "redirect:/patient/list";
        }
        return "patient/update";
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable("id") Long id) {
        patientClient.deletePatient(id);
        return "redirect:/patient/list";
    }
}
