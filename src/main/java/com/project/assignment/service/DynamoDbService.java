package com.project.assignment.service;

import com.project.assignment.entity.InsurancePolicyDynamoDb;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.Map;

@Service
public class DynamoDbService {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDbService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    // Save the policy to DynamoDB
    public void saveInsurancePolicyToDynamoDb(InsurancePolicyDynamoDb policy) {
        // Validate input fields to avoid empty or null values
        if (policy.getPolicyNumber() == null || policy.getPolicyNumber().isEmpty()) {
            throw new IllegalArgumentException("PolicyNumber cannot be null or empty.");
        }
        if (policy.getPolicyHolderName() == null || policy.getPolicyHolderName().isEmpty()) {
            throw new IllegalArgumentException("PolicyHolderName cannot be null or empty.");
        }
        if (policy.getPolicyAmount() == null || policy.getPolicyAmount() <= 0) {
            throw new IllegalArgumentException("PolicyAmount must be a positive number.");
        }

        // Convert policyAmount to string
        String policyAmountString = policy.getPolicyAmount().toString();

        // Build the DynamoDB item
        Map<String, AttributeValue> item = Map.of(
                "PolicyNumber", AttributeValue.builder().s(policy.getPolicyNumber()).build(),
                "PolicyHolderName", AttributeValue.builder().s(policy.getPolicyHolderName()).build(),
                "PolicyAmount", AttributeValue.builder().n(policyAmountString).build()
        );

        // Create a PutItem request
        PutItemRequest request = PutItemRequest.builder()
                .tableName("InsurancePolicies")
                .item(item)
                .build();

        // Log the request to check the data before insertion
        System.out.println("Inserting item into DynamoDB: " + item);

        // Insert into DynamoDB
        try {
            dynamoDbClient.putItem(request);
        } catch (Exception e) {
            System.err.println("Error while inserting item into DynamoDB: " + e.getMessage());
            throw e;  // Re-throw the exception to ensure the caller is notified
        }
    }
}