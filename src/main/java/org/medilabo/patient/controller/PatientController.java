package org.medilabo.patient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.medilabo.patient.model.dto.PatientDTO;
import org.medilabo.patient.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /**
     * Create - Add a new patient
     *
     * @param patient An object PatientDTO
     * @return A ResponseEntity containing the PatientDTO object saved
     */
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patient) {
        PatientDTO savedPatient = patientService.savePatient(patient);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPatient.id()).toUri();
        return ResponseEntity.created(location).body(savedPatient);
    }

    /**
     * Read - Get all patients
     * @return - A ResponseEntity containing a List object of PatientDTO fulfilled
     */
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
        return ResponseEntity.ok(patientService.getPatients());
    }

    /**
     * Read - Get one patient
     * @param id The id of the patient
     * @return A ResponseEntity containing a PatientDTO object fulfilled
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }


    /**
     * Update - Update an existing patient
     * @param id - The id of the patient to update
     * @param patientDetails - The patient object updated
     * @return A ResponseEntity containing the patient object updated
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDetails) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientDetails));
    }

    /**
     * Delete - Delete a patient
     * @param id - The id of the patient to delete
     * @return An empty ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatientById(id);
        return ResponseEntity.noContent().build();
    }
}
