package com.example.botanybliss.controller;

import com.example.botanybliss.dto.ContributionReviewRequest;
import com.example.botanybliss.dto.PlantContributionRequest;
import com.example.botanybliss.dto.PlantContributionResponse;
import com.example.botanybliss.entity.PlantContribution;
import com.example.botanybliss.service.ContributionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/contributions")
public class ContributionController {

    private final ContributionService contributionService;

    public ContributionController(ContributionService contributionService) {
        this.contributionService = contributionService;
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PlantContributionResponse submitContribution(
            @Valid @ModelAttribute PlantContributionRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication) {

        return contributionService.submitContribution(
                request, image, authentication.getName());
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
