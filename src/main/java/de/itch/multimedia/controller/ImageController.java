package de.itch.multimedia.controller;

import de.itch.multimedia.db.ImageDb;
import de.itch.multimedia.db.ImmoblieDb;
import de.itch.multimedia.dtos.Image;
import de.itch.multimedia.dtos.Immobile;
import de.itch.multimedia.dtos.Termin;
import de.itch.multimedia.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
@Tag(name = "Bilder API", description = "Verwaltet die Bilder zu den Immobilien")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageDb imageDb;

    @Autowired
    private ImmoblieDb immoblieDb;


    private static void accept(Object image) {

    }

    @Operation(
            summary = "Lädt ein Bild hoch",
            description = "Akzeptiert ein Bild (JPEG, PNG, etc.) und speichert es in der Datenbank.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bild erfolgreich hochgeladen"),
                    @ApiResponse(responseCode = "400", description = "Ungültige Datei"),
                    @ApiResponse(responseCode = "404", description = "Immoblie nicht gefunden"),
                    @ApiResponse(responseCode = "500", description = "Serverfehler")
            }
    )
    @PostMapping(path = "/{immobileId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> uploadImage(@PathVariable Long immobileId,
            @RequestParam("file")
            MultipartFile file)
    {
        Optional<Immobile> immobile = immoblieDb.findById(immobileId);
        if(immobile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
            try {
               Image save = imageService.saveImage(file, immobileId);
                return ResponseEntity.ok().body(save);
            }catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
    }

    @Operation(summary = "Gibt einen Bild zurück", description = "Gibt das Bild Objekt zurück")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bild erfolgreich abgerufen",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "404", description = "Bild nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Image> getImage(@PathVariable("id") Long id) {
        try {
            Image image = imageService.getImageData(id);
            return ResponseEntity.ok()
                    .body(image);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Gibt alle Bilder einer Immoblie zurück", description = "Liefert eine Liste aller Bilder einer Immoblie in der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Liste der Bilder erfolgreich abgerufen",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "404", description = "Immoblie nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @GetMapping("/immobile/{immobileId}")
    public ResponseEntity<List<Image>> getImageImmoblieList(@PathVariable("immobileId") Long immobileId) {
            Optional<Immobile> immobile = immoblieDb.findById(immobileId);
            if(immobile.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<Image> image = imageDb.findByImmobileId(immobileId);
            return ResponseEntity.ok()
                    .body(image);

    }

    @Operation(summary = "Gibt einen Bild zurück im Content-Type", description = "Zeigt dirkt das Bild im Content-Type an")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bild wird angezeigt"),
            @ApiResponse(responseCode = "404", description = "Bild nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @GetMapping("/{id}/view")
    public ResponseEntity<byte[]> getImageView(@PathVariable Long id) {
        try {
            Image image = imageService.getImageData(id);
            byte[] imageData = image.getData();
            return ResponseEntity.ok()
                    .header("Content-Type", image.getType()) // Passe den MIME-Typ an
                    .body(imageData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Löscht einen Bild", description = "Löscht einen Bild anhand seiner Id permanent aus der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bild erfolgreich gelöscht"),
            @ApiResponse(responseCode = "404", description = "Bild nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTermin(Long id) {
        try {
            imageService.deleteImage(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
