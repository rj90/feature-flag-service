package com.capco.featureflagservice.service;

import com.capco.featureflagservice.controller.model.CreateFeatureFlagRequest;
import com.capco.featureflagservice.data.model.FeatureFlag;
import com.capco.featureflagservice.data.repository.FeatureFlagRepository;
import com.capco.featureflagservice.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FeatureFlagManagementService {

    private final UserRepository userRepository;
    private final FeatureFlagRepository featureFlagRepository;

    public void createFeatureFlag(CreateFeatureFlagRequest createFeatureFlagRequest) {
        var featureFlag = new FeatureFlag();
        featureFlag.setName(createFeatureFlagRequest.getName());
        featureFlagRepository.save(featureFlag);
    }

    public void assignFlagToUser(String featureFlagName, String username) {
        var featureFlag = featureFlagRepository.findByName(featureFlagName)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("Feature flag %s not found", featureFlagName)));
        var user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("User %s not found", username)));
        user.getFeatureFlags().add(featureFlag);
        userRepository.save(user);
    }
}
