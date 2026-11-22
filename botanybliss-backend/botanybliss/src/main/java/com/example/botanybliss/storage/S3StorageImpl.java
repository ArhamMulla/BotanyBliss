
package com.example.botanybliss.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.*;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@Profile("s3")
public class S3StorageImpl implements StorageService {

    private final S3Client s3Client;
    private final String bucketName;
    private final String region;

    public S3StorageImpl(
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.region}") String region) {

        this.bucketName = bucketName;

        Region awsRegion = Region.of(region);

        this.s3Client = S3Client.builder()
                .region(awsRegion)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        this.region = region;
    }

    @Override
    public String store(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot store an empty file");
        }

        String extension = StringUtils.getFilenameExtension(
                file.getOriginalFilename()
        );

        if (extension == null ||
                !extension.matches("(?i)jpg|jpeg|png|webp")) {
            throw new IllegalArgumentException(
                    "Only JPG, JPEG, PNG and WEBP images are allowed"
            );
        }

        String fileName = UUID.randomUUID() + "." +
                extension.toLowerCase();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    request,
                    software.amazon.awssdk.core.sync.RequestBody
                            .fromInputStream(file.getInputStream(), file.getSize())
            );

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to S3", e);
        }
    }

    @Override
    public void delete(String fileName) {

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(request);
    }

    @Override
    public Resource load(String fileName) {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return new InputStreamResource(s3Client.getObject(request));
    }

    @Override
    public String getUrl(String fileName) {
        return "https://" + bucketName
                + ".s3." + region
                + ".amazonaws.com/" + fileName;
    }
}
