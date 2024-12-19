package com.project.assignment.service;

import com.project.assignment.entity.InsurancePolicy;
import com.project.assignment.repository.InsurancePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class InsurancePolicyService {

    @Autowired
    private InsurancePolicyRepository repository;

    //needed for tests
    public InsurancePolicyService(InsurancePolicyRepository repository) {
        this.repository = repository;
    }

    public InsurancePolicy savePolicy(InsurancePolicy policy) {
        return repository.save(policy);
    }

    public List<InsurancePolicy> getAllPolicies() {
        return repository.findAll();
    }

    public Optional<InsurancePolicy> findPolicyByNumber(String policyNumber) {
        return repository.findByPolicyNumber(policyNumber);
    }
}