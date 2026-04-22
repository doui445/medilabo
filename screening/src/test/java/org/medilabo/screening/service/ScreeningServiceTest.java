package org.medilabo.screening.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.medilabo.screening.client.NoteClient;
import org.medilabo.screening.client.PatientClient;
import org.medilabo.screening.dto.NoteDTO;
import org.medilabo.screening.dto.PatientDTO;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    @Mock
    private PatientClient patientClient;

    @Mock
    private NoteClient noteClient;

    @InjectMocks
    private ScreeningService screeningService;

    private PatientDTO patient;

    private NoteDTO note;

    @BeforeEach
    void setUp() {
        patient = new PatientDTO(1L, "Test", "Test", LocalDate.now(), "M", "", "");
    }

    @Test
    @DisplayName("screenPatient should return 'NONE' when patient is clean")
    void testScreenPatientNone() {
        patient = new PatientDTO(1L, "TestNone", "Test", LocalDate.of(1966, 12, 31), "F", "1 Brookside St", "100-222-3333");
        String noteContent = "Le patient déclare qu'il 'se sent très bien' Poids égal ou inférieur au poids recommandé";
        note = new NoteDTO("1", 1L, "", LocalDate.now(), "", noteContent);

        given(patientClient.getPatient(1L)).willReturn(patient);
        given(noteClient.getNotes(1L)).willReturn(Collections.singletonList(note));

        String result = screeningService.screenPatient(1L);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("NONE");
    }

    @Test
    @DisplayName("screenPatient should return 'Borderline' when patient is borderline")
    void testScreenPatientBorderline() {
        patient = new PatientDTO(1L, "TestBorderline", "Test", LocalDate.of(1945, 6, 24), "M", "2 High St", "200-333-4444");
        String noteContent = "Le patient déclare qu'il ressent beaucoup de stress au travail Il se plaint également que son audition est anormale dernièrement\n" +
                "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois Il remarque également que son audition continue d'être anormale";
        note = new NoteDTO("1", 1L, "", LocalDate.now(), "", noteContent);

        given(patientClient.getPatient(1L)).willReturn(patient);
        given(noteClient.getNotes(1L)).willReturn(Collections.singletonList(note));

        String result = screeningService.screenPatient(1L);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("BORDERLINE");
    }

    @Test
    @DisplayName("screenPatient should return 'IN_DANGER' when patient is in danger")
    void testScreenPatientInDanger() {
        patient = new PatientDTO(1L, "TestInDanger", "Test", LocalDate.of(2004, 6, 18), "M", "3 Club Road", "300-444-5555");
        String noteContent = "Le patient déclare qu'il fume depuis peu\n" +
                "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé";
        note = new NoteDTO("1", 1L, "", LocalDate.now(), "", noteContent);

        given(patientClient.getPatient(1L)).willReturn(patient);
        given(noteClient.getNotes(1L)).willReturn(Collections.singletonList(note));

        String result = screeningService.screenPatient(1L);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("IN_DANGER");
    }

    @Test
    @DisplayName("screenPatient should return 'EARLY_ONSET' when patient is early onset")
    void testScreenPatientEarlyOnset() {
        patient = new PatientDTO(1L, "TestEarlyOnset", "Test", LocalDate.of(2002, 6, 28), "F", "4 Valley Dr", "400-555-6666");
        String noteContent = """
                Le patient déclare qu'il lui est devenu difficile de monter les escaliers Il se plaint également d’être essoufflé Tests de laboratoire indiquant que les anticorps sont élevés Réaction aux médicaments
                Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps
                Le patient déclare avoir commencé à fumer depuis peu Hémoglobine A1C supérieure au niveau recommandé
                Taille, Poids, Cholestérol, Vertige et Réaction""";
        note = new NoteDTO("1", 1L, "", LocalDate.now(), "", noteContent);

        given(patientClient.getPatient(1L)).willReturn(patient);
        given(noteClient.getNotes(1L)).willReturn(Collections.singletonList(note));

        String result = screeningService.screenPatient(1L);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("EARLY_ONSET");
    }
}