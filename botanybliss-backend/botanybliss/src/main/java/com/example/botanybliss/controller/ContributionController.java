package com.example.botanybliss.controller;

import com.example.botanybliss.dto.ContributionReviewRequest;
import com.example.botanybliss.dto.PlantContributionRequest;
import com.example.botanybliss.dto.PlantContributionResponse;
import com.example.botanybliss.entity.PlantContribution;
import com.example.botanybliss.service.ContributionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contributions")
public class ContributionController {

    private final ContributionService contributionService;

    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlantContributionResponse submitContribution(
            @Valid @RequestBody PlantContributionRequest request,
            Authentication authentication) {

        return contributionService.submitContribution(
                request, authentication.getName());
    }

    @GetMapping("/pending")
    public List<PlantContributionResponse> getPendingContributions() {
        return contributionService.getPendingContributions();
    }

    @PostMapping("/{id}/approve")
    public String approveContribution(
            @PathVariable Long id,
            @RequestBody ContributionReviewRequest request) {

        contributionService.approveContribution(id, request.getAdminRemarks());

        return "Contribution approved successfully";
    }

    @PostMapping("/{id}/reject")
    public String rejectContribution(
            @PathVariable Long id,
            @RequestBody ContributionReviewRequest request) {

        contributionService.rejectContribution(id, request.getAdminRemarks());

        return "Contribution rejected successfully";
    }

    @GetMapping("/my")
    public List<PlantContributionResponse> getMyContributions(
            Authentication authentication) {

        return contributionService.getUserContributions(
                authentication.getName());
    }
}
