package com.example.botanybliss.service;

import com.example.botanybliss.dto.PlantPatchRequest;
import com.example.botanybliss.dto.PlantResponse;
import com.example.botanybliss.entity.Plant;
import com.example.botanybliss.repository.PlantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
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

    public Optional<Plant> updatePlant(Long id, PlantPatchRequest updates) {

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

                    if (updates.getImageUrl() != null)
                        existingPlant.setImageUrl(updates.getImageUrl());

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
        response.setImageUrl(plant.getImageUrl());

        return response;
    }
}
