package com.docencia.clase08.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {
    void store(MultipartFile file);
}
