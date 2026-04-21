package org.medilabo.screening.client;

import org.medilabo.screening.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "patient-client", url = "http://localhost:8081/api/patients")
public interface PatientClient {

    @GetMapping("/{id}")
    PatientDTO getPatient(@PathVariable("id") Long id);
}
