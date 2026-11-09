package com.example.botanybliss.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalStorageImpl implements StorageService {

    private final Path storageLocation;

    public LocalStorageImpl(@Value("${storage.location}") String location) {
        this.storageLocation = Paths.get(location)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create storage directory", e);
        }
    }

    @Override
    public String store(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store an empty file");
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if (extension == null ||
                !extension.matches("(?i)jpg|jpeg|png|webp")) {
            throw new IllegalArgumentException("Only JPG, JPEG, PNG and WEBP images are allowed");
        }

        String fileName = UUID.randomUUID() + "." + extension.toLowerCase();

        try {
            Path destination = storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), destination);

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store image", e);
        }
    }

    @Override
    public void delete(String fileName) {

        try {
            Path filePath = storageLocation.resolve(fileName)
                    .normalize();

            if (!filePath.startsWith(storageLocation)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    @Override
    public Resource load(String fileName) {
        try {
            Path filePath = storageLocation.resolve(fileName)
                    .normalize();

            if (!filePath.startsWith(storageLocation)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            Resource resource = new org.springframework.core.io.UrlResource(
                    filePath.toUri()
            );

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }

            throw new RuntimeException("Image not found: " + fileName);

        } catch (IOException e) {
            throw new RuntimeException("Could not load image", e);
        }
    }
}