package com.capco.featureflagservice.controller;

import com.capco.featureflagservice.configuration.SecurityConfiguration;
import com.capco.featureflagservice.service.FeatureFlagService;
import com.capco.featureflagservice.service.FeatureFlagUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({FeatureFlagController.class, SecurityConfiguration.class})
@ExtendWith(SpringExtension.class)
class FeatureFlagControllerTest {

    @MockBean
    private FeatureFlagService featureFlagService;

    @MockBean
    private FeatureFlagUserDetailsService featureFlagUserDetailsService;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test", roles = "TEST")
    void shouldReturn403OnForbiddenUser() throws Exception {
        mvc.perform(get("/feature-flag"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test", roles = "USER")
    void shouldReturn200OnAllowedUser() throws Exception {
        doReturn(Set.of("featureOne")).when(featureFlagService).getFeatureFlags("test");

        mvc.perform(get("/feature-flag"))
            .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("featureOne")));
    }

    @Test
    @WithMockUser(username = "test", roles = "ADMIN")
    void shouldReturn200OnAllowedAdmin() throws Exception {
        doReturn(Set.of("featureOne")).when(featureFlagService).getFeatureFlags("test");

        mvc.perform(get("/feature-flag"))
            .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("featureOne")));
    }
}
