package org.medilabo.ui.controller;

import lombok.RequiredArgsConstructor;
import org.medilabo.ui.client.PatientClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientUIController {

    private final PatientClient patientClient;

    @GetMapping("/list")
    public String patientsPage(Model model) {
        model.addAttribute("patients", patientClient.getAllPatients());
        return "patient/list";
    }
}
