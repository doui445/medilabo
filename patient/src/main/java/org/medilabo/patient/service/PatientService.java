package org.medilabo.patient.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.medilabo.patient.model.Patient;
import org.medilabo.patient.model.dto.PatientDTO;
import org.medilabo.patient.repository.PatientRepository;
import org.medilabo.patient.service.mapper.PatientMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Make sure default (gets) is read only
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public List<PatientDTO> getPatients() {
        return patientRepository.findAll().stream().map(patientMapper::entityToDto).toList();
    }

    public PatientDTO getPatientById(Long id) {
        return patientMapper
                .entityToDto(patientRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + id)));
    }

    @Transactional
    public PatientDTO savePatient(PatientDTO patientDTO) {
        // Check id is null
        if (patientDTO.id() != null) {
            throw new IllegalArgumentException("New patient cannot have an ID");
        }

        // Convert patientDTO to Patient with MapStruct
        Patient patient = patientMapper.dtoToEntity(patientDTO);

        // Save patient and return
        return patientMapper.entityToDto(patientRepository.save(patient));
    }

    @Transactional
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        // Check patient exists
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with ID: " + id));

        // Update patient information with MapStruct
        patientMapper.updateEntityFromDto(patientDTO, patient);

        // Save patient and return
        return patientMapper.entityToDto(patientRepository.save(patient));
    }

    @Transactional
    public void deletePatientById(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new EntityNotFoundException("Patient not found with ID: " + id);
        }
        patientRepository.deleteById(id);
    }
}
