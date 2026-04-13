package org.medilabo.patient.model.dto;

import jakarta.validation.constraints.*;
import org.medilabo.patient.model.Gender;

import java.time.LocalDate;

public record PatientDTO(
        Long id,

        @NotBlank(message = "Firstname is mandatory")
        String firstname,

        @NotBlank(message = "Lastname is mandatory")
        String lastname,

        @NotNull(message = "Birth date is mandatory")
        @PastOrPresent(message = "Birth date shouldn't be in the futur")
        LocalDate birthDate,

        @NotBlank(message = "Gender is mandatory")
        @Pattern(regexp = "^$|^([MF])$", message = "Gender must be 'M' or 'F'")
        String gender,

        String address,

        String phone
) {}
