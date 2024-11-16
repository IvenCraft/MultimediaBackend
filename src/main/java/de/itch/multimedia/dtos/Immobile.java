package de.itch.multimedia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Immobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Die eindeutige ID der Immobile.", example = "1")
    private Long id;

    @Schema(description = "Die Name der Immobile.", example = "Haus am See")
    private String name;

    @Schema(description = "Preis der Immobile in Euro.", example = "150000.00")
    private Float preis;

    @Schema(description = "Adresse der Immobile.", example = "Dratelnstraße 26, 21109 Hamburg")
    private String adresse;

    @Schema(description = "Ob die Immobile aktuell verkauft wird", example = "true")
    private boolean isAktiv;
}
