package de.itch.multimedia.services;

import de.itch.multimedia.db.ImageDb;
import de.itch.multimedia.dtos.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private ImageDb imageDb;

    public Image saveImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setData(file.getBytes());

        return imageDb.save(image);
    }

    public void deleteImage(Long id) throws IllegalArgumentException {
        imageDb.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bild nicht gefunden"));
        imageDb.deleteById(id);
    }

    public Image getImageData(Long id) {
        Image image = imageDb.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bild nicht gefunden"));
        return image;
    }
}
