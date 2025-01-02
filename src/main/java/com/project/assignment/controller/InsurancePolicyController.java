package com.project.assignment.controller;

import com.project.assignment.entity.InsurancePolicy;
import com.project.assignment.entity.InsurancePolicyDynamoDb;
import com.project.assignment.service.SnsMessagingService;
import com.project.assignment.service.InsurancePolicyService;
import com.project.assignment.service.SqsMessageHandler;
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
    private SnsMessagingService snsMessagingService;

    @Autowired
    private SqsMessageHandler sqsMessageHandler;

    @Autowired
    public InsurancePolicyController(SnsMessagingService snsMessagingService) {
        this.snsMessagingService = snsMessagingService;
    }

    //needed to implement test cases
    public InsurancePolicyController(InsurancePolicyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createPolicy(@Valid @RequestBody InsurancePolicy policy) {
        service.savePolicy(policy);
        //awsMessagingService.publishToSns(policy);
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

    @PostMapping("/create")
    public String createPolicy(@RequestParam String topicArn, @RequestBody String policyDetails) {
        try {
            snsMessagingService.publishToSns(topicArn, policyDetails);
            return "Policy created and message sent!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/process-sqs")
    public String processSqsMessages(@RequestBody InsurancePolicyDynamoDb policy) {
        System.out.println("Received policy: " + policy);
        try {
            sqsMessageHandler.processMessages();
            return "Messages processed and saved to DynamoDB!";
        } catch (Exception e) {
            return "Error occurred while processing SQS messages: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete-message")
    public String deleteSqsMessage(@RequestParam String receiptHandle) {
        sqsMessageHandler.deleteMessageFromQueue(receiptHandle);
        return "Message deleted from queue!";
    }
}