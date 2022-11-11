package com.capco.featureflagservice.service;

import com.capco.featureflagservice.data.model.FeatureFlag;
import com.capco.featureflagservice.data.model.User;
import com.capco.featureflagservice.data.repository.FeatureFlagRepository;
import com.capco.featureflagservice.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class FeatureFlagServiceTest {

    private static final String USERNAME = "name";
    private static final String LOCAL = "local";
    private static final String GLOBAL = "global";

    @Mock
    private UserRepository userRepository;
    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @InjectMocks
    private FeatureFlagService featureFlagService;

    @Test
    void shouldReturnFeatureFlags() {
        var user = new User();
        var localFeatureFlag = new FeatureFlag();
        localFeatureFlag.setName(LOCAL);
        user.setFeatureFlags(Set.of(localFeatureFlag));
        doReturn(Optional.of(user)).when(userRepository).findByUsername(USERNAME);
        var globalFeatureFlag = new FeatureFlag();
        globalFeatureFlag.setName(GLOBAL);
        doReturn(List.of(globalFeatureFlag)).when(featureFlagRepository).findAllByGlobal(true);

        var flags = featureFlagService.getFeatureFlags(USERNAME);

        assertThat(flags).contains(LOCAL, GLOBAL);
    }
}
