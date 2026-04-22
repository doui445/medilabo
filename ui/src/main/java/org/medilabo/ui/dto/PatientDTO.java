package org.medilabo.ui.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record PatientDTO(
        Long id,

        @NotBlank(message = "Firstname is mandatory")
        String firstname,

        @NotBlank(message = "Lastname is mandatory")
        String lastname,

        @NotNull(message = "Birth date is mandatory")
        @PastOrPresent(message = "Birth date shouldn't be in the futur")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate,

        @NotBlank(message = "Gender is mandatory")
        @Pattern(regexp = "^$|^([MF])$", message = "Gender must be 'M' or 'F'")
        String gender,

        String address,

        String phone
) {}
