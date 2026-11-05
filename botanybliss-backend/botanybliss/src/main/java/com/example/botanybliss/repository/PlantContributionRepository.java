package com.example.botanybliss.repository;

import com.example.botanybliss.dto.PlantContributionRequest;
import com.example.botanybliss.dto.PlantContributionResponse;
import com.example.botanybliss.entity.ContributionStatus;
import com.example.botanybliss.entity.PlantContribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface PlantContributionRepository
        extends JpaRepository<PlantContribution, Long> {

    List<PlantContributionResponse> findByUserId(Long userId);

    List<PlantContributionResponse> findByStatus(
            ContributionStatus status
    );

    List<PlantContribution> findByStatusOrderByCreatedAtAsc(
            ContributionStatus status
    );

    List<PlantContribution> findByUserIdOrderByCreatedAtDesc(Long id);
}
