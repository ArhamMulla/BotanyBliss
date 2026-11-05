package com.example.botanybliss.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commonName;
    private String scientificName;

    @Column(length = 1000)
    private String description;

    private String sunlight;
    private String watering;
    private String soilType;
    private String imageUrl;
}
