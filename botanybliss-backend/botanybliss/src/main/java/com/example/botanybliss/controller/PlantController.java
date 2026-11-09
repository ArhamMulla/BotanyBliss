package com.example.botanybliss.controller;

import com.example.botanybliss.dto.PlantPatchRequest;
import com.example.botanybliss.dto.PlantRequest;
import com.example.botanybliss.dto.PlantResponse;
import com.example.botanybliss.entity.Plant;
import com.example.botanybliss.service.PlantService;
import com.example.botanybliss.storage.StorageService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "http://localhost:5173")
public class PlantController {

    private final PlantService plantService;
    private final StorageService storageService;

    public PlantController(
            PlantService plantService,
            StorageService storageService) {

        this.plantService = plantService;
        this.storageService = storageService;
    }

    // Add a plant
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlantResponse> addPlant(
            @Valid @ModelAttribute PlantRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        Plant plant = new Plant();
        plant.setCommonName(request.getCommonName());
        plant.setScientificName(request.getScientificName());
        plant.setDescription(request.getDescription());
        plant.setSunlight(request.getSunlight());
        plant.setWatering(request.getWatering());
        plant.setSoilType(request.getSoilType());

        Plant savedPlant = plantService.createPlant(plant, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(plantService.mapToResponse(savedPlant));
    }

    // Get all plants
    @GetMapping
    public ResponseEntity<List<PlantResponse>> getAllPlants() {
        List<PlantResponse> plants = plantService.getAllPlants()
                .stream()
                .map(plantService::mapToResponse)
                .toList();
        return ResponseEntity.ok(plants);
    }

    // Get plant by ID
    @GetMapping("/{id}")
    public ResponseEntity<PlantResponse> getPlantById(
            @PathVariable Long id) {
        return plantService.getPlantById(id)
                .map(plantService::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete plant
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }

    // Update plant
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlantResponse> updatePlant(
            @PathVariable Long id,
            @ModelAttribute PlantPatchRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        return plantService.updatePlant(id, request, image)
                .map(plantService::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/images/{fileName:.+}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String fileName) {

        Resource image = storageService.load(fileName);

        MediaType contentType = MediaTypeFactory
                .getMediaType(image)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(contentType)
                .body(image);
    }
}