package org.medilabo.notes.model.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record NoteDTO(
        String id,

        @NotNull(message = "Patient Id is mandatory")
        Long patientId,

        @NotBlank(message = "Patient Name is mandatory")
        String patientName,

        @NotNull(message = "Date is mandatory")
        @PastOrPresent(message = "Date shouldn't be in the futur")
        LocalDate date,

        String title,

        @NotBlank(message = "Content is mandatory")
        String content
) {}
