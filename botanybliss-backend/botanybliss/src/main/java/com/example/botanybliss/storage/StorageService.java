package com.example.botanybliss.storage;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

public interface StorageService {

    String store(MultipartFile file);

    void delete(String fileName);

    Resource load(String fileName);
}
