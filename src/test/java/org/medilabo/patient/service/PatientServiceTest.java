package org.medilabo.patient.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.patient.model.Gender;
import org.medilabo.patient.model.Patient;
import org.medilabo.patient.model.dto.PatientDTO;
import org.medilabo.patient.repository.PatientRepository;
import org.medilabo.patient.service.mapper.PatientMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
                .id(1L)
                .firstname("test")
                .lastname("test")
                .birthDate(LocalDate.now())
                .gender(Gender.M)
                .address("123 Test")
                .phone("0123456789")
                .build();

        patientDTO = new PatientDTO(
                null,
                patient.getFirstname(),
                patient.getLastname(),
                patient.getBirthDate(),
                patient.getGender().name(),
                patient.getAddress(),
                patient.getPhone());
    }

    @Test
    @DisplayName("getPatientById should return a patient when patient exists")
    void testGetPatientByIdFound() {
        given(patientRepository.findById(1L)).willReturn(Optional.of(patient));
        given(patientMapper.entityToDto(patient)).willReturn(patientDTO);

        PatientDTO result = patientService.getPatientById(1L);

        assertThat(result).isNotNull();
        assertThat(result.firstname()).isEqualTo("test");
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getPatientById should throw not found exception when patient does not exist")
    void testGetPatientByIdNotFound() {
        given(patientRepository.findById(2L)).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> patientService.getPatientById(2L));

        assertThat(exception.getMessage()).isEqualTo("Patient not found with id: 2");
        verifyNoInteractions(patientMapper);
    }

    @Test
    @DisplayName("savePatient should save and return the patient")
    void testSavePatientSuccess() {
        given(patientMapper.dtoToEntity(patientDTO)).willReturn(patient);
        given(patientRepository.save(patient)).willReturn(patient);
        given(patientMapper.entityToDto(patient)).willReturn(patientDTO);

        PatientDTO result = patientService.savePatient(patientDTO);

        assertThat(result).isNotNull();
        assertThat(result.firstname()).isEqualTo("test");
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    @DisplayName("savePatient should throw illegal argument exception when id is already set")
    void testSavePatientIllegalArgument() {
        PatientDTO invalidPatientDTO = new PatientDTO(1L, "Zoe", "Carter", LocalDate.now(), "F", null, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> patientService.savePatient(invalidPatientDTO));

        assertThat(exception.getMessage()).isEqualTo("New patient cannot have an ID");
        verifyNoInteractions(patientRepository);
    }

    @Test
    @DisplayName("updatePatient should update and return the patient")
    void testUpdatePatientSuccess() {
        given(patientRepository.findById(1L)).willReturn(Optional.of(patient));
        given(patientRepository.save(patient)).willReturn(patient);
        given(patientMapper.entityToDto(patient)).willReturn(patientDTO);

        PatientDTO result = patientService.updatePatient(1L, patientDTO);

        assertThat(result).isNotNull();
        assertThat(result.firstname()).isEqualTo("test");
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    @DisplayName("updatePatient should throw not found exception when patient does not exist")
    void testUpdatePatientNotFound() {
        given(patientRepository.findById(1L)).willReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> patientService.updatePatient(1L, patientDTO));

        assertThat(exception.getMessage()).isEqualTo("Patient not found with id: 1");
        verify(patientRepository, times(0)).save(any());
        verifyNoInteractions(patientMapper);
    }

    @Test
    @DisplayName("deletePatientById should delete the patient")
    void testDeletePatientByIdExists() {
        given(patientRepository.existsById(1L)).willReturn(true);

        patientService.deletePatientById(1L);

        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deletePatientById should throw not found exception when patient does not exist")
    void testDeletePatientById() {
        given(patientRepository.existsById(1L)).willReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> patientService.deletePatientById(1L));

        assertThat(exception.getMessage()).isEqualTo("Patient not found with id: 1");
        verify(patientRepository, times(0)).deleteById(1L);
    }
}