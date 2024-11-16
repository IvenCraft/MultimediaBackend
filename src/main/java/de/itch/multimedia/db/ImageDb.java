package de.itch.multimedia.db;

import de.itch.multimedia.dtos.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageDb extends JpaRepository<Image, Long> {

    @Query("""
SELECT i from Image i where i.immobile = :immobileId
""")
    List<Image> findByImmobileId(@Param("immobileId") Long immobileId);

}
