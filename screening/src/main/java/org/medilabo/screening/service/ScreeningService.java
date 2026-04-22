package org.medilabo.screening.service;

import lombok.RequiredArgsConstructor;
import org.medilabo.screening.client.NoteClient;
import org.medilabo.screening.client.PatientClient;
import org.medilabo.screening.dto.NoteDTO;
import org.medilabo.screening.dto.PatientDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final PatientClient patientClient;
    private final NoteClient noteClient;

    private final List<String> TRIGGERS = List.of("hémoglobine a1c", "microalbumine", "taille", "poids",
            "fume", "anormal", "cholestérol", "vertige", "rechute", "réaction", "anticorps");

    public String screenPatient(Long patientId) {
        PatientDTO patient  = patientClient.getPatient(patientId);
        List<NoteDTO> notes = noteClient.getNotes(patientId);

        int age = Period.between(patient.birthDate(), LocalDate.now()).getYears();

        String notesContent = notes.stream()
                .map(note -> note.content().toLowerCase())
                .collect(Collectors.joining(" "));

        long triggerCount = TRIGGERS.stream()
                .filter(notesContent::contains)
                .count();

        return calculateRisk(age, patient.gender(), triggerCount).name();
    }

    private RiskLevel calculateRisk(int age, String gender, long triggerCount) {
        boolean isMale = gender.equals("M");
        boolean isFemale = gender.equals("F");
        boolean passed30 = age > 30;

        if ((passed30 && triggerCount >= 8) || (!passed30 && ((isFemale && triggerCount >= 7) || (isMale && triggerCount >= 5)))) {
            return RiskLevel.EARLY_ONSET;
        }

        if ((passed30 && triggerCount >= 6) || (!passed30 && ((isFemale && triggerCount >= 4) || (isMale && triggerCount >= 3)))) {
            return RiskLevel.IN_DANGER;
        }

        if (passed30 && triggerCount >= 2) {
            return RiskLevel.BORDERLINE;
        }

        return RiskLevel.NONE;
    }

    private enum RiskLevel {
        NONE,
        BORDERLINE,
        IN_DANGER,
        EARLY_ONSET
    }
}
