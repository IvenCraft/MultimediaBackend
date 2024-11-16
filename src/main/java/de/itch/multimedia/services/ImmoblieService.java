package de.itch.multimedia.services;

import de.itch.multimedia.db.ImmoblieDb;
import de.itch.multimedia.dtos.Immobile;
import de.itch.multimedia.dtos.ImmoblieUpdateCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImmoblieService {

    @Autowired
    private ImmoblieDb immoblieDb;

    public Immobile updateImmoblie(ImmoblieUpdateCreateDto createDto, Immobile immobile) {
        immobile = toImmobile(createDto, immobile);
        immoblieDb.save(immobile);
        return immobile;
    }

    public Immobile createImmoblie(ImmoblieUpdateCreateDto createDto) {
        Immobile immobile = toImmobile(createDto, new Immobile());
        immobile.setAktiv(true);
        immoblieDb.save(immobile);
        return immobile;
    }

    private Immobile toImmobile(ImmoblieUpdateCreateDto createDto, Immobile immobile) {
        if(createDto.getName() != null) {
            immobile.setName(createDto.getName());
        }
        if(createDto.getAdresse() != null) {
            immobile.setAdresse(createDto.getAdresse());
        }
        if(createDto.getPreis() != null) {
            immobile.setPreis(createDto.getPreis());
        }

        return immobile;
    }

}
