package com.capco.featureflagservice.service;

import com.capco.featureflagservice.controller.model.CreateFeatureFlagRequest;
import com.capco.featureflagservice.data.model.FeatureFlag;
import com.capco.featureflagservice.data.model.User;
import com.capco.featureflagservice.data.repository.FeatureFlagRepository;
import com.capco.featureflagservice.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FeatureFlagManagementServiceTest {
    
    private static final String FLAG_NAME = "flag";
    private static final String USERNAME = "name";

    @InjectMocks
    private FeatureFlagManagementService featureFlagManagementService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @Test
    void shouldCreateNewFeatureFlag() {
        var createFeatureFlagRequest = new CreateFeatureFlagRequest();
        createFeatureFlagRequest.setName(FLAG_NAME);

        featureFlagManagementService.createFeatureFlag(createFeatureFlagRequest);

        var argument = ArgumentCaptor.forClass(FeatureFlag.class);
        verify(featureFlagRepository).save(argument.capture());
        var value = argument.getValue();

        assertThat(value.getName()).isEqualTo(FLAG_NAME);
        assertThat(value.isGlobal()).isFalse();
    }

    @Test
    void shouldThrowErrorFeatureFlagNotFound() {
        var exception = assertThrows(ResponseStatusException.class,
            () -> featureFlagManagementService.assignFlagToUser(FLAG_NAME, USERNAME));

        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo(String.format("Feature flag %s not found", FLAG_NAME));
    }

    @Test
    void shouldThrowErrorUserNotFound() {
        var featureFlag = new FeatureFlag();
        featureFlag.setName(FLAG_NAME);
        doReturn(Optional.of(featureFlag)).when(featureFlagRepository).findByName(FLAG_NAME);

        var exception = assertThrows(ResponseStatusException.class,
            () -> featureFlagManagementService.assignFlagToUser(FLAG_NAME, USERNAME));

        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getReason()).isEqualTo(String.format("User %s not found", USERNAME));
    }

    @Test
    void shouldAssignFlagToUser() {
        var featureFlag = new FeatureFlag();
        featureFlag.setName(FLAG_NAME);
        doReturn(Optional.of(featureFlag)).when(featureFlagRepository).findByName(FLAG_NAME);

        var user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setEnabled(true);
        user.setFeatureFlags(new HashSet<>());
        doReturn(Optional.of(user)).when(userRepository).findByUsername(USERNAME);

        featureFlagManagementService.assignFlagToUser(FLAG_NAME, USERNAME);

        var argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());
        var value = argument.getValue();

        assertThat(value.getId()).isEqualTo(1L);
        assertThat(value.getUsername()).isEqualTo(USERNAME);
        assertThat(value.isEnabled()).isTrue();
        assertThat(value.getFeatureFlags()).contains(featureFlag);
    }
}
