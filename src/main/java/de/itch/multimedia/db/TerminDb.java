package de.itch.multimedia.db;

import de.itch.multimedia.dtos.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TerminDb extends JpaRepository<Termin, Long>  {

    @Query("""
    SELECT t
    FROM Termin t 
    WHERE 
        (t.startTime >= :start AND t.startTime <= :end)
        OR 
        (t.endTime >= :start AND t.endTime <= :end)
        OR 
        (t.startTime <= :start AND t.endTime >= :end)
        OR
        (t.startTime >= :start AND t.endTime <= :end)
""")
    List<Termin> findAllByRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
