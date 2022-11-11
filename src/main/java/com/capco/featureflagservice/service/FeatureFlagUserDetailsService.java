package com.capco.featureflagservice.service;

import com.capco.featureflagservice.data.model.FeatureFlag;
import com.capco.featureflagservice.data.repository.FeatureFlagRepository;
import com.capco.featureflagservice.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeatureFlagUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final FeatureFlagRepository featureFlagRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .map(user -> {
                var authorities = new HashSet<String>();
                authorities.addAll(featureFlagsToAuthorities(user.getFeatureFlags()));
                authorities.addAll(featureFlagsToAuthorities(featureFlagRepository.findAllByGlobal(true)));
                return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .disabled(!user.isEnabled())
                    .authorities(authorities.toArray(String[]::new))
                    .build();
            })
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User with name %s not found", username)));
    }

    private Set<String> featureFlagsToAuthorities(Collection<FeatureFlag> featureFlags) {
        return featureFlags.stream().map(featureFlag -> "ROLE_" + featureFlag.getName()).collect(Collectors.toSet());
    }
}
