package org.medilabo.ui.client;

import org.medilabo.ui.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class PatientClient {

    private final RestClient restClient;

    public PatientClient(@Value("${patient.api.url}") String patientApiUrl) {
        this.restClient = RestClient.builder().baseUrl(patientApiUrl).build();
    }

    public PatientDTO getPatientById(Long id) {
        return restClient.get()
                .uri("/{id}", id)
                .retrieve()
                .body(PatientDTO.class);
    }

    public List<PatientDTO> getAllPatients() {
        PatientDTO[] patients = restClient.get()
                .uri("")
                .retrieve()
                .body(PatientDTO[].class);

        return patients != null ? List.of(patients) : List.of();
    }

    public void createPatient(PatientDTO patientDTO) {
        restClient.post()
                .uri("")
                .body(patientDTO)
                .retrieve()
                .body(PatientDTO.class);
    }

    public void updatePatient(Long id, PatientDTO patientDTO) {
        restClient.put()
                .uri("/{id}", id)
                .body(patientDTO)
                .retrieve()
                .body(PatientDTO.class);
    }

    public void deletePatient(Long id) {
        restClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
