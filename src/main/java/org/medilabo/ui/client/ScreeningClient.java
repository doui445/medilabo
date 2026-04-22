package org.medilabo.ui.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ScreeningClient {

    private final RestClient restClient;

    public ScreeningClient(@Value("${screening.api.url}") String screeningApiUrl) {
        this.restClient = RestClient.builder().baseUrl(screeningApiUrl).build();
    }

    public String screenPatient(Long patientId) {
        return restClient.get()
                .uri("/{patientId}", patientId)
                .retrieve()
                .body(String.class);
    }
}
