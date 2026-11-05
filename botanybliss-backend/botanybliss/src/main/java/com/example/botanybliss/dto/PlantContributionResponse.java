package com.example.botanybliss.dto;

import com.example.botanybliss.entity.ContributionStatus;
import com.example.botanybliss.entity.ContributionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlantContributionResponse {

    private Long id;

    private Long userId;
    private String userName;

    private Long plantId;

    private ContributionType contributionType;
    private ContributionStatus status;

    private String commonName;
    private String scientificName;
    private String description;
    private String sunlight;
    private String watering;
    private String soilType;
    private String imageUrl;

    private String adminRemarks;

    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt;
}