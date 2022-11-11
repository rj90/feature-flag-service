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
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class FeatureFlagUserDetailsServiceTest {

    private static final String USERNAME = "name";
    private static final String PASSWORD = "password";

    @Mock
    private UserRepository userRepository;
    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @InjectMocks
    private FeatureFlagUserDetailsService featureFlagUserDetailsService;

    @Test
    void shouldThrowErrorUserNotFound() {
        var exception = assertThrows(UsernameNotFoundException.class,
            () -> featureFlagUserDetailsService.loadUserByUsername(USERNAME));

        assertThat(exception.getMessage()).isEqualTo(String.format("User with name %s not found", USERNAME));
    }

    @Test
    void shouldReturnUserDetails() {
        var user = new User();
        var localFeatureFlag = new FeatureFlag();
        localFeatureFlag.setName("local");
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setEnabled(true);
        user.setFeatureFlags(Set.of(localFeatureFlag));
        doReturn(Optional.of(user)).when(userRepository).findByUsername(USERNAME);
        var globalFeatureFlag = new FeatureFlag();
        globalFeatureFlag.setName("global");
        doReturn(List.of(globalFeatureFlag)).when(featureFlagRepository).findAllByGlobal(true);

        var userDetails = featureFlagUserDetailsService.loadUserByUsername(USERNAME);

        assertThat(userDetails.getUsername()).isEqualTo(USERNAME);
        assertThat(userDetails.getPassword()).isEqualTo(PASSWORD);
        assertThat(userDetails.isEnabled()).isTrue();
        assertThat(userDetails.getAuthorities()).hasSize(2);
    }
}
