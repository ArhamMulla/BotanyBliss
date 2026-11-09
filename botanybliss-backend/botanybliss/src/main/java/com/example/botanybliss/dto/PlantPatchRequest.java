package com.example.botanybliss.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlantPatchRequest {

    private String commonName;
    private String scientificName;
    private String description;
    private String sunlight;
    private String watering;
    private String soilType;
}