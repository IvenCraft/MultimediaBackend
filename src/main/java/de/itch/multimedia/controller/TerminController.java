package de.itch.multimedia.controller;

import de.itch.multimedia.db.TerminDb;
import de.itch.multimedia.dtos.Termin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/termin")
@Tag(name = "Termine API", description = "Verwaltet alle Termin die Angezeigt werden")
public class TerminController {

    @Autowired
    private TerminDb terminDb;

    @Operation(summary = "Gibt alle Termine zurück", description = "Liefert eine Liste aller Termine in der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste der Termine erfolgreich abgerufen",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Termin.class)))
    })
    @GetMapping
    public List<Termin> getAllTermine() {
        return terminDb.findAll();
    }

    @Operation(summary = "Gibt einen Termin zurück", description = "Liefert einen Termin anhand seiner Id in der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Termin erfolgreich abgerufen",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Termin.class))),
            @ApiResponse(responseCode = "404", description = "Termin nicht gefunden")
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
            @ApiResponse(responseCode = "404", description = "Termin nicht gefunden")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Termin> updateTermin(@PathVariable Long id, @RequestBody Termin terminUpdate) {
        return terminDb.findById(id)
                .map(termin -> {
                    termin.setTitel(terminUpdate.getTitel());
                    Termin updetedTermin = terminDb.save(termin);
                    return ResponseEntity.ok().body(updetedTermin);
                }).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Erstellt einen neuen Termin", description = "Fügt einen neuen Termin hinzu.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Termin erfolgreich erstellt",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Termin.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe")
    })
    @PostMapping
    public Termin createTermin(@RequestBody Termin termin) {
        return terminDb.save(termin);
    }

    @Operation(summary = "Löscht einen Termin", description = "Löscht einen Termin anhand seiner Id permanent aus der Datenbank.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Termin erfolgreich gelöscht",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Termin.class))),
            @ApiResponse(responseCode = "404", description = "Termin nicht gefunden")
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
