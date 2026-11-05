package com.example.botanybliss.controller;

import com.example.botanybliss.dto.PlantPatchRequest;
import com.example.botanybliss.dto.PlantRequest;
import com.example.botanybliss.dto.PlantResponse;
import com.example.botanybliss.entity.Plant;
import com.example.botanybliss.service.PlantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "http://localhost:5173")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    // Add a plant
    @PostMapping
    public ResponseEntity<PlantResponse> addPlant(
            @Valid @RequestBody PlantRequest request) {

        Plant plant = new Plant();
        plant.setCommonName(request.getCommonName());
        plant.setScientificName(request.getScientificName());
        plant.setDescription(request.getDescription());
        plant.setSunlight(request.getSunlight());
        plant.setWatering(request.getWatering());
        plant.setSoilType(request.getSoilType());
        plant.setImageUrl(request.getImageUrl());

        Plant savedPlant = plantService.savePlant(plant);

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
    @PatchMapping("/{id}")
    public ResponseEntity<PlantResponse> updatePlant(
            @PathVariable Long id,
            @Valid @RequestBody PlantPatchRequest request) {

        return plantService.updatePlant(id, request)
                .map(plantService::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}