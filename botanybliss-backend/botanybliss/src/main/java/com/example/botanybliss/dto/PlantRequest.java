package com.example.botanybliss.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantRequest {

    @NotBlank(message = "Common name is required")
    private String commonName;

    @NotBlank(message = "Scientific name is required")
    private String scientificName;

    private String description;
    private String sunlight;
    private String watering;
    private String soilType;
}