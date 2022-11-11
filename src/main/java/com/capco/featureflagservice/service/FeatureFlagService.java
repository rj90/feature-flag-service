package com.capco.featureflagservice.service;

import com.capco.featureflagservice.data.model.FeatureFlag;
import com.capco.featureflagservice.data.repository.FeatureFlagRepository;
import com.capco.featureflagservice.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeatureFlagService {

    private final UserRepository userRepository;
    private final FeatureFlagRepository featureFlagRepository;
    public Set<String> getFeatureFlags(String name) {
        var flags = userRepository.findByUsername(name)
            .map(user -> featureFlagsToAuthorities(user.getFeatureFlags()))
            .orElse(new HashSet<>());
        flags.addAll(featureFlagsToAuthorities(featureFlagRepository.findAllByGlobal(true)));
        return flags;
    }

    private Set<String> featureFlagsToAuthorities(Collection<FeatureFlag> featureFlags) {
        return featureFlags.stream().map(FeatureFlag::getName).collect(Collectors.toSet());
    }
}
