package de.itch.multimedia.controller;

import de.itch.multimedia.db.TerminDb;
import de.itch.multimedia.dtos.Termin;
import de.itch.multimedia.dtos.TerminUpdateCreateDto;
import de.itch.multimedia.services.TerminSearchService;
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
@RequestMapping("/api/termin")
@Tag(name = "Termine API", description = "Verwaltet alle Termin die Angezeigt werden")
public class TerminController {

    @Autowired
    private TerminDb terminDb;

    @Autowired
    private TerminSearchService terminSearchService;

    @Operation(summary = "Gibt alle Termine zurück", description = "Liefert eine Liste aller Termine in der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste der Termine erfolgreich abgerufen",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Termin.class))),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @GetMapping
    public ResponseEntity<List<Termin>> getAllTermine(@RequestParam @Nullable String RangeStart, @RequestParam @Nullable String RangeEnd) {
        List<Termin> list = null;
        try {
            list = terminSearchService.SearchTerminByDate(RangeStart, RangeEnd);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(list);
    }

    @Operation(summary = "Gibt einen Termin zurück", description = "Liefert einen Termin anhand seiner Id in der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Termin erfolgreich abgerufen",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Termin.class))),
            @ApiResponse(responseCode = "404", description = "Termin nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Termin> getTermin(Long id) {
        return terminDb.findById(id)
                .map(termin -> ResponseEntity.ok().body(termin))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Updated einen Termin", description = "Updated einen Termin anhand seiner Id in der Datenbank und gibt ihn zurück.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Termin erfolgreich geändert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Termin.class))),
            @ApiResponse(responseCode = "404", description = "Termin nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Termin> updateTermin(@PathVariable Long id, @RequestBody TerminUpdateCreateDto terminUpdateCreateDto) {
        Optional<Termin> existingTermin = terminDb.findById(id);
        if (existingTermin.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Termin updatedTermin = terminSearchService.updateTermin(existingTermin.get(), terminUpdateCreateDto);
            return ResponseEntity.ok().body(updatedTermin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Erstellt einen neuen Termin", description = "Fügt einen neuen Termin hinzu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Termin erfolgreich erstellt",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Termin.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @PostMapping
    public ResponseEntity<Termin> createTermin(@RequestBody TerminUpdateCreateDto termin) {
        try {
            return ResponseEntity.ok().body(terminSearchService.createTermin(termin));
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Löscht einen Termin", description = "Löscht einen Termin anhand seiner Id permanent aus der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Termin erfolgreich gelöscht"),
            @ApiResponse(responseCode = "404", description = "Termin nicht gefunden"),
            @ApiResponse(responseCode = "500", description = "Serverfehler")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTermin(Long id) {
        if(terminDb.existsById(id)) {
            terminDb.deleteById(id);
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
