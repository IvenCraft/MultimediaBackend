package de.itch.multimedia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Termin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Die eindeutige ID des Termins.", example = "1")
    private Long id;

    @Schema(description = "Titel des Termins", example = "Besichtigung")
    private String titel;

    @Schema(description = "Start des Termins", example = "2024-11-14T23:00:00")
    private LocalDateTime startTime;

    @Schema(description = "Ende des Termins", example = "2024-11-14T23:00:00")
    private LocalDateTime endTime;

}
