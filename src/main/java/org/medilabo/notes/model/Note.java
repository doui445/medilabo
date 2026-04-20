package org.medilabo.notes.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {

    @Id
    private String id;

    private Long patientId;

    private String patientName;

    private LocalDate date;

    private String title;

    private String content;
}
