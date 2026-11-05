package com.example.botanybliss.service;

import com.example.botanybliss.dto.PlantContributionRequest;
import com.example.botanybliss.dto.PlantContributionResponse;
import com.example.botanybliss.entity.*;
import com.example.botanybliss.repository.PlantContributionRepository;
import com.example.botanybliss.repository.PlantRepository;
import com.example.botanybliss.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContributionService {

    private final PlantContributionRepository contributionRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public ContributionService(
            PlantContributionRepository contributionRepository,
            PlantRepository plantRepository,
            UserRepository userRepository) {
        this.contributionRepository = contributionRepository;
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
    }

    public PlantContributionResponse submitContribution(
            PlantContributionRequest request, String email) {

        if (request.getContributionType() == null) {
            throw new IllegalArgumentException("Contribution type is required");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PlantContribution contribution = new PlantContribution();

        contribution.setUser(user);
        contribution.setContributionType(request.getContributionType());

        if (request.getContributionType() == ContributionType.NEW_PLANT) {

            if (!hasText(request.getCommonName())
                    || !hasText(request.getScientificName())) {
                throw new IllegalArgumentException(
                        "Common name and scientific name are required");
            }

        } else if (request.getContributionType() == ContributionType.UPDATE_PLANT) {

            if (request.getPlantId() == null) {
                throw new IllegalArgumentException("Plant ID is required for updates");
            }

            Plant plant = plantRepository.findById(request.getPlantId())
                    .orElseThrow(() -> new IllegalArgumentException("Plant not found"));

            contribution.setPlant(plant);

            if (!hasAnyPlantDetails(request)) {
                throw new IllegalArgumentException("At least one field must be provided");
            }
        }

        contribution.setCommonName(request.getCommonName());
        contribution.setScientificName(request.getScientificName());
        contribution.setDescription(request.getDescription());
        contribution.setSunlight(request.getSunlight());
        contribution.setWatering(request.getWatering());
        contribution.setSoilType(request.getSoilType());
        contribution.setImageUrl(request.getImageUrl());

        return mapToResponse(contributionRepository.save(contribution));
    }

    public List<PlantContributionResponse> getUserContributions(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return contributionRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private boolean hasAnyPlantDetails(PlantContributionRequest request) {
        return hasText(request.getCommonName())
                || hasText(request.getScientificName())
                || hasText(request.getDescription())
                || hasText(request.getSunlight())
                || hasText(request.getWatering())
                || hasText(request.getSoilType())
                || hasText(request.getImageUrl());
    }

    public List<PlantContributionResponse> getPendingContributions() {

        return contributionRepository
                .findByStatusOrderByCreatedAtAsc(ContributionStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional
    public void approveContribution(Long contributionId, String adminRemarks) {

        PlantContribution contribution = contributionRepository.findById(contributionId)
                .orElseThrow(() -> new IllegalArgumentException("Contribution not found"));

        if (contribution.getStatus() != ContributionStatus.PENDING) {
            throw new IllegalArgumentException("Contribution has already been reviewed");
        }

        if (contribution.getContributionType() == ContributionType.NEW_PLANT) {

            Plant plant = new Plant();
            copyContributionDetails(contribution, plant);
            plantRepository.save(plant);

        } else if (contribution.getContributionType() == ContributionType.UPDATE_PLANT) {

            Plant plant = contribution.getPlant();

            if (plant == null) {
                throw new IllegalArgumentException("Plant not found for this contribution");
            }

            copyNonBlankContributionDetails(contribution, plant);
            plantRepository.save(plant);
        }

        contribution.setStatus(ContributionStatus.APPROVED);
        contribution.setAdminRemarks(adminRemarks);
        contribution.setReviewedAt(java.time.LocalDateTime.now());

        contributionRepository.save(contribution);
    }

    @Transactional
    public void rejectContribution(Long contributionId, String adminRemarks) {

        PlantContribution contribution = contributionRepository.findById(contributionId)
                .orElseThrow(() -> new IllegalArgumentException("Contribution not found"));

        if (contribution.getStatus() != ContributionStatus.PENDING) {
            throw new IllegalArgumentException("Contribution has already been reviewed");
        }

        contribution.setStatus(ContributionStatus.REJECTED);
        contribution.setAdminRemarks(adminRemarks);
        contribution.setReviewedAt(java.time.LocalDateTime.now());

        contributionRepository.save(contribution);
    }

    private void copyContributionDetails(PlantContribution contribution, Plant plant) {
        plant.setCommonName(contribution.getCommonName());
        plant.setScientificName(contribution.getScientificName());
        plant.setDescription(contribution.getDescription());
        plant.setSunlight(contribution.getSunlight());
        plant.setWatering(contribution.getWatering());
        plant.setSoilType(contribution.getSoilType());
        plant.setImageUrl(contribution.getImageUrl());
    }

    private void copyNonBlankContributionDetails(
            PlantContribution contribution, Plant plant) {

        if (hasText(contribution.getCommonName()))
            plant.setCommonName(contribution.getCommonName());

        if (hasText(contribution.getScientificName()))
            plant.setScientificName(contribution.getScientificName());

        if (hasText(contribution.getDescription()))
            plant.setDescription(contribution.getDescription());

        if (hasText(contribution.getSunlight()))
            plant.setSunlight(contribution.getSunlight());

        if (hasText(contribution.getWatering()))
            plant.setWatering(contribution.getWatering());

        if (hasText(contribution.getSoilType()))
            plant.setSoilType(contribution.getSoilType());

        if (hasText(contribution.getImageUrl()))
            plant.setImageUrl(contribution.getImageUrl());
    }

    private PlantContributionResponse mapToResponse(
            PlantContribution contribution) {

        PlantContributionResponse response = new PlantContributionResponse();

        response.setId(contribution.getId());

        response.setUserId(contribution.getUser().getId());
        response.setUserName(contribution.getUser().getName());

        if (contribution.getPlant() != null) {
            response.setPlantId(contribution.getPlant().getId());
        }

        response.setContributionType(contribution.getContributionType());
        response.setStatus(contribution.getStatus());

        response.setCommonName(contribution.getCommonName());
        response.setScientificName(contribution.getScientificName());
        response.setDescription(contribution.getDescription());
        response.setSunlight(contribution.getSunlight());
        response.setWatering(contribution.getWatering());
        response.setSoilType(contribution.getSoilType());
        response.setImageUrl(contribution.getImageUrl());

        response.setAdminRemarks(contribution.getAdminRemarks());

        response.setCreatedAt(contribution.getCreatedAt());
        response.setReviewedAt(contribution.getReviewedAt());

        return response;
    }

}