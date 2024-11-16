package de.itch.multimedia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ImmoblieUpdateCreateDto {

    @Schema(description = "Die Name der Immobile.", example = "Haus am See")
    private String name;

    @Schema(description = "Preis der Immobile in Euro.", example = "150000.00")
    private Float preis;

    @Schema(description = "Adresse der Immobile.", example = "Dratelnstraße 26, 21109 Hamburg")
    private String adresse;

}
