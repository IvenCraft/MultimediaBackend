package de.itch.multimedia.controller;

import de.itch.multimedia.db.ImmoblieDb;
import de.itch.multimedia.dtos.Immobile;
import de.itch.multimedia.dtos.ImmoblieUpdateCreateDto;
import de.itch.multimedia.dtos.Termin;
import de.itch.multimedia.dtos.TerminUpdateCreateDto;
import de.itch.multimedia.services.ImmoblieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/immoblie")
@Tag(name = "Immoblie API", description = "Verwaltet die Immobilien")
public class ImmoblieController {

    @Autowired
    private ImmoblieService immoblieService;

    @Autowired
    private ImmoblieDb immoblieDb;

    @Operation(summary = "Gibt alle Immobile zurück", description = "Liefert eine Liste aller Immobile in der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste der Immobile erfolgreich abgerufen",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Immobile.class))),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @GetMapping
    public ResponseEntity<List<Immobile>> getAllImmobile(
            @RequestParam @Nullable String name,
            @RequestParam @Nullable String addresse,
            @RequestParam @Nullable Float preis,
            @RequestParam @Nullable Boolean includeInactive) {
        List<Immobile> list = null;
        try {
            list = immoblieDb.filterList(name, addresse, preis, includeInactive);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Gibt einen Immobile zurück", description = "Liefert einen Immobile anhand seiner Id in der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Immobile erfolgreich abgerufen",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Immobile.class))),
            @ApiResponse(responseCode = "404", description = "Immobile nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Immobile> getImmobile(@PathVariable("id") Long id) {
        return immoblieDb.findById(id)
                .map(immobile -> ResponseEntity.ok().body(immobile))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Erstellt eine neue Immobile", description = "Fügt einen neuen Immobile hinzu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Immobile erfolgreich erstellt",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Immobile.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @PostMapping
    public ResponseEntity<Immobile> createImmobile(@RequestBody ImmoblieUpdateCreateDto immobile) {
        try {
            return ResponseEntity.ok().body(immoblieService.createImmoblie(immobile));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Updated eine Immobile", description = "Updated eine Immobile anhand seiner Id in der Datenbank und gibt ihn zurück.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Immobile erfolgreich geändert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Immobile.class))),
            @ApiResponse(responseCode = "404", description = "Immobile nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Immobile> updateImmobile(@PathVariable Long id, @RequestBody ImmoblieUpdateCreateDto createDto) {
        Immobile existingImmobile = immoblieDb.findById(id).orElse(null);
        if (existingImmobile == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            Immobile immobile = immoblieService.updateImmoblie(createDto, existingImmobile);
            return ResponseEntity.ok().body(immobile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Löscht eine Immobile", description = "Setzt den Aktive Status eine Immoblie auf false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Termin erfolgreich gelöscht"),
            @ApiResponse(responseCode = "404", description = "Termin nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTermin(Long id) {
        Immobile existingImmobile = immoblieDb.findById(id).orElse(null);
        if (existingImmobile == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            existingImmobile.setAktiv(false);
            immoblieDb.save(existingImmobile);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Reaktivieren eine Immobile", description = "Setzt den Aktive Status eine Immoblie auf true wenn er vorher auf false war.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Immobile erfolgreich geändert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Immobile.class))),
            @ApiResponse(responseCode = "400", description = "Immobile ist nicht inaktive"),
            @ApiResponse(responseCode = "404", description = "Immobile nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<Immobile> reactivateImmobile(@PathVariable Long id) {
        Immobile existingImmobile = immoblieDb.findByIdWithInactive(id).orElse(null);
        if (existingImmobile == null) {
            return ResponseEntity.notFound().build();
        }
        if(existingImmobile.isAktiv()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            existingImmobile.setAktiv(true);
            immoblieDb.save(existingImmobile);
            return ResponseEntity.ok().body(existingImmobile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
