package com.capco.featureflagservice.controller;

import com.capco.featureflagservice.service.FeatureFlagService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("feature-flag")
@RequiredArgsConstructor
@SecurityRequirement(name = "feature-flag")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    @GetMapping
    public Set<String> getFeatureFlags(Principal principal) {
        return featureFlagService.getFeatureFlags(principal.getName());
    }
}
