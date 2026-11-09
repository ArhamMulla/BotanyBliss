
package com.example.botanybliss.dto;

import com.example.botanybliss.entity.ContributionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantContributionRequest {

    @NotNull(message = "Contribution type is required")
    private ContributionType contributionType;

    private Long plantId;

    @Size(max = 100, message = "Common name cannot exceed 100 characters")
    private String commonName;

    @Size(max = 150, message = "Scientific name cannot exceed 150 characters")
    private String scientificName;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private String sunlight;
    private String watering;
    private String soilType;
}