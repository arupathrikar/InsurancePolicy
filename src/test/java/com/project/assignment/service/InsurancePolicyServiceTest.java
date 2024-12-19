package com.project.assignment.service;

import com.project.assignment.entity.InsurancePolicy;
import com.project.assignment.repository.InsurancePolicyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class InsurancePolicyServiceTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private InsurancePolicyRepository repository;

    @Test
    @DisplayName("Create and retrieve a policy")
    public void createAndRetrievePolicyTest() {
        String baseUrl = "/api/policies";

        // Create a new policy
        InsurancePolicy newPolicy = new InsurancePolicy(null, "POL003", "MNO", 5000.0);
        ResponseEntity<String> createResponse = restTemplate.postForEntity(baseUrl, newPolicy, String.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isEqualTo("Policy Created Successfully");

        // Retrieve the policy
        ResponseEntity<InsurancePolicy> getResponse =
                restTemplate.getForEntity(baseUrl + "/POL003", InsurancePolicy.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getPolicyHolderName()).isEqualTo("MNO");
    }

    @Test
    @DisplayName("Retrieve a non-existent policy")
    public void getNonExistentPolicyTest() {
        String baseUrl = "/api/policies/999999";

        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Policy Does Not Exist");
    }
}
