package de.itch.multimedia.db;

import de.itch.multimedia.dtos.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImmoblieDb extends JpaRepository<Immobile, Long>{

    @Query("""
    SELECT i
    FROM Immobile i 
    WHERE 
        (:name is null OR i.name LIKE %:name%)
        AND (:addresse is null OR i.adresse LIKE %:addresse%)
        AND (:preis is null OR i.preis = :preis)
        AND (:includeInactive = true OR i.isAktiv = true)
        
""")
    List<Immobile> filterList(@Param("name") String name, @Param("addresse") String addresse, @Param("preis") Float preis, @Param("includeInactive") Boolean includeInactive);

    @Override
    @Query("""
        SELECT i
        FROM Immobile i 
        WHERE i.isAktiv = true
        AND i.id = :id
    """)
    Optional<Immobile> findById(@Param("id") Long id);

    @Query("""
        SELECT i
        FROM Immobile i
        WHERE i.id = :id
    """)
    Optional<Immobile> findByIdWithInactive(@Param("id") Long id);
}
