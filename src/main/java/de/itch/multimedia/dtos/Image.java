package de.itch.multimedia.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Die eindeutige ID des Bildes.", example = "1")
    private Long id;

    @Schema(description = "Die ID des zugehörigen Immobilienobjekts.", example = "12345")
    private Long immobile;

    @Schema(description = "Der Name des Bildes, z.B. 'bild123.jpg'.", example = "bild123.jpg")
    private String name;

    @Schema(description = "Der MIME-Typ des Bildes, z.B. 'image/jpeg'.", example = "image/jpeg")
    private String type;

    @Lob
    @Schema(description = "Die Bilddaten als binäre Datei.", type = "string", format = "binary")
    private byte[] data;
}
