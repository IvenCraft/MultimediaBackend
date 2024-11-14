package de.itch.multimedia.db;

import de.itch.multimedia.dtos.Termin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminDb extends JpaRepository<Termin, Long>  {

}
