package com.project.assignment.service;

import com.project.assignment.entity.InsurancePolicy;
import com.project.assignment.entity.DynamoDbPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
public class SqsService {

    private final DynamoDbService dynamoDbService;
    private final ObjectMapper objectMapper;

    public SqsService(DynamoDbService dynamoDbService, ObjectMapper objectMapper) {
        this.dynamoDbService = dynamoDbService;
        this.objectMapper = objectMapper;
    }

    @SqsListener("policy-queue")
    public void handleMessage(String message) {
        try {
            InsurancePolicy policy = objectMapper.readValue(message, InsurancePolicy.class);
            DynamoDbPolicy dynamoDbPolicy = new DynamoDbPolicy(policy.getPolicyNumber(),
                    policy.getPolicyHolderName(),
                    policy.getPolicyAmount());
            dynamoDbService.saveToDynamoDb(dynamoDbPolicy);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process SQS message", e);
        }
    }
}