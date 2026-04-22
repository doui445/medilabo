package org.medilabo.screening.controller;

import lombok.RequiredArgsConstructor;
import org.medilabo.screening.service.ScreeningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/screening")
@RequiredArgsConstructor
public class ScreeningController {

    private final ScreeningService screeningService;

    @GetMapping("/{patientId}")
    public ResponseEntity<String> screenPatient(@PathVariable("patientId") Long patientId) {
        return ResponseEntity.ok(screeningService.screenPatient(patientId));
    }
}
