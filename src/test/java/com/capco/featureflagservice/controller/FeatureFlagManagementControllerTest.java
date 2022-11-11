package com.capco.featureflagservice.controller;

import com.capco.featureflagservice.configuration.SecurityConfiguration;
import com.capco.featureflagservice.controller.model.CreateFeatureFlagRequest;
import com.capco.featureflagservice.service.FeatureFlagManagementService;
import com.capco.featureflagservice.service.FeatureFlagUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({FeatureFlagManagementController.class, SecurityConfiguration.class})
@ExtendWith(SpringExtension.class)
class FeatureFlagManagementControllerTest {

    @MockBean
    private FeatureFlagManagementService featureFlagManagementService;
    @MockBean
    private FeatureFlagUserDetailsService featureFlagUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test", roles = "USER")
    void shouldReturn403OnFeatureFlagCreationOnForbiddenUser() throws Exception {
        var value = new CreateFeatureFlagRequest();
        value.setName("name");

        mvc.perform(post("/management/feature-flag")
            .content(objectMapper.writeValueAsString(value))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test", roles = "ADMIN")
    void shouldReturn200OnFeatureFlagCreationOnAllowedUser() throws Exception {
        var value = new CreateFeatureFlagRequest();
        value.setName("name");

        mvc.perform(post("/management/feature-flag")
            .content(objectMapper.writeValueAsString(value))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(featureFlagManagementService).createFeatureFlag(value);
    }

    @Test
    @WithMockUser(username = "test", roles = "USER")
    void shouldReturn403OnFeatureFlagAssignmentOnForbiddenUser() throws Exception {
        var value = new CreateFeatureFlagRequest();
        value.setName("name");

        mvc.perform(put("/management/feature-flag/{featureFlagName}/assign/{username}", "flag", "user"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test", roles = "ADMIN")
    void shouldReturn200OnFeatureFlagAssignmentOnAllowedUser() throws Exception {
        var value = new CreateFeatureFlagRequest();
        value.setName("name");

        mvc.perform(put("/management/feature-flag/{featureFlagName}/assign/{username}", "flag", "user"))
            .andExpect(status().isOk());

        verify(featureFlagManagementService).assignFlagToUser("flag", "user");
    }
}
