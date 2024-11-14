package de.itch.multimedia.services;

import de.itch.multimedia.db.TerminDb;
import de.itch.multimedia.dtos.Termin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class TerminSearchService {

    @Autowired
    private TerminDb terminDb;

    public List<Termin> SearchTerminByDate(Timestamp startTime, Timestamp endTime) {
        List<Termin> list = terminDb.findAll();
        list.stream().map(termin -> termin.getStartTime().after(startTime) && termin.getEndTime().before(endTime));
        return list;
    }

}
