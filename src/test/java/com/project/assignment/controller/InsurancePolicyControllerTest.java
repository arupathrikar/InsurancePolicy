package com.project.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.assignment.entity.InsurancePolicy;
import com.project.assignment.service.InsurancePolicyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class InsurancePolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InsurancePolicyService policyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get all policies")
    @Order(1)
    public void getAllPoliciesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/policies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Get policy by number")
    @Order(2)
    public void getPolicyByNumberTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/policies/POL001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyHolderName").value("ABC"));
    }

    @Test
    @DisplayName("Create a new policy")
    @Order(3)
    public void createPolicyTest() throws Exception {
        InsurancePolicy newPolicy = new InsurancePolicy(null, "POL002", "PQR", 60000.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPolicy)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Policy Created Successfully"));
    }

    @Test
    @DisplayName("Create policy with invalid data")
    @Order(4)
    public void createPolicyInvalidDataTest() throws Exception {
        String invalidRequest = """
                {
                    "policyNumber": "",
                    "policyHolderName": "",
                    "policyAmount": ""
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.policyNumber").value("Policy number is mandatory"))
                .andExpect(jsonPath("$.policyHolderName").value("Policy holder name is mandatory"))
                .andExpect(jsonPath("$.policyAmount").value("Policy amount is mandatory"));
    }
}
