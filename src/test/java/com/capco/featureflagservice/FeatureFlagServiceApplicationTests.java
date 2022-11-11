package com.capco.featureflagservice;

import com.capco.featureflagservice.controller.FeatureFlagController;
import com.capco.featureflagservice.controller.FeatureFlagManagementController;
import com.capco.featureflagservice.service.FeatureFlagManagementService;
import com.capco.featureflagservice.service.FeatureFlagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FeatureFlagServiceApplicationTests {

    @Autowired
    private FeatureFlagController featureFlagController;

    @Autowired
    private FeatureFlagManagementController featureFlagManagementController;

    @Autowired
    private FeatureFlagService featureFlagService;

    @Autowired
    private FeatureFlagManagementService featureFlagManagementService;

    @Test
    void contextLoads() {
        assertThat(featureFlagController).isNotNull();
        assertThat(featureFlagManagementController).isNotNull();
        assertThat(featureFlagService).isNotNull();
        assertThat(featureFlagManagementService).isNotNull();
    }
}
