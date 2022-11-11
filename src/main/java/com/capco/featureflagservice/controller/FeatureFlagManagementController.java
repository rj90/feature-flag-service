package com.capco.featureflagservice.controller;

import com.capco.featureflagservice.controller.model.CreateFeatureFlagRequest;
import com.capco.featureflagservice.service.FeatureFlagManagementService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("management/feature-flag")
@RequiredArgsConstructor
@SecurityRequirement(name = "feature-flag")
@PreAuthorize("hasRole('ADMIN')")
public class FeatureFlagManagementController {

    private final FeatureFlagManagementService featureFlagManagementService;

    @PostMapping
    public void createFeatureFlag(@RequestBody CreateFeatureFlagRequest createFeatureFlagRequest) {
        featureFlagManagementService.createFeatureFlag(createFeatureFlagRequest);
    }

    @PutMapping("{featureFlagName}/assign/{username}")
    public void assignFlagToUser(@PathVariable String featureFlagName, @PathVariable String username) {
        featureFlagManagementService.assignFlagToUser(featureFlagName, username);
    }
}
