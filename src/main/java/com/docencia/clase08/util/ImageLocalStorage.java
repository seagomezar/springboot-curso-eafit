package com.docencia.clase08.util;

import com.docencia.clase08.interfaces.ImageStorage;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageLocalStorage implements ImageStorage {
    private static final String STORAGE_DIR = "uploads/";

    @Override
    public void store(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                // Crear el directorio si no existe
                Path storageDir = Paths.get(STORAGE_DIR);
                if (!Files.exists(storageDir)) {
                    Files.createDirectories(storageDir);
                }
                // Guardar la imagen con el nombre "test.png"
                Path destinationFile = storageDir.resolve("test.png").normalize().toAbsolutePath();
                Files.copy(file.getInputStream(), destinationFile);
            } catch (IOException e) {
                e.printStackTrace();
                // Aquí se podría lanzar una excepción personalizada
            }
        }
    }
}
