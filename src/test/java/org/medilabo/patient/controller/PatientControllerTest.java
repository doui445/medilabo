package org.medilabo.patient.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.medilabo.patient.model.dto.PatientDTO;
import org.medilabo.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For serialize/deserialize JSON

    @MockitoBean
    private PatientService patientService;

    PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        patientDTO = new PatientDTO(
                1L,
                "John",
                "West",
                LocalDate.now(),
                "M",
                "123 John",
                "0123456789");
    }

    @Test
    @DisplayName("Create Patient - Success")
    void givenPatientDTO_whenCreatePatient_thenReturnCreatedPatient() throws Exception {
        given(patientService.savePatient(any(PatientDTO.class))).willReturn(patientDTO);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.gender").value("M"));
    }

    @Test
    @DisplayName("Get All Patients - Success")
    void givenPatients_whenGetPatients_thenReturnPatientList() throws Exception {
        given(patientService.getPatients()).willReturn(List.of(patientDTO));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstname").value("John"));
    }

    @Test
    @DisplayName("Get Patient By Id - Success")
    void givenPatientId_whenGetPatientById_thenReturnPatient() throws Exception {
        given(patientService.getPatientById(patientDTO.id())).willReturn(patientDTO);

        mockMvc.perform(get("/api/patients/" + patientDTO.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientDTO.id()))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.gender").value("M"));
    }

    @Test
    @DisplayName("Get Patient By Id - Not Found")
    void givenNonExistingPatientId_whenGetPatientById_thenReturnNotFound() throws Exception {
        given(patientService.getPatientById(patientDTO.id())).willThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/patients/" + patientDTO.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update Patient - Success")
    void givenPatient_whenUpdatePatient_thenReturnUpdatedPatient() throws Exception {
        given(patientService.updatePatient(any(Long.class), any(PatientDTO.class))).willReturn(patientDTO);

        mockMvc.perform(put("/api/patients/" + patientDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientDTO.id()))
                .andExpect(jsonPath("$.firstname").value("John"))
                .andExpect(jsonPath("$.gender").value("M"));
    }

    @Test
    @DisplayName("Update Patient - Not Found")
    void givenNonExistingPatientId_whenUpdatePatient_thenReturnNotFound() throws Exception {
        given(patientService.updatePatient(any(Long.class), any(PatientDTO.class))).willThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/api/patients/" + patientDTO.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete Patient - Success")
    void givenPatientId_whenDeletePatient_thenReturnNoContent() throws Exception {
        willDoNothing().given(patientService).deletePatientById(patientDTO.id());

        mockMvc.perform(delete("/api/patients/" + patientDTO.id()))
                .andExpect(status().isNoContent());

        verify(patientService, times(1)).deletePatientById(patientDTO.id());
    }
}