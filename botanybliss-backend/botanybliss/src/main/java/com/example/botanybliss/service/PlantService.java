package com.example.botanybliss.service;

import com.example.botanybliss.dto.PlantPatchRequest;
import com.example.botanybliss.dto.PlantResponse;
import com.example.botanybliss.entity.Plant;
import com.example.botanybliss.repository.PlantRepository;
import com.example.botanybliss.storage.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    private final PlantRepository plantRepository;
    private final StorageService storageService;

    @Value("${storage.base-url}")
    private String imageBaseUrl;

    public PlantService(
            PlantRepository plantRepository,
            StorageService storageService) {

        this.plantRepository = plantRepository;
        this.storageService = storageService;
    }
    public Plant savePlant(Plant plant) {
        return plantRepository.save(plant);
    }

    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    public Optional<Plant> getPlantById(Long id) {
        return plantRepository.findById(id);
    }

    public void deletePlant(Long id) {
        plantRepository.deleteById(id);
    }

    public Plant createPlant(Plant plant, MultipartFile image) {

        if (image != null && !image.isEmpty()) {
            String fileName = storageService.store(image);
            plant.setImageUrl(fileName);
        }

        return plantRepository.save(plant);
    }

    public Optional<Plant> updatePlant(
            Long id,
            PlantPatchRequest updates,
            MultipartFile image) {

        return plantRepository.findById(id)
                .map(existingPlant -> {

                    if (updates.getCommonName() != null)
                        existingPlant.setCommonName(updates.getCommonName());

                    if (updates.getScientificName() != null)
                        existingPlant.setScientificName(updates.getScientificName());

                    if (updates.getDescription() != null)
                        existingPlant.setDescription(updates.getDescription());

                    if (updates.getSunlight() != null)
                        existingPlant.setSunlight(updates.getSunlight());

                    if (updates.getWatering() != null)
                        existingPlant.setWatering(updates.getWatering());

                    if (updates.getSoilType() != null)
                        existingPlant.setSoilType(updates.getSoilType());

                    if (image != null && !image.isEmpty()) {

                        String oldImage = existingPlant.getImageUrl();

                        String newImage = storageService.store(image);
                        existingPlant.setImageUrl(newImage);

                        if (oldImage != null
                                && !oldImage.isBlank()
                                && !oldImage.startsWith("http://")
                                && !oldImage.startsWith("https://")) {
                            storageService.delete(oldImage);
                        }
                    }

                    return plantRepository.save(existingPlant);
                });
    }

    public PlantResponse mapToResponse(Plant plant) {

        PlantResponse response = new PlantResponse();

        response.setId(plant.getId());
        response.setCommonName(plant.getCommonName());
        response.setScientificName(plant.getScientificName());
        response.setDescription(plant.getDescription());
        response.setSunlight(plant.getSunlight());
        response.setWatering(plant.getWatering());
        response.setSoilType(plant.getSoilType());

        String image = plant.getImageUrl();
        if (image == null || image.isBlank()) {
            response.setImageUrl(null);
        } else if (image.startsWith("http://") || image.startsWith("https://")) {
            response.setImageUrl(image);
        } else {
            response.setImageUrl(imageBaseUrl + image);
        }

        return response;
    }
}
