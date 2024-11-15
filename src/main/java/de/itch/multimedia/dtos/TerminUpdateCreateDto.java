package de.itch.multimedia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TerminUpdateCreateDto {

    @Schema(description = "Titel des Termins", example = "Besichtigung")
    private String titel;

    @Schema(description = "Start des Termins", example = "2024-11-14T23:00:00")
    private LocalDateTime startTime;

    @Schema(description = "Ende des Termins", example = "2024-11-14T23:00:00")
    private LocalDateTime endTime;

}
