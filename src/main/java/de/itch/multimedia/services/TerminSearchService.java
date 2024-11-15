package de.itch.multimedia.services;

import de.itch.multimedia.db.TerminDb;
import de.itch.multimedia.dtos.Termin;
import de.itch.multimedia.dtos.TerminUpdateCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TerminSearchService {

    @Autowired
    private TerminDb terminDb;

    public List<Termin> SearchTerminByDate(String start, String end) {
        List<Termin> list;
        if(start != null && end != null) {
            LocalDateTime startDateTime = LocalDateTime.parse(start);
            LocalDateTime endDateTime = LocalDateTime.parse(end);
            checkStartAndEndTime(startDateTime, endDateTime);
            list = terminDb.findAllByRange(startDateTime, endDateTime);
        }else {
            list = terminDb.findAll();
        }
        return list;
    }

    private void checkStartAndEndTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if(startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        if(endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        if(startDateTime.equals(endDateTime)) {
            throw new IllegalArgumentException("Start date cannot be equals to end date");
        }
    }

    public Termin createTermin(TerminUpdateCreateDto terminUpdateCreateDto) {
        Termin termin = toTermin(new Termin(), terminUpdateCreateDto);
        checkStartAndEndTime(termin.getStartTime(), termin.getEndTime());
        terminDb.save(termin);
        return termin;
    }

    public Termin updateTermin(Termin termin, TerminUpdateCreateDto terminUpdateCreateDto) {
        toTermin(termin, terminUpdateCreateDto);
        checkStartAndEndTime(termin.getStartTime(), termin.getEndTime());
        terminDb.save(termin);
        return termin;
    }

    private Termin toTermin(Termin termin, TerminUpdateCreateDto terminUpdateCreateDto) {
        if(!terminUpdateCreateDto.getTitel().isEmpty()) {
            termin.setTitel(terminUpdateCreateDto.getTitel());
        }
        if(terminUpdateCreateDto.getStartTime() != null) {
            termin.setStartTime(terminUpdateCreateDto.getStartTime());
        }
        if(terminUpdateCreateDto.getEndTime() != null) {
            termin.setEndTime(terminUpdateCreateDto.getEndTime());
        }
        return termin;
    }

}
