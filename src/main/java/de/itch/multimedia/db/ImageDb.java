package de.itch.multimedia.db;

import de.itch.multimedia.dtos.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDb extends JpaRepository<Image, Long> {
}
