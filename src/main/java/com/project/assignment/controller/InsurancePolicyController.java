package com.project.assignment.controller;

import com.project.assignment.entity.InsurancePolicy;
import com.project.assignment.service.InsurancePolicyService;
import com.project.assignment.service.SnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/policies")
public class InsurancePolicyController {

    @Autowired
    private InsurancePolicyService service;
    private SnsService snsService;

    //needed to implement test cases
    public InsurancePolicyController(InsurancePolicyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createPolicy(@Valid @RequestBody InsurancePolicy policy) {
        service.savePolicy(policy);
        snsService.publishPolicy(policy);
        return new ResponseEntity<>("Policy Created and Published to SNS Successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InsurancePolicy>> getAllPolicies() {
        List<InsurancePolicy> policies = service.getAllPolicies();
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/{policyNumber}")
    public ResponseEntity<Object> getPolicy(@PathVariable String policyNumber) {
        Optional<InsurancePolicy> policy = service.findPolicyByNumber(policyNumber);

        if (policy.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(policy.get());
        } else {
            return new ResponseEntity<>("Policy Does Not Exist", HttpStatus.NOT_FOUND);
        }
    }
}