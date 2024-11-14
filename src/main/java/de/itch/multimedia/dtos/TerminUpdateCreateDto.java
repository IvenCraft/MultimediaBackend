package de.itch.multimedia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

public class TerminUpdateCreateDto {

    @Schema(description = "Titel des Termins", example = "Besichtigung")
    private String titel;

    @Schema(description = "Start des Termins", example = "Besichtigung")
    private Timestamp startTime;

    @Schema(description = "Ende des Termins", example = "Besichtigung")
    private Timestamp endTime;

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

}
